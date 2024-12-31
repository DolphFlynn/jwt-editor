/*
Author : Dolph Flynn

Copyright 2024 Dolph Flynn

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

package com.blackberry.jwteditor.operations;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

// Secret Source: https://github.com/wallarm/jwt-secrets
class WeakSecrets {
    private static final int TOTAL_NUMBER_OF_SECRETS = 103975;

    private final BufferedReader bufferedReader;
    private final Object lock;

    WeakSecrets() {
        InputStream inputStream = this.getClass().getResourceAsStream("/jwt.secrets.list.txt");

        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
        this.lock = new Object();
    }

    String next() {
        synchronized (lock) {
            try {
                return bufferedReader.readLine();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    int total() {
        return TOTAL_NUMBER_OF_SECRETS;
    }
}
