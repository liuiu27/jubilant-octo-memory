package com.cupdata.sip.common.lang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
public class RSAHelperTest {


    @Test
    public void testhuajia() {

        String huajiapub = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXFJZ9tY5kN3nAEobL3jyPQ1FW\n" +
                "Ynz6rOLMNB8DnGBMZdkqLKyZz2glxAZCEwnBVSgZcZ8jU0rq3DsM1IThovvfXk3Z\n" +
                "5IRkuTCf0JOB5nXA0AA9GkSo4PW4HkUtFoIfvj3G6GOSQWQpJAeEUdyA0U/tKWcX\n" +
                "Tca4d3StejFCpdLduwIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        String huajiapri="-----BEGIN PRIVATE KEY-----\n" +
                "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANcUln21jmQ3ecAS\n" +
                "hsvePI9DUVZifPqs4sw0HwOcYExl2SosrJnPaCXEBkITCcFVKBlxnyNTSurcOwzU\n" +
                "hOGi+99eTdnkhGS5MJ/Qk4HmdcDQAD0aRKjg9bgeRS0Wgh++PcboY5JBZCkkB4RR\n" +
                "3IDRT+0pZxdNxrh3dK16MUKl0t27AgMBAAECgYEA1s7bFqYzlmfJk0ltk0NcHIFR\n" +
                "RQOMaxgsU2Ji70pI60R1RifkIqIBFGbZETjOb2bLa84M8E0J79MQrtm59VjAUBp2\n" +
                "qZoNKZ05UgwTEX3NTcbh7gOqrJrpnfrQXl56Qc1cu4xL3s9LKW2Lov27vz5a69q1\n" +
                "vhRgSQx0yxTZp6d4C+ECQQD8jTT7Ziq6VvuWqH2S1dX2p0FFYgwO9+qPuzci6x3i\n" +
                "2Gkre/vklDUPIcFkpYWvtDVhUIVCfpdEvBOKb8oQmZfjAkEA2gRmjG3Fzz91kZP6\n" +
                "4bqj1GmySbtZ+P4SwEWK2nBTL9PgOawiNnGxKsGy1rEHP2MXbxh33MbeXpLOSuHs\n" +
                "eTiaSQJBANMgLNGZiFC8lJhPBKMy1irbiao6HACLanB7vs0XJqXT6p+qh1qAN5cN\n" +
                "zgGYihjnmtRenviGoWiZnfBWIeMcs6cCQQCJE7c+mE6FTksnqzlU8DrN9V8KCOg7\n" +
                "K9SyW4dAbNuA+ODEojwoqzdPwNYnMstxyWYUjfffMpLiN7/JSV+boEExAkAQH+O6\n" +
                "tpsa4pqgMF0HOAGoOQI+VO9ITpjlfdwbpcLeZiPxrAvAMm6dz533QEO5ojfQZlez\n" +
                "qAAPyacI8S18BIAU\n" +
                "-----END PRIVATE KEY-----";

        String testStr = "oQy9QPiFf3n+eegST5pAfOn4/p3VT3hWfwEhlLjuUD/fv8/GO7+RDMahQukgcq09P3wUhXR+NRPG988Hnyhh/+q9nbIXkitP0PhzWkEzY9MqSaEHdV1BHlucgAU3+GuB8oHxdwehRzX/FhIUGEXy/LEnfukDzQTVjQ175ir3HX1chTsbuaG0BGyh50zZk/18urut2JiX0J7iper/E4sZaZLW3iqnspsEElKlakDkr+268PcmjTZjRk8O/CZlPr0gwq2S43zWw+b9k3ZG45ZVkc+eW+hPMbyPA3iXH6isZdMWVXueMBvswucsnCWj9Poao4NJg8nBsi5/CeMKMjc8Rg==";


        PublicKey pemPublicKey = RSAHelper.getPemPublicKey(huajiapub);
        PrivateKey pemPrivateKey = RSAHelper.getPemPrivateKey(huajiapri);

        testStr = RSAHelper.encipher(testStr, pemPublicKey, 0);
        log.info(testStr);

        String decipher = RSAHelper.decipher(testStr, pemPrivateKey,0);

        log.info(decipher);

    }


    @Test
    public void testsip() throws Exception {

        String testStr = "oQy9QPiFf3n+eegST5pAfOn4/p3VT3hWfwEhlLjuUD/fv8/GO7+RDMahQukgcq09P3wUhXR+NRPG988Hnyhh/+q9nbIXkitP0PhzWkEzY9MqSaEHdV1BHlucgAU3+GuB8oHxdwehRzX/FhIUGEXy/LEnfukDzQTVjQ175ir3HX1chTsbuaG0BGyh50zZk/18urut2JiX0J7iper/E4sZaZLW3iqnspsEElKlakDkr+268PcmjTZjRk8O/CZlPr0gwq2S43zWw+b9k3ZG45ZVkc+eW+hPMbyPA3iXH6isZdMWVXueMBvswucsnCWj9Poao4NJg8nBsi5/CeMKMjc8Rg==";

        String str= "{\"a\":\"b\",\"c\":\"d\"}";
        String pri="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";

        String pub="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";


        PrivateKey privateKey = RSAHelper.getPrivateKey(pri);
        PublicKey publicKey = RSAHelper.getPublicKey(pub);

        String sign = RSAHelper.sign(str, privateKey);

        log.info(sign);

        boolean verify = RSAHelper.verify(str, publicKey, sign);

        log.info(verify+"");

    }


    @Test
    public void testhuajiares() throws Exception {

        String huajiapub = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXFJZ9tY5kN3nAEobL3jyPQ1FW\n" +
                "Ynz6rOLMNB8DnGBMZdkqLKyZz2glxAZCEwnBVSgZcZ8jU0rq3DsM1IThovvfXk3Z\n" +
                "5IRkuTCf0JOB5nXA0AA9GkSo4PW4HkUtFoIfvj3G6GOSQWQpJAeEUdyA0U/tKWcX\n" +
                "Tca4d3StejFCpdLduwIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        PublicKey pemPublicKey = RSAHelper.getPemPublicKey(huajiapub);

        String prikey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";

        PrivateKey privateKey = RSAHelper.getPrivateKey(prikey);

        String data ="KEO0kTTtrH1spXIwMarUt3QBnVN196tEwl8KiCYaFC8S5XWqS82fhn3GmNbJony+sdAl3CTzODmm/v/eq4RbvvLxVrcGFf6+6F/svTig2/P1hXW9hEreLiGXchPiNmmqzWsD0feuXdGTcxoPK2INvtMcSU4kMEu4j3gN79uqKo4=";

        data= RSAHelper.decipher(data,privateKey,0);
        log.info(data);

        String sign="D2aGs8zVo2rWA7eo1PiwLfQINGKMEB0tzGUhlxzsHMABXuQljTreGtbevp3GGcOxW4fspOTdjvavmD4XNS/L64jjUS9UcgvkoHxps0Gfvjr2mUPb7Q+ePqWgyVTJVU5xr3i2CII+kW9LqCpQ8gAkff5cUO/mLJqqpG2e2XxjMYg=";

        boolean verify = RSAHelper.verify(data, pemPublicKey, sign);

        log.info(verify+"");
    }

}