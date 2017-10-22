package br.com.anteros.nextcloud.api.exception;

public class NextCloudApiException extends RuntimeException {
    private static final long serialVersionUID = 8088239559973590632L;

    public NextCloudApiException(Throwable cause) {
        super(cause);
    }

    public NextCloudApiException(String message) {
        super(message);
    }
}
