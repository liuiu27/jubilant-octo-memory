package com.cupdata.lakala.utils;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.voucher.GetVoucherReq;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * @author LinYong
 * @Description: lakala测试类
 * @create 2018-01-04 18:41
 */
public class TestUtils {
    public static void main(String[] args) throws Exception{
        String url = "http://localhost:8040/voucher/voucher/getVoucher";
        String URL2 = "http://cvpa.leagpoint.com/sipService/voucher/voucher/getVoucher";
        String org = "2018010200000001";
        GetVoucherReq getVoucherReq = new GetVoucherReq();
        getVoucherReq.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));
        getVoucherReq.setProductNo("20180105V123");
        getVoucherReq.setOrgOrderNo("1DHsfdAkll8a24KJ");
        getVoucherReq.setOrderDesc("拉卡拉券码测试");
        getVoucherReq.setMobileNo("15857128524");
        getVoucherReq.setExpire("20180331");
        String reqStr = JSONObject.toJSONString(getVoucherReq);
        System.out.print("请求参数json字符串" + reqStr);
        String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
        //String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDO7B1rs4jss/QeA+16BeJrXWntHfZFhFinwAWgxsbYKnKS4cUijWGgZF+qEVJfu2CU7SMqZANv0i4RFcjnXr0euYdKM5UP/FQr5unTUIM/cDt5kYJHkHt9v1TbLfYC8coHZRH3+moWzJSAYTiTsJ7dsWPOP+/xtZFy+48Smu0uQQIDAQAB";

        String orgPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
        //String orgPriKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK8c9UtWeruB1hTYN0rfPJbQvrTu3/XOgYkwnfTWEhuWZr3e5HQRg2lXihkR6VcUW3vSaQJUMdfwOgwphP0M1+9ZsbsBoJzkBW04e4ajnkEJAyrnfkrpwzMb3bymcOE89cif7RliLDXWOVhKIu+SZdiLsVUfI6R8mZDO6BWEjck/AgMBAAECgYAV3AKO3gPCJmoH/hmg4g8ZMIOt6GfSsm2fJ4+AQbzO7s5yg0F3b7w8yS23BXJgaW0mHtT28nWqZBWK8R/lytWlmM+G/uYNdu6//rKGsJB164YAk5OVA424BNUtd0Ph+VyUMI5bnBP+nIeM+WGmA9w+rO1uO5yzxidIcAnUFyvReQJBAPZjWfF2RnlDF0A4PcpaFT/BXf6kBr/wg7HK2lFUF9SXzQxBggS7Ih3/7AHYugykUV3ZKOJ8GFuZkBtHrC/dZ0sCQQC18crJGYW7JEA1NBCFLhBP8Lg9Hfa2HQxln8hgpfal9rusn8n7WoZzeH3u5oATUBvnlyyccP5JZW7W6mm81OldAkALelNdUUI1Me/qWPRf8dRdlPd4/lEmLeEkrit/cGhvyeaOdJrG96S+OwbWiy8XmawEsDIcYuWLltrEupEF5c2DAkAd2Y1rcMR/73KZR/Ft6CDE/Lk9Ta0sM5fVFGHLeW79y3z+1ThOBIwKZbpDd42LnZj2Zdbr053kbL/CgrLWSBgBAkEA5ZsJCcQtbOOHUmH0Hn7bkwIhEpiumHdDUOucMZ62tN0S3dYnXn0LbrkEa+AnyKbZbWATSzaZcfcOib8esSU1WA==";
        PublicKey sipPubKey = RSAUtils.getPublicKeyFromString(sipPubKeyStr);
        PrivateKey orgPriKey = RSAUtils.getPrivateKeyFromString(orgPriKeyStr);
        String data = RSAUtils.encrypt(reqStr, sipPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String sign = RSAUtils.sign(reqStr, orgPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        String params = "org=" + org + "&data=" + URLEncoder.encode(data, "utf-8") + "&sign=" + URLEncoder.encode(sign, "utf-8");
        String res = HttpUtil.doPost(url, params, "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.print("响应数据为" + res);

    /*  String text = "Qhtcb2ZfMGYJJRW3LulDRKlAlhyvd6dFFNUYJTNf0CB0UaLDHifrer0t759YTVYZUoa53dG4jh635xfJLrumI2HD8uPabCnhg0ptOLG7+h3hQ2lc1Itz1WpgcbPJeJY6COZlwACLfY55vdMrPrGjeG2n3UE2i22F0KVXUbbYYz969BIWMsBuufprOUcLhb5XIpC5YfUIL6PzPuYEbOa3UqNcV1VAIx6TEplpJ4i/CxyOzdGaWJJz5HBIUrWHEzuJNGacFJoC2UnYxrXaAMUWG73sbtyW6echLioTrkdYwZN1QZVR8JdpGYRQHenfKWcNLMqk2B0umpCvL5BgmTfHRQ==";
        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDU9lc4kI7+HR1fAArR3SzpnVfp9ihj7t6Ta5EeC70hgg7GK2tDZwvvwXwukD+RMGr3e5o9cOXRL/785CHcJYNbU4zmKweBpzEL4097UbI2Yracs6BCej4zH7dUTqVPi2/8EJwsKFpXILFMBCjKLNkhYFwmkqHbf710XC9BE4JLBQIDAQAB";
        String pri = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANT2VziQjv4dHV8ACtHdLOmdV+n2KGPu3pNrkR4LvSGCDsYra0NnC+/BfC6QP5Ewavd7mj1w5dEv/vzkIdwlg1tTjOYrB4GnMQvjT3tRsjZitpyzoEJ6PjMft1ROpU+Lb/wQnCwoWlcgsUwEKMos2SFgXCaSodt/vXRcL0ETgksFAgMBAAECgYBPUzkW8UXKItcraU0ecffGRx0VoWLXIoVWvUUPP4kh/5t4NnkcYLhiBJy4jXOYJcRaTficdT6thmbBFUsFgRWlkyPGpe61mGu+lRLYAzJlHWWR0XUY/tCMBLe1TPt8TXejCZ5EH+HihbZ5KXDyVICSkxa7BweVQDSpFw8+XDXzGwJBAPoqeYOVc1rUBzhDamteaX1IMa9bxGYH92TZP1OQUbVwfwZkmgSsqjfXpxHCqIHVvb0M2+fcq60lSuUwumyMvYcCQQDZ7cFNRUAdYO9fCgUqHDIeGbKa3XBIzunMb5TrIjvEPo/FopOETztUolkyKG3hvsSDjA0jT3FQty3TNlJ5rHYTAkBz3saHpupWMIzjh348Gu+7Ynv44AVYyOnSoTlOqDLgWinLuT8JWTxCPzWX6VSJRonqjZExlKlDulk1TNucOZfzAkA4bF5H604WTSqsqKN9q58uO+kKP5r6vte5noon9s0SmkAPI3CeKQVrfD0rx1vrvsCSbohYRBFKnKK4tK/V4NkRAkBFMbBnBbzxyjm7pzxwW2mQFrNFLtt2Ewadb73ZQzyh726d2kQa/iyLkytOw78qiV7l5zQr3LuPCKhYaskcyXGN";
        String data = URLDecoder.decode(text,"UTF-8");
        PublicKey publicKey = RSAUtils.getPublicKeyFromString(pub);
        PrivateKey privateKey = RSAUtils.getPrivateKeyFromString(pri);
        String encrypt = RSAUtils.encrypt("2333", publicKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String decrypt = RSAUtils.decrypt(text, privateKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        System.out.print(decrypt);*/

    }

}
