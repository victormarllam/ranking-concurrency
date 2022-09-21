package infrastructure.server;

import static infrastructure.server.SessionKey.DELIMITER;

public class SessionKeyBuilder {
    private static final int INDEX_TO_SKIP_DELIMITER = 1;

    public SessionKey create(int idUser) {
        return new SessionKey(idUser);
    }

    public SessionKey create(String sessionKey) {
        var firstDelimiterIndex = sessionKey.indexOf(DELIMITER);
        var lastDelimiterIndex = sessionKey.lastIndexOf(DELIMITER);
        if (firstDelimiterIndex < 0 || lastDelimiterIndex < 0) {
            return null;
        }

        var idUser = Integer.parseInt(sessionKey.substring(0,
                firstDelimiterIndex));
        var timeStamp = Long.parseLong(sessionKey.substring(
                lastDelimiterIndex + INDEX_TO_SKIP_DELIMITER));
        var uuid = sessionKey.substring(firstDelimiterIndex + INDEX_TO_SKIP_DELIMITER,
                lastDelimiterIndex);

        return new SessionKey(idUser, uuid, timeStamp);
    }
}
