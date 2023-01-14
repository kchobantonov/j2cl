/*
 * Copyright 2023 Google Inc.
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
package com.google.j2cl.transpiler.backend.kotlin.objc

import com.google.j2cl.transpiler.backend.kotlin.source.Source
import com.google.j2cl.transpiler.backend.kotlin.source.plus
import com.google.j2cl.transpiler.backend.kotlin.source.plusSemicolon
import com.google.j2cl.transpiler.backend.kotlin.source.source
import com.google.j2cl.transpiler.backend.kotlin.source.spaceSeparated

fun comment(source: Source) = spaceSeparated(source("//"), source)

fun semicolonEnded(source: Source) = source.plusSemicolon

fun return_(source: Source) = spaceSeparated(source("return"), source)