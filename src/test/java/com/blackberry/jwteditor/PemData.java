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

interface PemData {
    String PRIME256v1PrivatePKCS8 = """
            -----BEGIN PRIVATE KEY-----
            MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgR7xUBrtHikGBXsJk
            DekdUxWWC2YhYMKTDXILREc4/7uhRANCAAQrHJ52L8MTrBl0LWyEx5hXH0i9JeXX
            hFGn9nm1mMGh2lF7be9CQr3tHP6bmbI8kx7Sw+sXwb6CN31aIfVs4R/j
            -----END PRIVATE KEY-----""";

    String PRIME256v1PrivateSEC1 = """
            -----BEGIN EC PRIVATE KEY-----
            MHcCAQEEIEe8VAa7R4pBgV7CZA3pHVMVlgtmIWDCkw1yC0RHOP+7oAoGCCqGSM49
            AwEHoUQDQgAEKxyedi/DE6wZdC1shMeYVx9IvSXl14RRp/Z5tZjBodpRe23vQkK9
            7Rz+m5myPJMe0sPrF8G+gjd9WiH1bOEf4w==
            -----END EC PRIVATE KEY-----
            """;

    String PRIME256v1Public = """
            -----BEGIN PUBLIC KEY-----
            MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEKxyedi/DE6wZdC1shMeYVx9IvSXl
            14RRp/Z5tZjBodpRe23vQkK97Rz+m5myPJMe0sPrF8G+gjd9WiH1bOEf4w==
            -----END PUBLIC KEY-----
            """;

    String SECP256K1PrivateSEC1 = """
            -----BEGIN EC PRIVATE KEY-----
            MHQCAQEEIPN7f0wS3vreE9CjEEGOqGciNPoj9nqyu8TyBTgP9henoAcGBSuBBAAK
            oUQDQgAE8/GY7z+hgeR8UripTXvk8LCx0m18tYlrMmwkEYr0VyrPAuIDUwingBqA
            sH2vsEMgMZaXXSrsQYQNWo2SBIN0lQ==
            -----END EC PRIVATE KEY-----
            """;

    String SECP256K1PrivatePKCS8 = """
            -----BEGIN PRIVATE KEY-----
            MIGEAgEAMBAGByqGSM49AgEGBSuBBAAKBG0wawIBAQQg83t/TBLe+t4T0KMQQY6o
            ZyI0+iP2erK7xPIFOA/2F6ehRANCAATz8ZjvP6GB5HxSuKlNe+TwsLHSbXy1iWsy
            bCQRivRXKs8C4gNTCKeAGoCwfa+wQyAxlpddKuxBhA1ajZIEg3SV
            -----END PRIVATE KEY-----
            """;

    String SECP256K1Public = """
            -----BEGIN PUBLIC KEY-----
            MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAE8/GY7z+hgeR8UripTXvk8LCx0m18tYlr
            MmwkEYr0VyrPAuIDUwingBqAsH2vsEMgMZaXXSrsQYQNWo2SBIN0lQ==
            -----END PUBLIC KEY-----
            """;

    String SECP384R1PrivatePKCS8 = """
            -----BEGIN PRIVATE KEY-----
            MIG2AgEAMBAGByqGSM49AgEGBSuBBAAiBIGeMIGbAgEBBDAkpfmck1kNnG1L3MSL
            wALFQOxIBriwc499iT6687Qh8iXU+qFrY8RJyuzDVGe46L2hZANiAAT80M09vag9
            hHCeyJnmIXho1Yu628/AT1o0STyfSi4ZkB2tLWmrrmaIeKZist4OfvpK0BGhRFcb
            Y4cljodjnEbYjoeib/dlewwTYpxuUJCgXSdo2Bf7/gi8e70fKTb1awI=
            -----END PRIVATE KEY-----
            """;

