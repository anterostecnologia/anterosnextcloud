package br.com.anteros.nextcloud.api.exception;

public class NextCloudOperationFailedException extends NextCloudApiException {
    private static final long serialVersionUID = 6382478664807826933L;

    public NextCloudOperationFailedException(int statuscode, String message) {
        super(String.format("Nextcloud API call failed with statuscode %d and message \"%s\"", statuscode, message));
    }
}
