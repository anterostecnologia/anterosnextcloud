package br.com.anteros.nextcloud.api.exception;

public class MoreThanOneShareFoundException extends NextCloudApiException {
    private static final long serialVersionUID = 5654006062204752474L;

    public MoreThanOneShareFoundException(int shareId) {
        super(String.format("More than one share found, not possible <%d>", shareId));
    }
}
