package infrastructure.server;

import java.util.Base64;

public class SessionKeyProvider {
    private static final SessionKeyBuilder sessionKeyBuilder = new SessionKeyBuilder();

    public static String createAndGetSessionKey(int idUser) {
        return Base64.getEncoder().withoutPadding().encodeToString(sessionKeyBuilder.create(idUser)
                .build()
                .getBytes());
    }

    public static SessionKey getIfValid(String base64SessionKey) {
        var sessionKey = sessionKeyBuilder.create(new String(Base64.getDecoder().decode(base64SessionKey)));
        return sessionKey != null && sessionKey.isValid() ? sessionKey : null;
    }

    private static String decodeSessionKey(String sessionKey) {
        return new String(Base64.getDecoder().decode(sessionKey));
    }
}
