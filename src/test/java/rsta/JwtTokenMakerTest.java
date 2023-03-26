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

package rsta;

import com.blackberry.jwteditor.view.rsta.jwt.JWTTokenMaker;
import org.fife.ui.rsyntaxtextarea.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;

import static com.blackberry.jwteditor.view.rsta.jwt.JWTTokenizerConstants.MAPPING;
import static com.blackberry.jwteditor.view.rsta.jwt.JWTTokenizerConstants.TOKEN_MAKER_FQCN;
import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenMakerTest {

    private TokenMaker tokenMaker;

    @BeforeAll
    static void registerTokenMaker() {
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping(MAPPING, TOKEN_MAKER_FQCN);
    }

    @BeforeEach
    void createTokenMaker() {
        tokenMaker = TokenMakerFactory.getDefaultInstance().getTokenMaker(MAPPING);
    }

    @Test
    void tokenMakerInstanceShouldBeCorrect() {
        assertThat(tokenMaker).isInstanceOf(JWTTokenMaker.class);
    }

    @Test
    void tokensMakerParsesNoDataCorrectly() {
        char[] data = new char[0];
        Segment segment = new Segment(data, 0, data.length);

        Token firstToken = tokenMaker.getTokenList(segment, TokenTypes.NULL, 0);

        assertThat(firstToken.getType()).isEqualTo(TokenTypes.NULL);
        assertThat(firstToken.getLexeme()).isEqualTo(null);
    }

    @Test
    void tokensMakerParsesJWSCorrectly() {
        char[] data = "eyJhb.GciOi.JFUzM4".toCharArray();
        Segment segment = new Segment(data, 0, data.length);

        Token token1 = tokenMaker.getTokenList(segment, TokenTypes.NULL, 0);

        assertThat(token1.getType()).isEqualTo(JWTTokenMaker.JWT_PART1);
        assertThat(token1.getLexeme()).isEqualTo("eyJhb");

        Token token2 = token1.getNextToken();

        assertThat(token2).isNotNull();
        assertThat(token2.getType()).isEqualTo(JWTTokenMaker.JWT_SEPARATOR1);
        assertThat(token2.getLexeme()).isEqualTo(".");

        Token token3 = token2.getNextToken();

        assertThat(token3).isNotNull();
        assertThat(token3.getType()).isEqualTo(JWTTokenMaker.JWT_PART2);
        assertThat(token3.getLexeme()).isEqualTo("GciOi");

        Token token4 = token3.getNextToken();

        assertThat(token4).isNotNull();
        assertThat(token4.getType()).isEqualTo(JWTTokenMaker.JWT_SEPARATOR2);
        assertThat(token4.getLexeme()).isEqualTo(".");

        Token token5 = token4.getNextToken();

        assertThat(token5).isNotNull();
        assertThat(token5.getType()).isEqualTo(JWTTokenMaker.JWT_PART3);
        assertThat(token5.getLexeme()).isEqualTo("JFUzM4");

        Token token6 = token5.getNextToken();

        assertThat(token6).isNotNull();
        assertThat(token6.getType()).isEqualTo(TokenTypes.NULL);
        assertThat(token6.getLexeme()).isNull();

        assertThat(token6.getNextToken()).isNull();
    }

    @Test
    void tokensMakerParsesUnsignedJWSCorrectly() {
        char[] data = "eyJhb.GciOi.".toCharArray();
        Segment segment = new Segment(data, 0, data.length);

        Token token1 = tokenMaker.getTokenList(segment, TokenTypes.NULL, 0);

        assertThat(token1.getType()).isEqualTo(JWTTokenMaker.JWT_PART1);
        assertThat(token1.getLexeme()).isEqualTo("eyJhb");

        Token token2 = token1.getNextToken();

        assertThat(token2).isNotNull();
        assertThat(token2.getType()).isEqualTo(JWTTokenMaker.JWT_SEPARATOR1);
        assertThat(token2.getLexeme()).isEqualTo(".");

        Token token3 = token2.getNextToken();

        assertThat(token3).isNotNull();
        assertThat(token3.getType()).isEqualTo(JWTTokenMaker.JWT_PART2);
        assertThat(token3.getLexeme()).isEqualTo("GciOi");

        Token token4 = token3.getNextToken();

        assertThat(token4).isNotNull();
        assertThat(token4.getType()).isEqualTo(JWTTokenMaker.JWT_SEPARATOR2);
        assertThat(token4.getLexeme()).isEqualTo(".");

        Token token5 = token4.getNextToken();

        assertThat(token5).isNotNull();
        assertThat(token5.getType()).isEqualTo(TokenTypes.NULL);
        assertThat(token5.getLexeme()).isNull();

        assertThat(token5.getNextToken()).isNull();
    }

    @Test
    void tokensMakerParsesJWECorrectly() {
        char[] data = "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiUlNBMV81In0.Q_D7l_-OG0UyYS17Bmduszqlpz09Iuu2aJ8_s2TgRlf11ppfTp_PkS0ALErgZURkigiVotAGxuulf9IrUUTBqP36YLFmPQVWnYgUh5ztULu3Www524wlj0bOCjoFFOf5Ek1-WFwH38RLRSdZIBG5w7NMUHZ5ac2-clWYRDYFS-MQ5WOHr1D16bFenXsRWTgATkrgSNaRMiSa1KVtRKHds79wqZw-V07aTFQDBJZmgCoZN2YX0JQc3MSeAoD89gG8_ZqtCm0UjduedktFcks9Mac4_Hvsqnw80siKuSp2sQuxKhLcMx8pGxD4ZtgRPE85GBJqS1M0H8T7vyJMouIzhA.dqaD7Kx6S579X1C0.grrQ7GFxTCXB1PidGWOQ6v-2WEivjP_B_gl2VdQxsOhSQeC5eTbKgU7QTfFah_qEPMU5wZ2xT4RYduMqTNavyhqUQIgArI1UnWueQ3rHLHS7Wd9SNTuZQ9tR1Y-y7gxLQ24G6pfLaA-wQbmeaKaqoAnFTLuEa3vFq4jP5L_8S5SlFtpIf8d1FebCGI4EXBRCFZ_bnuxpR7FKCOUgCfL9kyBS2DdBGMDHDj59HhtvJ1R0wSAlC_m7SnjGrzpJUmUqQoIeIykr28ZRjwzeHgO9wjxSnMjCXuGwwKw_tmkVIjCGi3EAmdqaXDWPsd9iefnhXj6uaaA4RUidhpWuCXqV-saA-12uwiO9AKJwXgfkEr-7ndEWtWg4EHFVLyFwVk232WXjemTXjFKbv0trq544484Qm42jjm_QIGxi-1Jnh1dIOrlozhH-3CltUU3HJAhjSogyoeBeJ27KOMd21zIVUapsKmL9v8i8_ZVskzhXYc-DU626PE6U1ekJfkdUmuQqmzCoOZgsWlD5l0GFRzhIGkrWBVQOEyhBwPd54otdNbq4vANPrMxd-Uyzer0I-AwPRKm814Fjx4gX_gvpAOTwCHlcvl3fcg.B3Rezq51U3P7uCjRokYIzw".toCharArray();
        Segment segment = new Segment(data, 0, data.length);

        Token token1 = tokenMaker.getTokenList(segment, TokenTypes.NULL, 0);

        assertThat(token1.getType()).isEqualTo(JWTTokenMaker.JWT_PART1);
        assertThat(token1.getLexeme()).isEqualTo("eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiUlNBMV81In0");

        Token token2 = token1.getNextToken();

        assertThat(token2).isNotNull();
        assertThat(token2.getType()).isEqualTo(JWTTokenMaker.JWT_SEPARATOR1);
        assertThat(token2.getLexeme()).isEqualTo(".");

        Token token3 = token2.getNextToken();

        assertThat(token3).isNotNull();
        assertThat(token3.getType()).isEqualTo(JWTTokenMaker.JWT_PART2);
        assertThat(token3.getLexeme()).isEqualTo("Q_D7l_-OG0UyYS17Bmduszqlpz09Iuu2aJ8_s2TgRlf11ppfTp_PkS0ALErgZURkigiVotAGxuulf9IrUUTBqP36YLFmPQVWnYgUh5ztULu3Www524wlj0bOCjoFFOf5Ek1-WFwH38RLRSdZIBG5w7NMUHZ5ac2-clWYRDYFS-MQ5WOHr1D16bFenXsRWTgATkrgSNaRMiSa1KVtRKHds79wqZw-V07aTFQDBJZmgCoZN2YX0JQc3MSeAoD89gG8_ZqtCm0UjduedktFcks9Mac4_Hvsqnw80siKuSp2sQuxKhLcMx8pGxD4ZtgRPE85GBJqS1M0H8T7vyJMouIzhA");

        Token token4 = token3.getNextToken();

        assertThat(token4).isNotNull();
        assertThat(token4.getType()).isEqualTo(JWTTokenMaker.JWT_SEPARATOR2);
        assertThat(token4.getLexeme()).isEqualTo(".");

        Token token5 = token4.getNextToken();

        assertThat(token5).isNotNull();
        assertThat(token5.getType()).isEqualTo(JWTTokenMaker.JWT_PART3);
        assertThat(token5.getLexeme()).isEqualTo("dqaD7Kx6S579X1C0");

        Token token6 = token5.getNextToken();

        assertThat(token6).isNotNull();
        assertThat(token6.getType()).isEqualTo(JWTTokenMaker.JWT_SEPARATOR3);
        assertThat(token6.getLexeme()).isEqualTo(".");

        Token token7 = token6.getNextToken();

        assertThat(token7).isNotNull();
        assertThat(token7.getType()).isEqualTo(JWTTokenMaker.JWT_PART4);
        assertThat(token7.getLexeme()).isEqualTo("grrQ7GFxTCXB1PidGWOQ6v-2WEivjP_B_gl2VdQxsOhSQeC5eTbKgU7QTfFah_qEPMU5wZ2xT4RYduMqTNavyhqUQIgArI1UnWueQ3rHLHS7Wd9SNTuZQ9tR1Y-y7gxLQ24G6pfLaA-wQbmeaKaqoAnFTLuEa3vFq4jP5L_8S5SlFtpIf8d1FebCGI4EXBRCFZ_bnuxpR7FKCOUgCfL9kyBS2DdBGMDHDj59HhtvJ1R0wSAlC_m7SnjGrzpJUmUqQoIeIykr28ZRjwzeHgO9wjxSnMjCXuGwwKw_tmkVIjCGi3EAmdqaXDWPsd9iefnhXj6uaaA4RUidhpWuCXqV-saA-12uwiO9AKJwXgfkEr-7ndEWtWg4EHFVLyFwVk232WXjemTXjFKbv0trq544484Qm42jjm_QIGxi-1Jnh1dIOrlozhH-3CltUU3HJAhjSogyoeBeJ27KOMd21zIVUapsKmL9v8i8_ZVskzhXYc-DU626PE6U1ekJfkdUmuQqmzCoOZgsWlD5l0GFRzhIGkrWBVQOEyhBwPd54otdNbq4vANPrMxd-Uyzer0I-AwPRKm814Fjx4gX_gvpAOTwCHlcvl3fcg");

        Token token8 = token7.getNextToken();

        assertThat(token8).isNotNull();
        assertThat(token8.getType()).isEqualTo(JWTTokenMaker.JWT_SEPARATOR4);
        assertThat(token8.getLexeme()).isEqualTo(".");

        Token token9 = token8.getNextToken();

        assertThat(token9).isNotNull();
        assertThat(token9.getType()).isEqualTo(JWTTokenMaker.JWT_PART5);
        assertThat(token9.getLexeme()).isEqualTo("B3Rezq51U3P7uCjRokYIzw");

        Token token10 = token9.getNextToken();

        assertThat(token10).isNotNull();
        assertThat(token10.getType()).isEqualTo(TokenTypes.NULL);
        assertThat(token10.getLexeme()).isNull();

        assertThat(token10.getNextToken()).isNull();
    }
}