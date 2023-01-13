/*
Author : Dolph Flynn

Copyright 2023 Dolph Flynn

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

package com.blackberry.jwteditor;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class ArgumentUtils {

    static <T, U> Stream<Arguments> cartesianProduct(Collection<T> a, Collection<U> b) {
        List<Arguments> arguments = new LinkedList<>();

        for (T t : a) {
            for (U u : b) {
                arguments.add(arguments(t, u));
            }
        }

        return arguments.stream();
    }

    static <T, U, V> Stream<Arguments> cartesianProduct(Collection<T> a, Collection<U> b, Collection<V> c) {
        List<Arguments> arguments = new LinkedList<>();

        for (T t : a) {
            for (U u : b) {
                for (V v : c) {
                    arguments.add(arguments(t, u, v));
                }
            }
        }

        return arguments.stream();
    }
}
