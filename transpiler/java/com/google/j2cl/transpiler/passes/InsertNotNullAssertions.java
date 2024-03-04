/*
 * Copyright 2021 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.j2cl.transpiler.passes;

import com.google.j2cl.common.SourcePosition;
import com.google.j2cl.transpiler.ast.AbstractRewriter;
import com.google.j2cl.transpiler.ast.ArrayAccess;
import com.google.j2cl.transpiler.ast.ArrayLength;
import com.google.j2cl.transpiler.ast.AssertStatement;
import com.google.j2cl.transpiler.ast.CompilationUnit;
import com.google.j2cl.transpiler.ast.ConditionalExpression;
import com.google.j2cl.transpiler.ast.Expression;
import com.google.j2cl.transpiler.ast.ForEachStatement;
import com.google.j2cl.transpiler.ast.HasSourcePosition;
import com.google.j2cl.transpiler.ast.MemberReference;
import com.google.j2cl.transpiler.ast.MethodCall;
import com.google.j2cl.transpiler.ast.MultiExpression;
import com.google.j2cl.transpiler.ast.Node;
import com.google.j2cl.transpiler.ast.NullLiteral;
import com.google.j2cl.transpiler.ast.StringLiteral;
import com.google.j2cl.transpiler.ast.SwitchStatement;
import com.google.j2cl.transpiler.ast.SynchronizedStatement;
import com.google.j2cl.transpiler.ast.ThrowStatement;
import com.google.j2cl.transpiler.ast.TypeDescriptor;
import com.google.j2cl.transpiler.ast.TypeDescriptors;
import com.google.j2cl.transpiler.ast.Variable;
import com.google.j2cl.transpiler.ast.VariableDeclarationExpression;
import com.google.j2cl.transpiler.passes.ConversionContextVisitor.ContextRewriter;

/**
 * Inserts NOT_NULL_ASSERTION (!!) in places where Java performs implicit null-check, and when
 * conversion is needed from nullable to non-null type.
 */
