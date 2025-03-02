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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.time.ZoneId;
import java.util.stream.Stream;

import static com.blackberry.jwteditor.model.jose.TimeClaimType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TimeClaimTest {
    @EnumSource(TimeClaimType.class)
    @ParameterizedTest
    void invalidEpochTime(TimeClaimType type) {
        TimeClaim timeClaim = TimeClaimFactory.fromEpochSeconds(type, "-1");

        assertThat(timeClaim.type()).isEqualTo(type);
        assertThat(timeClaim.value()).isEqualTo("-1");
        assertThat(timeClaim.isValid()).isFalse();
        assertThat(timeClaim.hasDate()).isFalse();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isEmpty();
        assertThat(timeClaim.dateTime()).isNull();
    }

    @EnumSource(TimeClaimType.class)
    @ParameterizedTest
    void invalidDateString(TimeClaimType type) {
        TimeClaim timeClaim = TimeClaimFactory.fromIsoDateTime(type, "invalid");

        assertThat(timeClaim.type()).isEqualTo(type);
        assertThat(timeClaim.value()).isEqualTo("invalid");
        assertThat(timeClaim.isValid()).isFalse();
        assertThat(timeClaim.hasDate()).isFalse();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isEmpty();
        assertThat(timeClaim.dateTime()).isNull();
    }

    static Stream<Arguments> isoDateTimes() {
        return Stream.of(
                arguments("1985-04-12T23:20:50.52Z", "Fri Apr 12 1985 23:20:50 GMT"),
                arguments("1996-12-19T16:39:57-08:00", "Fri Dec 20 1996 00:39:57 GMT")
        );
    }


    @MethodSource("isoDateTimes")
    @ParameterizedTest
    void testIssuedAtTimeClaimsForIsoDateTimeStrings(String isoDateTime, String expectedDateTimeString) {
        TimeClaim timeClaim = TimeClaimFactory.fromIsoDateTime(ISSUED_AT_TIME, isoDateTime);

        assertThat(timeClaim.type()).isEqualTo(ISSUED_AT_TIME);
        assertThat(timeClaim.value()).isEqualTo(isoDateTime);
        assertThat(timeClaim.isValid()).isTrue();
        assertThat(timeClaim.hasDate()).isTrue();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isEqualTo(expectedDateTimeString);
        assertThat(timeClaim.dateTime()).isNotNull();
    }

    static Stream<Arguments> epochDateTimes() {
        return Stream.of(
                arguments(482196050L, "Fri Apr 12 1985 23:20:50 GMT"),
                arguments(851042397L, "Fri Dec 20 1996 00:39:57 GMT")
        );
    }

    @MethodSource("epochDateTimes")
    @ParameterizedTest
    void testIssuedAtTimeClaimsForEpochTimes(long epochDateTime, String expectedDateTimeString) {
        String epochDateTimeValue = Long.toString(epochDateTime);

        TimeClaim timeClaim = TimeClaimFactory.fromEpochSeconds(ISSUED_AT_TIME, epochDateTimeValue);

        assertThat(timeClaim.type()).isEqualTo(ISSUED_AT_TIME);
        assertThat(timeClaim.value()).isEqualTo(epochDateTimeValue);
        assertThat(timeClaim.isValid()).isTrue();
        assertThat(timeClaim.hasDate()).isTrue();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isEqualTo(expectedDateTimeString);
        assertThat(timeClaim.dateTime()).isNotNull();
    }

    @Test
    void testExpiredDatesInThePastAreInvalid() {
        long epochSeconds = Instant.now().getEpochSecond() - 1;
        String epochSecondsValue = Long.toString(epochSeconds);

        TimeClaim timeClaim = TimeClaimFactory.fromEpochSeconds(EXPIRATION_TIME, epochSecondsValue);

        assertThat(timeClaim.type()).isEqualTo(EXPIRATION_TIME);
        assertThat(timeClaim.value()).isEqualTo(epochSecondsValue);
        assertThat(timeClaim.isValid()).isFalse();
        assertThat(timeClaim.hasDate()).isTrue();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isNotEmpty();
        assertThat(timeClaim.dateTime()).isNotNull();
    }

    @Test
    void testExpiredDatesInTheFutureAreValid() {
        long epochSeconds = Instant.now().getEpochSecond() + 5;
        String epochSecondsValue = Long.toString(epochSeconds);

        TimeClaim timeClaim = TimeClaimFactory.fromEpochSeconds(EXPIRATION_TIME, epochSecondsValue);

        assertThat(timeClaim.type()).isEqualTo(EXPIRATION_TIME);
        assertThat(timeClaim.value()).isEqualTo(epochSecondsValue);
        assertThat(timeClaim.isValid()).isTrue();
        assertThat(timeClaim.hasDate()).isTrue();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isNotEmpty();
        assertThat(timeClaim.dateTime()).isNotNull();
    }

    @Test
    void testIssuedAtDatesInThePastAreValid() {
        long epochSeconds = Instant.now().getEpochSecond() - 1;
        String epochSecondsValue = Long.toString(epochSeconds);

        TimeClaim timeClaim = TimeClaimFactory.fromEpochSeconds(ISSUED_AT_TIME, epochSecondsValue);

        assertThat(timeClaim.type()).isEqualTo(ISSUED_AT_TIME);
        assertThat(timeClaim.value()).isEqualTo(epochSecondsValue);
        assertThat(timeClaim.isValid()).isTrue();
        assertThat(timeClaim.hasDate()).isTrue();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isNotEmpty();
        assertThat(timeClaim.dateTime()).isNotNull();
    }

    @Test
    void testIssuedAtDatesInTheFutureAreInvalid() {
        long epochSeconds = Instant.now().getEpochSecond() + 5;
        String epochSecondsValue = Long.toString(epochSeconds);

        TimeClaim timeClaim = TimeClaimFactory.fromEpochSeconds(ISSUED_AT_TIME, epochSecondsValue);

        assertThat(timeClaim.type()).isEqualTo(ISSUED_AT_TIME);
        assertThat(timeClaim.value()).isEqualTo(epochSecondsValue);
        assertThat(timeClaim.isValid()).isFalse();
        assertThat(timeClaim.hasDate()).isTrue();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isNotEmpty();
        assertThat(timeClaim.dateTime()).isNotNull();
    }
    @Test
    void testNotBeforeDatesInThePastAreValid() {
        long epochSeconds = Instant.now().getEpochSecond() - 1;
        String epochSecondsValue = Long.toString(epochSeconds);

        TimeClaim timeClaim = TimeClaimFactory.fromEpochSeconds(NOT_BEFORE_TIME, epochSecondsValue);

        assertThat(timeClaim.type()).isEqualTo(NOT_BEFORE_TIME);
        assertThat(timeClaim.value()).isEqualTo(epochSecondsValue);
        assertThat(timeClaim.isValid()).isTrue();
        assertThat(timeClaim.hasDate()).isTrue();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isNotEmpty();
        assertThat(timeClaim.dateTime()).isNotNull();
    }

    @Test
    void testNotBeforeDatesInTheFutureAreInvalid() {
        long epochSeconds = Instant.now().getEpochSecond() + 5;
        String epochSecondsValue = Long.toString(epochSeconds);

        TimeClaim timeClaim = TimeClaimFactory.fromEpochSeconds(NOT_BEFORE_TIME, epochSecondsValue);

        assertThat(timeClaim.type()).isEqualTo(NOT_BEFORE_TIME);
        assertThat(timeClaim.value()).isEqualTo(epochSecondsValue);
        assertThat(timeClaim.isValid()).isFalse();
        assertThat(timeClaim.hasDate()).isTrue();
        assertThat(timeClaim.date(ZoneId.of("UTC"))).isNotEmpty();
        assertThat(timeClaim.dateTime()).isNotNull();
    }

    static Stream<Arguments> expectedTimeZoneConversionsFromEpochSeconds() {
        return Stream.of(
                arguments("UTC", Long.toString(0L), "Thu Jan 01 1970 00:00:00 GMT"),
                arguments("UTC", Long.toString(946684800L), "Sat Jan 01 2000 00:00:00 GMT"),
                arguments("UTC", Long.toString(1751328000), "Tue Jul 01 2025 00:00:00 GMT"),
                arguments("UTC", Long.toString(2524608000L), "Sat Jan 01 2050 00:00:00 GMT"),
                arguments("America/Los_Angeles", Long.toString(0L), "Wed Dec 31 1969 16:00:00 GMT-8"),
                arguments("America/Los_Angeles", Long.toString(946684800L), "Fri Dec 31 1999 16:00:00 GMT-8"),
                arguments("America/Los_Angeles", Long.toString(1751328000), "Mon Jun 30 2025 17:00:00 GMT-7"),
                arguments("America/Los_Angeles", Long.toString(2524608000L), "Fri Dec 31 2049 16:00:00 GMT-8"),
                arguments("Asia/Seoul", Long.toString(0L), "Thu Jan 01 1970 09:00:00 GMT+9"),
                arguments("Asia/Seoul", Long.toString(946684800L), "Sat Jan 01 2000 09:00:00 GMT+9"),
                arguments("Asia/Seoul", Long.toString(1751328000), "Tue Jul 01 2025 09:00:00 GMT+9"),
                arguments("Asia/Seoul", Long.toString(2524608000L), "Sat Jan 01 2050 09:00:00 GMT+9")
        );
    }

    @MethodSource("expectedTimeZoneConversionsFromEpochSeconds")
    @ParameterizedTest
    void testDateStringTimeZoneFromEpochSeconds(String timeZoneName, String epochSeconds, String expectedString) {
        TimeClaim iat = TimeClaimFactory.fromEpochSeconds(ISSUED_AT_TIME, epochSeconds);

        assertThat(iat.date(ZoneId.of(timeZoneName))).isEqualTo(expectedString);
    }

    static Stream<Arguments> expectedTimeZoneConversionsFromIsoDateTime() {
        return Stream.of(
                arguments("UTC", "1970-01-01T00:00:00.00Z", "Thu Jan 01 1970 00:00:00 GMT"),
                arguments("UTC", "1999-12-31T16:00:00.00-08:00", "Sat Jan 01 2000 00:00:00 GMT"),
                arguments("UTC", "2025-07-01T02:00:00.00+02:00", "Tue Jul 01 2025 00:00:00 GMT"),
                arguments("UTC", "2050-01-01T09:00:00.00+09:00", "Sat Jan 01 2050 00:00:00 GMT"),
                arguments("America/Los_Angeles", "1970-01-01T00:00:00.00Z", "Wed Dec 31 1969 16:00:00 GMT-8"),
                arguments("America/Los_Angeles", "1999-12-31T16:00:00.00-08:00", "Fri Dec 31 1999 16:00:00 GMT-8"),
                arguments("America/Los_Angeles", "2025-07-01T02:00:00.00+02:00", "Mon Jun 30 2025 17:00:00 GMT-7"),
                arguments("America/Los_Angeles", "2050-01-01T09:00:00.00+09:00", "Fri Dec 31 2049 16:00:00 GMT-8"),
                arguments("Asia/Seoul", "1970-01-01T00:00:00.00Z", "Thu Jan 01 1970 09:00:00 GMT+9"),
                arguments("Asia/Seoul", "1999-12-31T16:00:00.00-08:00", "Sat Jan 01 2000 09:00:00 GMT+9"),
                arguments("Asia/Seoul", "2025-07-01T02:00:00.00+02:00", "Tue Jul 01 2025 09:00:00 GMT+9"),
                arguments("Asia/Seoul", "2050-01-01T09:00:00.00+09:00", "Sat Jan 01 2050 09:00:00 GMT+9")
        );
    }

    @MethodSource("expectedTimeZoneConversionsFromIsoDateTime")
    @ParameterizedTest
    void testDateStringTimeZoneFromIsoDateTime(String timeZoneName, String isoDateTime, String expectedString) {
        TimeClaim iat = TimeClaimFactory.fromIsoDateTime(ISSUED_AT_TIME, isoDateTime);
        assertThat(iat.date(ZoneId.of(timeZoneName))).isEqualTo(expectedString);
    }
}