    String SECP384R1PrivateSEC1 = """
            -----BEGIN EC PRIVATE KEY-----
            MIGkAgEBBDAkpfmck1kNnG1L3MSLwALFQOxIBriwc499iT6687Qh8iXU+qFrY8RJ
            yuzDVGe46L2gBwYFK4EEACKhZANiAAT80M09vag9hHCeyJnmIXho1Yu628/AT1o0
            STyfSi4ZkB2tLWmrrmaIeKZist4OfvpK0BGhRFcbY4cljodjnEbYjoeib/dlewwT
            YpxuUJCgXSdo2Bf7/gi8e70fKTb1awI=
            -----END EC PRIVATE KEY-----
            """;

    String SECP384R1Public = """
            -----BEGIN PUBLIC KEY-----
            MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE/NDNPb2oPYRwnsiZ5iF4aNWLutvPwE9a
            NEk8n0ouGZAdrS1pq65miHimYrLeDn76StARoURXG2OHJY6HY5xG2I6Hom/3ZXsM
            E2KcblCQoF0naNgX+/4IvHu9Hyk29WsC
            -----END PUBLIC KEY-----
            """;

    String SECP521R1PrivatePCKS8 = """
            -----BEGIN PRIVATE KEY-----
            MIHuAgEAMBAGByqGSM49AgEGBSuBBAAjBIHWMIHTAgEBBEIAtat9ezvGvb3BUxwR
            OPol+8amfRTtQcFvFWNnyOfNJF0GxcoZS9wuIIK3vaFKl4fOh7zd3pwZ1WErSxzA
            DS8TY0WhgYkDgYYABAHzfrOZwPomdJFAzKpUqLMz80/XjL2Brv1S26nJo1eh+m1X
            yV3jKGZhZFtEx5/UVW491LJk+i7V8tCZQu3cpSF10QDHJP6+lViLEvrSD+wgbeb1
            3fidiuuV2VsVOwBQoL3KGJQ2+kxd73FsqjI506m3gBUS0oRiWKtR/6UX/spFaglS
            Ew==
            -----END PRIVATE KEY-----
            """;

    String SECP521R1PrivateSEC1 = """
            -----BEGIN EC PRIVATE KEY-----
            MIHcAgEBBEIAtat9ezvGvb3BUxwROPol+8amfRTtQcFvFWNnyOfNJF0GxcoZS9wu
            IIK3vaFKl4fOh7zd3pwZ1WErSxzADS8TY0WgBwYFK4EEACOhgYkDgYYABAHzfrOZ
            wPomdJFAzKpUqLMz80/XjL2Brv1S26nJo1eh+m1XyV3jKGZhZFtEx5/UVW491LJk
            +i7V8tCZQu3cpSF10QDHJP6+lViLEvrSD+wgbeb13fidiuuV2VsVOwBQoL3KGJQ2
            +kxd73FsqjI506m3gBUS0oRiWKtR/6UX/spFaglSEw==
            -----END EC PRIVATE KEY-----
            """;

    String[] ECPrivate = {
            PRIME256v1PrivateSEC1,
            PRIME256v1PrivatePKCS8,
            SECP256K1PrivateSEC1,
            SECP256K1PrivatePKCS8,
            SECP384R1PrivateSEC1,
            SECP384R1PrivatePKCS8,
            SECP521R1PrivateSEC1,
            SECP521R1PrivatePCKS8
    };

    String SECP521R1Public = """
            -----BEGIN PUBLIC KEY-----
            MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQB836zmcD6JnSRQMyqVKizM/NP14y9
            ga79UtupyaNXofptV8ld4yhmYWRbRMef1FVuPdSyZPou1fLQmULt3KUhddEAxyT+
            vpVYixL60g/sIG3m9d34nYrrldlbFTsAUKC9yhiUNvpMXe9xbKoyOdOpt4AVEtKE
            YlirUf+lF/7KRWoJUhM=
            -----END PUBLIC KEY-----
            """;

    String[] ECPublic = {
            PRIME256v1Public,
            SECP256K1Public,
            SECP384R1Public,
            SECP521R1Public
    };

