package infrastructure.server.errors;

public class HttpError {
    private final String message;
    private final HttpErrorTypes httpErrorType;

    public HttpError(String message, HttpErrorTypes httpErrorType) {
        this.message = message;
        this.httpErrorType = httpErrorType;
    }

    @Override
    public String toString() {
        return "HttpError{" +
                "httpErrorType=" + httpErrorType.toString() +
                ", message='" + message + '\'' +
                '}';
    }
}
