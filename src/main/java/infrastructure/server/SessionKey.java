package infrastructure.server;

import java.util.UUID;

public class SessionKey {
    public static final String DELIMITER = "-";
    private static final int TEN_MINUTES = 10 * 60 * 1000;

    private final int idUser;
    private final String uuid;
    private final long timeStamp;

    public SessionKey(int idUser) {
        this.idUser = idUser;
        this.uuid = UUID.randomUUID().toString();
        this.timeStamp = System.currentTimeMillis();
    }

    public SessionKey(int idUser, String uuid, long timeStamp) {
        this.idUser = idUser;
        this.uuid = uuid;
        this.timeStamp = timeStamp;
    }

    public String build() {
        return String.join(DELIMITER, Integer.toString(idUser), uuid, Long.toString(timeStamp));
    }

    public boolean isValid() {
        long tenAgo = System.currentTimeMillis() - TEN_MINUTES;
        return timeStamp > tenAgo;
    }

    public int getIdUser() {
        return idUser;
    }
}