    String X25519Private = """
            -----BEGIN PRIVATE KEY-----
            MC4CAQAwBQYDK2VuBCIEIPgRPKqga++sfwqOIBjZ7HFEPbJ03wqa/xbbIZ27T6BM
            -----END PRIVATE KEY-----
            """;

    String X25519Public = """
            -----BEGIN PUBLIC KEY-----
            MCowBQYDK2VuAyEAwkCeJq/OWjW7jEXyLFnFOz/ZMfEcTssin8MPvl4PEyI=
            -----END PUBLIC KEY-----
            """;

    String RSA512Private = """
            -----BEGIN RSA PRIVATE KEY-----
            MIIBOgIBAAJBAPPJiFwXk7ZgPdPCHvGLxdliseVYhTB8LMCUHOEtBaugH3yMpo/G
            OSQg8gjCUrW/mC9LVfRRfp+ZnvyQw0bPYfkCAwEAAQJBAILvSASmSRiX4j0csr/q
            4U2bW46hl49t8h9QrZ4nLzd4kXYsx1Sq7rrCWKl6N15rvMU1FuxT7rSb0xBTlltc
            FrECIQD6gPFEZRGJl1+BMWfa/VKF9642ZC8HeoscuvjAxIqdvQIhAPki3VDy03Cp
            uv5wcxSUEz3kQJ4uiGynOycH3SB1jWLtAiBg7intbCpIElG7PPHR0/ntqrQ3ibaE
            708K8/IzLRnyBQIgG2ebYrEjUwxIln52GjhjmXZHKV9DAHiwyA1UZKKpp80CIAFs
            dw0Ervattq78H6ryQfZaec//c5AHdsFOqwei+6pN
            -----END RSA PRIVATE KEY-----
            """;

    String RSA512Public = """
            -----BEGIN PUBLIC KEY-----
            MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAPPJiFwXk7ZgPdPCHvGLxdliseVYhTB8
            LMCUHOEtBaugH3yMpo/GOSQg8gjCUrW/mC9LVfRRfp+ZnvyQw0bPYfkCAwEAAQ==
            -----END PUBLIC KEY-----
            """;

    String RSA1024Private = """
            -----BEGIN RSA PRIVATE KEY-----
            MIICWwIBAAKBgQC/Xgl6Ks3JBFA+68XabvonzWBRzx8ZSgtZptRsze7Og8aYrrn4
            XzRuoaXsu40TM22E7rt684YzfRLzwCLEs9fH6i+r/2ymkJOf/MXpRKUih2xDpH/x
            IhRLz/LD3zYiO+iJ14/VSHKE1V9EwANnoOYxHGj8xqVY0i3wZVEY+5skwQIDAQAB
            AoGANMpdNO1f7WcaN/FUmEhrC9XVD1xiyNgrP2UNTqKUkTW68F2rAcBJt4cyPZcT
            eMuEF9q/r1BEoimLs+A59ydhi9ND3CmYtmDqVgswHpKXjJf0I5zVuFQR2nvmgudy
            DAZDhS8xHpmBbLzaWq0X0RIXPcMAdBCVsaQVCKVW+tYBHVECQQDsnhPbH0CSGfg0
            bO/joR4Dl/IhKXLudjdwucxPATneXcr3+Dn5ySzM1xHKl+P030/scDNOJwibojoP
            km09dardAkEAzwsNqfmn9PpUHhIB2cymLGUKX4lW6Q/nkq4omaqSPUqxd8fIPhRn
            nqClYUGR49dqyqOn2qeLVaVYiIc0D+EJNQJAWb6nN1AGNKPm8TN0VTjx4lkKPCu1
            bN6nrni6FikX4tBeQGkEzEnSVSspk1bC3kiozrvLqdPCPw1ryiY2ir4LfQJAdtua
            mYTIJfv7hFPEGjvA/eV3ggb/DMbjozI1/pmMYIFbxi67xt/B6K/CoUWaMhl9ph0z
            hHQAqkdEvwQXIDKJLQJAWmLfQEnw5lDgdZZwuEBrrAhj7EZs5or7+Z+c44SQqcV8
            Sk+vHfigwzUxYFSl/XwKhPzX0Um0Vq/cvO2rEJfMUg==
            -----END RSA PRIVATE KEY-----
            """;

