/*
Author : Dolph Flynn

Copyright 2022 Dolph Flynn

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

public interface JWKData {
    String RSA2048PrivateDEN = "{\"kty\": \"RSA\", \"d\": \"NWk0zSEQGWDeAuuVa_5N_oNr5K2whMhvbCPFHjUPFpfcA3FMAPMHT17u9GMBlum2pgYrvRHsibfn0i5pRxpFnSEvqnzYubWlaVSiRL1xTLjn6UixpxwfJkPY8C1dBa9aPZvSO_R-MfHoDudVecckMrWWdScw_9nir_Fc2l8QCp4R_OYGu6Uj58YDYCrToJ817utPltDRaTBg_7FD-ppX9-qdoBB1RW5kzJqGksBt4gk6zBeagvryvCfxVtrkYVv4cVJ8K1i2waMXRtAl-cyNTJh4M6zcsBM8pO6DirxBQdmzYsz_20w3vPdPs3yH53XxbAMXTEwlZz3lrJaoFAi6eQ\", \"e\": \"AQAB\", \"kid\": \"4c756ccb-0080-42d3-bc58-7692fd2ab3c5\", \"n\": \"mt04cN9oFDdmSDWkuxnsxyA6FJvEi1Ha1bYOUNLnS1Ecoqe7ErvDoycctBrAZ2ul408zFSoSRqf7IX5P3W2b8KDh7QvToZ0o9E0DBFWlqo88Iariif4ZpXL7r2Kse67zZ7ZVmU4A3aB_8mbpZQutQEWwuhTANnzc94pDv6ryaqcAc4rPgsnE1IY2gqznWBL2ZGa2iCzphTeB4yxSr3AfLY8HOUSku4Vvr6l20t-_ZbaSJFDqvjo699w9LkqzzxNTtdOmenxRc8eEKg6_Cw_Do_Q-SgblDXMWI057u-pSXbYjbknk20HczULLxgS7nUQJhs8uN8tX-xcQuoZexU0sGw\"}";

    String RSA2048PrivateAll = "{\"p\": \"4lzEStkwPU75h1uGr8Tn2hYAouFLhR8nnN7XV66UM6wQkJwA6pBE1FDb-xRvmZyrz9oGeryYrs6cE34tfYVkZYjyO95sE4ABi44986RmioHMg1-P5Ip8iq7_CMagq1WGgyi5Bx-hRtOxhnGZT3W2Juz34VWelDRsRNnbV0-ZV5c\", \"kty\": \"RSA\", \"q\": \"ryP4Uo9wMeA_nvGHsLwOjTaFWm0aLYZDm06U7i8Tl3y1NO1zgez2qyuGyPA0RYPrKVdYU9275RRv7hT1I2SxEr5RT6IXFOoXZOtrtocmLll2W2cVVZqfsHoVtD_TcZ4haMqp8hUOVW0i_nc4uSMPQFLSVgfLaRLXi80jcFpLwB0\", \"d\": \"NWk0zSEQGWDeAuuVa_5N_oNr5K2whMhvbCPFHjUPFpfcA3FMAPMHT17u9GMBlum2pgYrvRHsibfn0i5pRxpFnSEvqnzYubWlaVSiRL1xTLjn6UixpxwfJkPY8C1dBa9aPZvSO_R-MfHoDudVecckMrWWdScw_9nir_Fc2l8QCp4R_OYGu6Uj58YDYCrToJ817utPltDRaTBg_7FD-ppX9-qdoBB1RW5kzJqGksBt4gk6zBeagvryvCfxVtrkYVv4cVJ8K1i2waMXRtAl-cyNTJh4M6zcsBM8pO6DirxBQdmzYsz_20w3vPdPs3yH53XxbAMXTEwlZz3lrJaoFAi6eQ\", \"e\": \"AQAB\", \"kid\": \"4c756ccb-0080-42d3-bc58-7692fd2ab3c5\", \"qi\": \"hWYpdilJqiGG7lMEdEPEjPuwycq21YXexob2WTbCxJcF3Yy0vC_zSzxPB1qExA18-Zz9lUwnI9F21gBxvCactWZJ9tvwwawfTbX_9OqOl_ebORFvWKEsoFfpVCQXhwHCTB-kf9ShmDzxBnEuTcok5EXZi6xrofM3Y0PZotSgSoo\",\"dp\": \"h4Xsy7cup3YR9RU6FR_5g9tqdBoYwdG-QLA2EzvlZO5eWIXeEpFfdBIZMkCw9DIVt3KcMH2bmAUA8ra3e5ASZKvSA0AOSrp3slruAmHqNoCxtfHPz4-OMuXEsTdiWFHzH7GQ3Y_1WddCUPDQTf92l-WGHvXI5IhiTfJ03Ng-QW8\", \"dq\": \"m7RG2F9dR3ouFYh1MdJ-vVxzQektFLwA7tn13atMp6jfEKbpweCBi7uuoIWscwDM2Hwmsqi2mvqIaAmJxmWGZzt73mgkTRuwoLALmsKcVyiB6NDETs6gmaxwD0ePG7uRyDAk1muRyrC0I7aqXy2kKXN4O7PCSy_NISTHFOOx5KE\", \"n\": \"mt04cN9oFDdmSDWkuxnsxyA6FJvEi1Ha1bYOUNLnS1Ecoqe7ErvDoycctBrAZ2ul408zFSoSRqf7IX5P3W2b8KDh7QvToZ0o9E0DBFWlqo88Iariif4ZpXL7r2Kse67zZ7ZVmU4A3aB_8mbpZQutQEWwuhTANnzc94pDv6ryaqcAc4rPgsnE1IY2gqznWBL2ZGa2iCzphTeB4yxSr3AfLY8HOUSku4Vvr6l20t-_ZbaSJFDqvjo699w9LkqzzxNTtdOmenxRc8eEKg6_Cw_Do_Q-SgblDXMWI057u-pSXbYjbknk20HczULLxgS7nUQJhs8uN8tX-xcQuoZexU0sGw\"}";

    String[] RSAPrivate = {
            RSA2048PrivateAll,
            //RSA2048PrivateDEN
    };

    String RSA2048Public = "{\"kty\": \"RSA\", \"e\": \"AQAB\", \"kid\": \"4c756ccb-0080-42d3-bc58-7692fd2ab3c5\", \"n\": \"mt04cN9oFDdmSDWkuxnsxyA6FJvEi1Ha1bYOUNLnS1Ecoqe7ErvDoycctBrAZ2ul408zFSoSRqf7IX5P3W2b8KDh7QvToZ0o9E0DBFWlqo88Iariif4ZpXL7r2Kse67zZ7ZVmU4A3aB_8mbpZQutQEWwuhTANnzc94pDv6ryaqcAc4rPgsnE1IY2gqznWBL2ZGa2iCzphTeB4yxSr3AfLY8HOUSku4Vvr6l20t-_ZbaSJFDqvjo699w9LkqzzxNTtdOmenxRc8eEKg6_Cw_Do_Q-SgblDXMWI057u-pSXbYjbknk20HczULLxgS7nUQJhs8uN8tX-xcQuoZexU0sGw\"}";

    String[] RSAPublic = {
            RSA2048Public
    };

    String P256Private = "{\"kty\": \"EC\", \"d\": \"UBcNQgxNE1d7JdTe6G8ez5TtM4c6qSHmgKtiIZHGq18\", \"crv\": \"P-256\", \"kid\": \"c572f6eb-a48b-4b2b-bfe2-dd6722e6d0c6\", \"x\": \"4OO4UgLJ6L0XdENG6T7R6VRj31Zq9ecRwb8eKuensns\", \"y\": \"eKBa5Q-_ClY848UpH90G94ve54m3JSx6dLyJIUArwsA\"}";

    String P256Public = "{\"kty\": \"EC\", \"crv\": \"P-256\", \"kid\": \"c572f6eb-a48b-4b2b-bfe2-dd6722e6d0c6\", \"x\": \"4OO4UgLJ6L0XdENG6T7R6VRj31Zq9ecRwb8eKuensns\", \"y\": \"eKBa5Q-_ClY848UpH90G94ve54m3JSx6dLyJIUArwsA\"}";

    String SECP256K1Private = "{\"kty\": \"EC\", \"d\": \"CBT5lhTynh7L8Z1Uh6wXwXsxx-ho8-aggmXq5Qw6iAM\", \"crv\": \"secp256k1\", \"kid\": \"a6d1ba27-45be-4ec2-9324-58ffe764b898\", \"x\": \"ECR1kI36fUH5Xt2RfSCS0XW1Qhc9pD2hnn7HvHvLfr0\", \"y\": \"z6OUuRROlya9PyOieGzkPcajR0og_95i8A9NdrxVk4A\"}";

    String SECP256K1Public = "{\"kty\": \"EC\", \"crv\": \"secp256k1\", \"kid\": \"a6d1ba27-45be-4ec2-9324-58ffe764b898\", \"x\": \"ECR1kI36fUH5Xt2RfSCS0XW1Qhc9pD2hnn7HvHvLfr0\", \"y\": \"z6OUuRROlya9PyOieGzkPcajR0og_95i8A9NdrxVk4A\"}";

    String P384Private = "{\"kty\": \"EC\", \"d\": \"EI2fP6ghM00yeXhkgMp7Lk8bUfdw2hKM5qLZtlBE754Lmrw2a0eKKCAwVYnbRrGC\", \"crv\": \"P-384\", \"kid\": \"78f3da69-7aa9-4ef5-9e25-3b07ea34e8cc\", \"x\": \"jTbrGKUR28MzNoQjE1PE-K-iD0pDRwkvnGtbrQxIKN0oXU7HwpgcSBezxmCRsSqq\", \"y\": \"c6PlDhTHnpsQU1jakL35blrpsWPlQhAA-nMOdyzlIwYS899NC-60xFBLGu3YHQsf\"}";

    String P384Public = "{\"kty\": \"EC\", \"crv\": \"P-384\", \"kid\": \"78f3da69-7aa9-4ef5-9e25-3b07ea34e8cc\", \"x\": \"jTbrGKUR28MzNoQjE1PE-K-iD0pDRwkvnGtbrQxIKN0oXU7HwpgcSBezxmCRsSqq\", \"y\": \"c6PlDhTHnpsQU1jakL35blrpsWPlQhAA-nMOdyzlIwYS899NC-60xFBLGu3YHQsf\"}";

    String P521Private = "{\"kty\": \"EC\", \"d\": \"AEAmcxfuAJoT38ig1QU2APZm0QDtfJMyB_lvgb9lfsdvAMI5peD2BfPz2U_S1-h4R1w-EINd1tdEsz8PsqbJ95tx\", \"crv\": \"P-521\", \"kid\": \"635cf969-e377-4034-895f-1a6022a0e93d\", \"x\": \"Afp3fCwa4qZtG0DD1_CZJMrlOcuUmPH2MWd1b5gL8xSDsZIDURE_4KB3b0gihHN6MJavyRb8BwofVyPds4aa2e0A\", \"y\": \"ATTwJtHUTfIFVDpyMAtZeQ2xdXFohr_ETegtn1WGf9gmkrHvCZbbTYyU9el9IGA6A8FnSHAAN3imkOKkDIbEcRwn\"}";

    String[] ECPrivate = {
            P256Private,
            SECP256K1Private,
            P384Private,
            P521Private
    };

    String P521Public = "{\"kty\": \"EC\", \"crv\": \"P-521\", \"kid\": \"635cf969-e377-4034-895f-1a6022a0e93d\", \"x\": \"Afp3fCwa4qZtG0DD1_CZJMrlOcuUmPH2MWd1b5gL8xSDsZIDURE_4KB3b0gihHN6MJavyRb8BwofVyPds4aa2e0A\", \"y\": \"ATTwJtHUTfIFVDpyMAtZeQ2xdXFohr_ETegtn1WGf9gmkrHvCZbbTYyU9el9IGA6A8FnSHAAN3imkOKkDIbEcRwn\"}";

    String[] ECPublic = {
            P256Public,
            SECP256K1Public,
            P384Public,
            P521Public
    };

    String X25519Private = "{\"kty\": \"OKP\", \"d\": \"t75j8DATl-aDTKOlxMJsBt-P1Q4Lyy4SRQXyQ2yEgo4\", \"crv\": \"X25519\", \"kid\": \"4778a36f-1cf4-45c2-a138-9fa1151346e9\", \"x\": \"r8UOGY16orlHR9BZcwf86-mx35XeAOdDZ-_ZA2AoKT0\"}";

    String X25519Public = "{\"kty\": \"OKP\", \"crv\": \"X25519\", \"kid\": \"4778a36f-1cf4-45c2-a138-9fa1151346e9\", \"x\": \"r8UOGY16orlHR9BZcwf86-mx35XeAOdDZ-_ZA2AoKT0\"}";

    String Ed25519Private = "{\"kty\": \"OKP\", \"d\": \"_1iBm2slEqE8ekqBTX_Hx1-3qU_zy50H4rppWJ2e6dg\", \"crv\": \"Ed25519\", \"kid\": \"484a783c-0480-4198-97bc-b7086e44bcbd\", \"x\": \"aLGXCU3U-zrgk0o6IA0kH01nmJitziqzNpdZWqVr6GQ\"}";

    String[] OKPPrivate = {
            X25519Private,
            Ed25519Private
    };

    String Ed25519Public = "{\"kty\": \"OKP\", \"crv\": \"Ed25519\", \"kid\": \"484a783c-0480-4198-97bc-b7086e44bcbd\", \"x\": \"aLGXCU3U-zrgk0o6IA0kH01nmJitziqzNpdZWqVr6GQ\"}";

    String[] OKPPublic = {
            X25519Public,
            Ed25519Public
    };
}
