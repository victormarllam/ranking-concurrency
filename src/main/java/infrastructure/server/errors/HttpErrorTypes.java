package infrastructure.server.errors;

public enum HttpErrorTypes {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    METHOD_NOT_ALLOWED(405);

    private HttpErrorTypes(int code) {
        this.code = code;
    }
    private final int code;

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.join(" - ", Integer.toString(code), this.name());
    }
}