    String RSA1024Public = """
            -----BEGIN PUBLIC KEY-----
            MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/Xgl6Ks3JBFA+68XabvonzWBR
            zx8ZSgtZptRsze7Og8aYrrn4XzRuoaXsu40TM22E7rt684YzfRLzwCLEs9fH6i+r
            /2ymkJOf/MXpRKUih2xDpH/xIhRLz/LD3zYiO+iJ14/VSHKE1V9EwANnoOYxHGj8
            xqVY0i3wZVEY+5skwQIDAQAB
            -----END PUBLIC KEY-----
            """;

    String RSA2048Private = """
            -----BEGIN RSA PRIVATE KEY-----
            MIIEpAIBAAKCAQEAzSL+XrvMUim9vq+tj9j7TSKn6d3DI9saRTMJPj8S6mADSgZG
            4JE6ZOtFwzV9NHllcuxnIFxpo/iq0K3hVdQP1R6UvTSk9jxnufR2fiPN3Nc7WdAZ
            6bxOAkATootjoMG5BMIbU34tdRK5UBiN1ewznRn3ZV2kNUkw1RElCyyXWOteuZVP
            5R6f6aBXbvbzjg9ffJgFIvxqMTEfLhYRoSmuvnJmKJpRtasmtCqffsXYg/4stD+Q
            0+sOrZlwijbjA9/TYmR7B4QpNi3feYO4Sv6JWBXFs2Ei9+kNuHn2dYNh9MLuH1nT
            IMGbS7e2bIBmB5QNTyjg/JrFlVuKzw4JXVWO4wIDAQABAoIBAGjK0pUEbLkgV6z8
            w7YzSxU5efvPbKt5dy8YhkVFJV2TlLqRAE4iAmmhgq7ZOU/AICIYQApZCyJXmrMl
            0HHu9fOSPrt1FDwx2W82Qf3oPnbtgIU9+K7doiAmKaGkgU2TCPg0TI9XatNesEoJ
            cRNyKV+87xPaRVNvQ0HlEz5lFh+2msG64Rq2zm7eXUQCUx3oTkA9XWlVVkZLTgJz
            DommlLneDWisL9ffvWzumXURm1JPumE5oxlQtHF3l3C9CvwqSlrIMO9KQwkyG168
            KQWFXED2r4MHLGG3f88jd/JZWuYZbReu1Ogfh8lnciUZPZBVMC1WpFH/HC5NheSP
            UdtEkLECgYEA9ARlRO2Py0FLS01bOjuZFvsGoDrIWfGpQr7P5lbQ0WqqBMy0IZum
            D6awXe9CQc3qlDEaVPgVMyTn9I0nQo4kqQJZGq7cD6XP7oL4Vl3eGDyMSJ4Dl4Jc
            jAdKzE8b82vG2Ck6aXfPRXAN5xyCoaz3jBJ6f6GnuajsuaRpon5RpEsCgYEA1zXS
            QEg48XEYR2mSzKSsxYYLTHaVrsmhmN8PGyx0qickx47EcIgC9FEUsPF7SBMLGFkm
            eFf0zCfFx3FHHM6a8k1eB8uRaPKAi53PN5zN2FtfB/h2ttTZYaAH5CGy9nzmjya7
            jS2907S/gbIKXQ87s+wWaijE68gk+uqR4O3YsMkCgYEAp+WiXGPszZh2mzB1tcKu
            Gj7Ml1D669A4eyFZYQ6ZDY6Lv1OuGXXEM/6oWWm3uBlb7209xfsQcylQpFa/Da9K
            EDP6ZT6tUeLwCBd7kclyeFCo4Yo/xG7BCOQ3lg5UmA+vOKpdc5YpOojYOA3I4D9n
            8KwCN3FeO4O8phs3R8QMnrMCgYEAsthwbKlYYRVqZtZOf7COP+wgcZ/Ozp2TpJYi
            WKO+JDU5dHmpuBbf1aeX+WAE4qWov8g+lx4JOqwGybLStRbhX2F6NiIvKjb1KpGv
            xfiSfC2asOYDyfbi3CWAyU5UClFLzUUT8cREj64SxDZwNFvXcrNNAVX2KDeAu+C5
            8GHYhTkCgYAkj2PplSwJeV0h/VwWskCm+IHfPyok7gBAgMfmpDiR6ucChen02kPd
            qBpjBlZx7pSAo2V4m1tLHkR8UIje6pKLVpJJQAE57psYIbLL0rO1GV9nyTNz5oNm
            +JvNmGVK1CC0mitNctz6rxsURSSnaJF86k2CigfPlkVs8cW5DMIZZg==
            -----END RSA PRIVATE KEY-----
            """;

