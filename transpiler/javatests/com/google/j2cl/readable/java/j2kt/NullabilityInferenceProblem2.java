/*
 * Copyright 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package j2kt;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class NullabilityInferenceProblem2 {
  interface Supplier<T extends @Nullable Object> {
    T get();
  }

  public abstract class Test<T extends @Nullable Object> {
    public <E extends T> E foo(Supplier<E> supplier) {
      E local = supplier.get(); // local is E? in Kotlin.
      return foo(local, supplier.get());
    }

    public <E extends T> E foo(E a, E b) {
      throw new RuntimeException();
    }
  }
}
