/*
Author : Dolph Flynn

Copyright 2025 Dolph Flynn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.blackberry.jwteditor.model.tokens;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenIdGenerator {
    private final AtomicInteger counter;

    public TokenIdGenerator() {
        this(0);
    }

    public TokenIdGenerator(int lastId) {
        counter = new AtomicInteger(lastId);
    }

    public int next() {
        return counter.incrementAndGet();
    }
}
