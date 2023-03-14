package cn.geterror.base.util;

import cn.geterror.base.exception.SecretNotFoundException;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

public abstract class BAUtil {

    public static final String DATE_FORMAT = "EEE dd MMM yyyy HH:mm:ss 'GMT'";

    public boolean auth(String uri, String method, String authorization,String date,long expiredMin){
        try {

            if (StringUtils.isEmpty(authorization) || StringUtils.isEmpty(date)) {
                return false;
            }
            if (!LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.US)).isBefore(LocalDateTime.now(ZoneId.of("GMT")).plusMinutes(expiredMin))) {
                return false;
            }
            String[] split = authorization.split(" ");
            if (split.length != 2) {
                return false;
            }
            String[] clientAndSign = split[1].split(":");
            if (clientAndSign.length != 2) {
                return false;
            }
            String clientId = clientAndSign[0];
            String secret = null;
            try {
                secret = getSecretByClientId(clientId);
            } catch (SecretNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            String genAuthorization = genAuthorization(uri, method, clientId, secret, date);
            return Objects.equals(authorization, genAuthorization);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getAuthorization(String uri, String method, String clientId, String secret) {
        String date = LocalDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.US));
        return genAuthorization(uri,method,clientId,secret,date);
    }
    public String genAuthorization(String uri, String method, String clientId, String secret,String date) {
        String stringToSign = method + " " + uri + "\n" + date;
        String signature = getSignature(stringToSign, secret);
        return "RSA " + clientId + ":" + signature;
    }

    public String getSignature(String data, String secret) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String result = Base64.getEncoder().encodeToString(rawHmac);
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate HMAC : " + e.getMessage());
        }
    }

    public abstract String getSecretByClientId(String clientId) throws SecretNotFoundException;
}
