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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;

public record TimeClaim(TimeClaimType type, String data, Long value) {

    public boolean isValid() {
        return type.isValid(value);
    }

    public String warning() {
        if (isValid()) {
            return "";
        }

        if (value == null || value < 0) {
            return "'%s' value is invalid".formatted(type.name);
        }

        String futurePast = type.dateInThePastRequired() ? "future" : "past";

        return "'%s' date is in the %s".formatted(type.name, futurePast);
    }

    static List<TimeClaim> from(String payloadJson) {
        Optional<JSONObject> optional = parsePayload(payloadJson);

        if (optional.isEmpty()) {
            return emptyList();
        }

        JSONObject jsonObject = optional.get();

        return stream(TimeClaimType.values())
                .filter(type -> jsonObject.has(type.name))
                .map(type -> new TimeClaim(
                        type,
                        jsonObject.get(type.name).toString(),
                        numberValue(jsonObject, type.name))
                )
                .toList();
    }

    private static Optional<JSONObject> parsePayload(String payloadJson) {
        try {
            return Optional.of(new JSONObject(payloadJson));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    private static Long numberValue(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getLong(name);
        } catch (JSONException e) {
            return null;
        }
    }
}