public final class InsertNotNullAssertions extends NormalizationPass {
  @Override
  public void applyTo(CompilationUnit compilationUnit) {
    // Insert non-null assertions when converting from nullable to non-null type.
    // We run this first before adding any other not null assertions since the surrounding context
    // is obscured from this rewriter. If we ran this later we may emit double not-null assertions
    // we would be unaware that we're already surrounded by one.
    compilationUnit.accept(
        new ConversionContextVisitor(
            new ContextRewriter() {
              @Override
              public Expression rewriteTypeConversionContext(
                  TypeDescriptor inferredTypeDescriptor,
                  TypeDescriptor actualTypeDescriptor,
                  Expression expression) {

                if (expression instanceof MethodCall
                    && !expression.getTypeDescriptor().canBeNull()) {
                  // Do not insert a null assertion if the return type of a call inferred to be
                  // non-null. Kotlin does not null check these situations
                  // (https://youtrack.jetbrains.com/issue/KT-8135) and there is existing code
                  // that takes advantage of that.
                  return expression;
                }

                return !TypeDescriptors.isJavaLangVoid(inferredTypeDescriptor)
                        && (!inferredTypeDescriptor.canBeNull()
                            || !actualTypeDescriptor.canBeNull())
                    ? insertNotNullAssertionIfNeeded(getSourcePosition(), expression)
                    : expression;
              }
            }));

    // Insert null assertions if necessary on places where the construct requires them.
    // TODO(b/236987392): Revisit when the bug is fixed. The context rewriter based traversal above
    // should be enough to emit most of these assertions. But in the current state whether a
    // construct requires a non-nullable expression is expressed by a non-nullable type which for
    // type variables is not accurate.
    compilationUnit.accept(
        new AbstractRewriter() {
          @Override
          public Node rewriteAssertStatement(AssertStatement assertStatement) {
            return AssertStatement.Builder.from(assertStatement)
                .setMessage(
                    insertElvisIfNeeded(assertStatement.getMessage(), new StringLiteral("null")))
                .build();
          }

          @Override
          public Node rewriteArrayAccess(ArrayAccess arrayAccess) {
            return ArrayAccess.Builder.from(arrayAccess)
                .setArrayExpression(
                    insertNotNullAssertionIfNeeded(
                        getSourcePosition(), arrayAccess.getArrayExpression()))
                .build();
          }

          @Override
          public Node rewriteArrayLength(ArrayLength arrayLength) {
            return ArrayLength.Builder.from(arrayLength)
                .setArrayExpression(
                    insertNotNullAssertionIfNeeded(
                        getSourcePosition(), arrayLength.getArrayExpression()))
                .build();
          }

          @Override
          public Node rewriteForEachStatement(ForEachStatement forEachStatement) {
            return ForEachStatement.Builder.from(forEachStatement)
                .setIterableExpression(
                    insertNotNullAssertionIfNeeded(
                        forEachStatement.getSourcePosition(),
                        forEachStatement.getIterableExpression()))
                .build();
          }

          @Override
          public Node rewriteMemberReference(MemberReference memberReference) {
            return MemberReference.Builder.from(memberReference)
                .setQualifier(
                    insertNotNullAssertionIfNeeded(
                        getSourcePosition(), memberReference.getQualifier()))
                .build();
          }

          @Override
          public Node rewriteSwitchStatement(SwitchStatement switchStatement) {
            return SwitchStatement.Builder.from(switchStatement)
                .setSwitchExpression(
                    insertNotNullAssertionIfNeeded(
                        switchStatement.getSourcePosition(), switchStatement.getSwitchExpression()))
                .build();
          }

          @Override
          public Node rewriteSynchronizedStatement(SynchronizedStatement synchronizedStatement) {
            return SynchronizedStatement.Builder.from(synchronizedStatement)
                .setExpression(
                    insertNotNullAssertionIfNeeded(
                        synchronizedStatement.getSourcePosition(),
                        synchronizedStatement.getExpression()))
                .build();
          }

          @Override
          public Node rewriteThrowStatement(ThrowStatement throwStatement) {
            return ThrowStatement.Builder.from(throwStatement)
                .setExpression(
                    insertNotNullAssertionIfNeeded(
                        throwStatement.getSourcePosition(), throwStatement.getExpression()))
                .build();
          }

          private SourcePosition getSourcePosition() {
            return ((HasSourcePosition) getParent(HasSourcePosition.class::isInstance))
                .getSourcePosition();
          }
        });
  }

  private Expression insertNotNullAssertionIfNeeded(
      SourcePosition sourcePosition, Expression expression) {
    if (expression == null || !expression.canBeNull()) {
      // Don't insert null-check for expressions which are known to be non-null, regardless of
      // nullability annotations.
      return expression;
    }
    if (expression instanceof NullLiteral) {
      getProblems().warning(sourcePosition, "Non-null assertion applied to null.");
    }

    return expression.postfixNotNullAssertion();
  }

  private static Expression insertElvisIfNeeded(
      Expression expression, Expression nonNullExpression) {
    if (expression == null || !expression.canBeNull()) {
      // Don't insert null-check for expressions which are known to be non-null, regardless of
      // nullability annotations.
      return expression;
    }

    if (expression instanceof NullLiteral) {
      return nonNullExpression;
    }

    MultiExpression.Builder elvisExpressionBuilder = MultiExpression.newBuilder();
    if (!expression.isIdempotent()) {
      Variable elvisVariable =
          Variable.newBuilder()
              .setName("tmp")
              .setFinal(true)
              .setTypeDescriptor(expression.getTypeDescriptor())
              .build();
      elvisExpressionBuilder.addExpressions(
          VariableDeclarationExpression.newBuilder()
              .addVariableDeclaration(elvisVariable, expression)
              .build());
      expression = elvisVariable.createReference();
    }

    return elvisExpressionBuilder
        .addExpressions(
            ConditionalExpression.newBuilder()
                .setConditionExpression(expression.infixEqualsNull())
                .setTrueExpression(nonNullExpression)
                .setFalseExpression(expression.clone())
                .setTypeDescriptor(TypeDescriptors.get().javaLangObject.toNonNullable())
                .build())
        .build();
  }
}
