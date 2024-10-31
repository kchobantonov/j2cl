/*
 * Copyright 2022 Google Inc.
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
package protobuf;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Internal.ProtoNonnullApi;
import javaemul.internal.annotations.KtProperty;

@ProtoNonnullApi
public class MyMessage extends GeneratedMessage {

  @KtProperty
  public int getTestField() {
    throw new RuntimeException();
  }

  public static MyMessage getDefaultInstance() {
    throw new RuntimeException();
  }

  public static Builder newBuilder() {
    throw new RuntimeException();
  }

  private MyMessage(int testField) {
    throw new RuntimeException();
  }

  @ProtoNonnullApi
  public static class Builder extends GeneratedMessage.Builder {
    @KtProperty
    public int getTestField() {
      throw new RuntimeException();
    }

    public Builder setTestField(int testField) {
      throw new RuntimeException();
    }

    public MyMessage build() {
      throw new RuntimeException();
    }

    private Builder() {}
  }
}