    String RSA2048Public = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzSL+XrvMUim9vq+tj9j7
            TSKn6d3DI9saRTMJPj8S6mADSgZG4JE6ZOtFwzV9NHllcuxnIFxpo/iq0K3hVdQP
            1R6UvTSk9jxnufR2fiPN3Nc7WdAZ6bxOAkATootjoMG5BMIbU34tdRK5UBiN1ewz
            nRn3ZV2kNUkw1RElCyyXWOteuZVP5R6f6aBXbvbzjg9ffJgFIvxqMTEfLhYRoSmu
            vnJmKJpRtasmtCqffsXYg/4stD+Q0+sOrZlwijbjA9/TYmR7B4QpNi3feYO4Sv6J
            WBXFs2Ei9+kNuHn2dYNh9MLuH1nTIMGbS7e2bIBmB5QNTyjg/JrFlVuKzw4JXVWO
            4wIDAQAB
            -----END PUBLIC KEY-----
            """;

    String RSA3072Private = """
            -----BEGIN RSA PRIVATE KEY-----
            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
            -----END RSA PRIVATE KEY-----
            """;

    String RSA3072Public = """
            -----BEGIN PUBLIC KEY-----
            MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAveNnew9oJECmfopx2wzF
            JaXKT4KgJ0vp8SmRl5wGVq5toU50BTIj1m1tJuMDS9NAI8vYt9IUaqZ/wdKudV2c
            nD2bTz32lbcur/SgDj7aWr/Mhzw/+qwS3N4FTacVT+Fqfj2D37Xe6SlyhO0X9qrV
            2ugQlwTmu4QubvIKkPeJcnoheYAff7dMl3mTV/kaXrNUxbF7jZqCW3szmZLcUWBs
            8hEjMtlnO9o3TG23cXusG73C9R1GpoFzY1P4eJbHMhF0aj/FgDienFmmUZAFWyXW
            9s68YD0NR2Q6kFj6UvxDwT2ZvqulDOM9+puFJHDzZ35lDQnRthu89KesO9CRvFiC
            yeLbAlZ9ez7EFBtSkXKCrRRso24JanahGK4f8H0omBusnew3w/FFSLCrPlBapAJB
            tqDOcupHwzz97Mzdr6cV6+vuXRwCGD/cQ7ge2/ZhtCPtrfxUBH8H2kRDlWfoY7YN
            3DWqyTqhM5RKj9t9Hc1ocqdQh0uAHIhJVZUzk32d/C6lAgMBAAE=
            -----END PUBLIC KEY-----
            """;

    String RSA4096Private = """
            -----BEGIN RSA PRIVATE KEY-----
            MIIJJwIBAAKCAgEAxA5+ROF30GELK7WyRUt5kYx2anaBDazJFDsVfuNNFcYfrwLZ
            8D25hoR2dyC50YZ3uHb+KsCfQxNJwKur6FbBgtVZJIPkEeJSCb14Oh4trrlIpb0d
            +RNntlgPK9UDkOAVTjHMI1LPB99osNJ1wTaxxB+gfqWSDijeMEBdhZ3/ZX+/x3ER
            qRXVBPs80CUVNF8sZ1hs+BVaML3KiJK9BtIa6bdHKWwxBV2o30uiG4TbJdpF13EH
            EHsoalNsN92RjAh6mi8e99176iOMKsmO7qB9dkAemkWO7CcAWrOi2uKzx8DR3KNg
            xnmCuS6yPCKnEoCH3R9c+TZz3k/JOTuwMKZ8Jd76dEXj0q1PzbAnFbIPFl/VHapA
            c8a2XXHIpMjxBiuZG7CuX5uzmsZZFU1jmcRfYPnJzffHnVLWNLsRbbyXUL4erHN/
            OhXL/TLRU+bKa4Q9KqbyWYn9o8Ap51jpFN8iA2fG65XVrqxlyjBzFSa3IYSRzmvU
            SXj4cPvD7y34ZsuHpWwVghM1IqeWKTMdqfTydnSaL57NIIiGhC81iI71BlF9+hoe
            vIviHBsSTk8NPknQy1tPazwkTHKWXQbk1VmVz30Hg6H120sZghpbJ1MK8GQ8btTA
            inVgcpER18s/97mrSKNz6YpaCTVLkGVgawEkP+0k/a2t/7GLu2GL/d5AFv0CAwEA
            AQKCAgA/aKCWDoX9syU1wdIib0KmPFOgIyuiDJwMGv/cxg8MV8OvirCJ7qyuQPE/
            nin7Bg56bqHNmfbobriPw/YZctWrRu1Jzg5ZamU2GT1lLpV/yRUh5YRoJkqIwvpF
            JuO8W30IiTqIwiOLXEozxh2nm9TqpX/29f3C7pzN5kNZD7ymSybvjjY0BYeb0NXe
            S+ALzCxEUGkN43oEAZ4bhbn1Fsb1h/1P/FnublhLv95yIAnwuCOWhkvOnMZ5fPmn
            J9YHAEcGzu6pI80L8OtIe1/q9aUeNaawJocPAZ4vt6GYQXH7zPUS8xQ9nNcWpW+n
            211FsqQ+dAJR57aZ1Bw0koVtdqr30vPEwrKqZX0Ds3vracTkYn9PNNNmxLN6x+lL
            OrharU2/gxJJOgFUzQ4Zfpkl43hgevQYXFrnEIC1/PzS7wCXknYt1s51Fxpg33m5
            MkfPwbgBsq0LO1L6faEWD2CRWprp1lw9Yp2LCROawWLl5GO3GdCAi9lB8lCax04P
            fYs/rO8QK4uj8NQOKaDjVA296mQY5QRgLfcWKNKdYN9/V/N0VYWCmsPv42fRYu5F
            qjgfscU2qECazersOX1EKWZ1cKSO5bdUHqaxKJcNrY7HcBFaHUq9AcTRUMqwwHqv
            GIc73o560ajHaSwjeqY6NWO0dy2UEAc+Hm0YgE0g052aANNZ0QKCAQEA+EXZCblh
            EeHKY6+FuGENbJxP3fXzBmniBiqkU1k5fbAikJ5G5EZuAWD4EqxgHvg90che07U/
            Vpxn2YUlHNQC0AKtEH2QzB9sCHo3LGbFEvSAA2PVp4vLq/X033zgNw+TMX8BfyDH
            lapG9+FRB4xUyclzUJueXY6oI4RPl7jt70zt94n8qM2l3+pZzjrj6iGqvO91S1Ip
            JVkUHPvficT+SKTwinaGPoGaxMulSCyMYVHMbACq16NV/uH+gSkFGj4hoD8Ge+zu
            MxNw7/nFjEbzXSMwYpKYUBNUeDnjWAWrnFBktd717JaGyp7YfpSDSiMy5Y7vfSMV
            B2xuRkaOcC6A8wKCAQEAyiiaxTslazjz+mQnmmgmt4++PYAiD3QOGZWOyIxI3e0R
            2b+ntqvAhiE+jjap6dULowv5ZsxSvGjfmu6AO207PsSAtBGa66rUAWO2mTxZRXf4
            VsHlIVGloohA7P/JduNgyRKtwxffOxQqURdnLhNsYQaNTBcebjxb542QRu++hcSV
            ODT0PERtAp882v5JfrB7/ad5/vHoEuxu0kA6WK79P9oM5Cuuv+CqLMfu7Uyx+1/t
            PDpHcI+G2Tx9XGVmKtWhqRyf3DrhzDifDC41ZH3QgFlTKlGopS4uDLltbizcsBhy
            M8hXc5f5LgYVVvOYKP5ObF8NNAfQRRWTum05VzOETwKCAQB079EuN1Z1Kbg+A6ok
            EfJUubsUfIee+bgv113WtCvT+2YMX8BcPKALnjS59t6/N4VGje28IZ5XmQuA9Klq
            TqGUf+4r2jidLB+iTo42J6GjL+4rqpYzeDDSKeLQcOzagkd+BKgbzMC0yaLrlgCo
            YXx+Hphl2yZYQp0HPYaPm9bV45IiCagridEbCjKKZEp9OKZGnzLL60LeCHrku+2p
            4lMyvB1f2juYrUrMbm7G89Sa8gAqzpWpQ5F5n9BT3ZI9rg4AgC75p7r566pCHY7i
            Z3iw4aYkh4+eB7y9c+aUyvbthSAqWX3TlYKfZPnrhN/hx3ptjUlX03JQMM4v44Or
            avvpAoIBAGVMw4FhifaYq2klFtNArWX+GY4nzul/djFLyCimYz0RtoguiZP3rJmJ
            vxb0PWB26UXiki7449yVkLbKHGeq7K0fOn8A82h0SZgIp09R9knhPAf9URvJFf7D
            2ATqh8GTXsRjH79LDwlDqZDI8WMdSIm2BsfCLfV/OW5meY2SRR4yy7NGlbw9bYKK
            2KvEEH/zX65TZcZi4a/Z2g5xMMuPpEeCtEMBwKdVpPCbnxY6AFW+U9qCOZol0aS5
            svfBt9hvBHsBZPmuZ8c1+DzaStb4VKp8GDh0gtWCtoCZykQTJuL23QMluOyrkqmF
            UtwnZG/mPtX/K1aPM8XqcgRRBxKzNfECggEAc4eqdnHsbegKBfXdYEjTCr2psttD
            IA+ysQw3C5Hk1HF4fTZ8qHaMj1WEdxdfjSB/kek3VlXUm/rooSeWeCpkOWhX3A4z
            EupumrpMZci+TqiZfTildJvpr2ls4y2kHUcVe9ATKq/T2ZJm3DokjjTCXmOpp0q4
            A+LkKfECY5mEb6pGzPYF8F5aFtv41QX8kz6dN0PUnqDgYX8+37q34xTNbqbO/Bsy
            DP9vxuiBHGOtl/cgFX+iVCuIEL2zYtZO+pIoNdAVIkpH0riO7eOyaIYqsEXMNbVt
            saeqYmS9IbkuZloqvCqTQqfFSfgXMdvgT+CixyqfgakwudAY+T+BgLPOxQ==
            -----END RSA PRIVATE KEY-----
            """;

    String[] RSAPrivate = {
            RSA512Private,
            RSA1024Private,
            RSA2048Private,
            RSA3072Private,
            RSA4096Private,
    };

    String RSA4096Public = """
            -----BEGIN PUBLIC KEY-----
            MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAxA5+ROF30GELK7WyRUt5
            kYx2anaBDazJFDsVfuNNFcYfrwLZ8D25hoR2dyC50YZ3uHb+KsCfQxNJwKur6FbB
            gtVZJIPkEeJSCb14Oh4trrlIpb0d+RNntlgPK9UDkOAVTjHMI1LPB99osNJ1wTax
            xB+gfqWSDijeMEBdhZ3/ZX+/x3ERqRXVBPs80CUVNF8sZ1hs+BVaML3KiJK9BtIa
            6bdHKWwxBV2o30uiG4TbJdpF13EHEHsoalNsN92RjAh6mi8e99176iOMKsmO7qB9
            dkAemkWO7CcAWrOi2uKzx8DR3KNgxnmCuS6yPCKnEoCH3R9c+TZz3k/JOTuwMKZ8
            Jd76dEXj0q1PzbAnFbIPFl/VHapAc8a2XXHIpMjxBiuZG7CuX5uzmsZZFU1jmcRf
            YPnJzffHnVLWNLsRbbyXUL4erHN/OhXL/TLRU+bKa4Q9KqbyWYn9o8Ap51jpFN8i
            A2fG65XVrqxlyjBzFSa3IYSRzmvUSXj4cPvD7y34ZsuHpWwVghM1IqeWKTMdqfTy
            dnSaL57NIIiGhC81iI71BlF9+hoevIviHBsSTk8NPknQy1tPazwkTHKWXQbk1VmV
            z30Hg6H120sZghpbJ1MK8GQ8btTAinVgcpER18s/97mrSKNz6YpaCTVLkGVgawEk
            P+0k/a2t/7GLu2GL/d5AFv0CAwEAAQ==
            -----END PUBLIC KEY-----
            """;

    String[] RSAPublic = {
            RSA512Public,
            RSA1024Public,
            RSA2048Public,
            RSA3072Public,
            RSA4096Public
    };

    String ED448Private = """
            -----BEGIN PRIVATE KEY-----
            MEcCAQAwBQYDK2VxBDsEOShHsf2xx7zi/GQOknZExKC8xcO3xdDrDFKyEY8TKdrZ
            Y84Y+XpGx9is96BqgpDYK5Avq5vUsgTmHA==
            -----END PRIVATE KEY-----
            """;

    String ED448Public = """
            -----BEGIN PUBLIC KEY-----
            MEMwBQYDK2VxAzoAy4WecbUYEP3O7++Bt0/7oDWhKHsP2uRYWLfD4TO0LpW8w4cM
            uj6Wl78QYoYr2Sc2poyjt4BeNiKA
            -----END PUBLIC KEY-----
            """;

    String ED25519Private = """
            -----BEGIN PRIVATE KEY-----
            MC4CAQAwBQYDK2VwBCIEIOTL3VV2R9BELspLlaOR3nBmJApK99kuBKx/Lzr2v1sa
            -----END PRIVATE KEY-----
            """;

    String ED25519Public = """
            -----BEGIN PUBLIC KEY-----
            MCowBQYDK2VwAyEAIOp8VZPD2Qx5Hgd+gsJUQBThqOnvxruxuDUoAMIn3Ow=
            -----END PUBLIC KEY-----
            """;

    String X448Private = """
            -----BEGIN PRIVATE KEY-----
            MEYCAQAwBQYDK2VvBDoEOOhbOc3z/5awW274AWzUTnvZh6IIBFRcYTWDtjgGDGbb
            yQbJyU200FnveqlcPHQK8cr+ahhDf6uf
            -----END PRIVATE KEY-----
            """;

    String[] OKPPrivate = {
            ED448Private,
            ED25519Private,
            X448Private,
            X25519Private
    };

    String X448Public = """
            -----BEGIN PUBLIC KEY-----
            MEIwBQYDK2VvAzkALZAQ06R+yE4SMldgYX/zXjpB0GzE+xl7B4S7hDenvbCf7XxC
            cKGtMu6M9wWC7+F9zO64rwmOux8=
            -----END PUBLIC KEY-----
            """;

    String[] OKPPublic = {
            ED448Public,
            ED25519Public,
            X448Public,
            X25519Public
    };
}
