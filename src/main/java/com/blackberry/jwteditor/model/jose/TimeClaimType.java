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

package com.blackberry.jwteditor.model.jose;

import java.time.Instant;

import static java.time.Instant.now;

public enum TimeClaimType {
    EXPIRATION_TIME("exp", "Expiration Time"),
    NOT_BEFORE_TIME("nbf", "Not Before"),
    ISSUED_AT_TIME("iat", "Issued At");

    final String name;
    private final String displayName;

    TimeClaimType(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public boolean isValid(Long value) {
        if (value == null || value < 0) {
            return false;
        }

        Instant valueTime = Instant.ofEpochSecond(value);

        return dateInThePastRequired() ? valueTime.isBefore(now()) : valueTime.isAfter(now());
    }

    private boolean dateInThePastRequired() {
        return switch (this) {
            case EXPIRATION_TIME -> false;
            case NOT_BEFORE_TIME, ISSUED_AT_TIME -> true;
        };
    }

    @Override
    public String toString() {
        return displayName;
    }
}
