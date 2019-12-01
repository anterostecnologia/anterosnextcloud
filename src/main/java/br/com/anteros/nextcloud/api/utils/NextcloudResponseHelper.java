package br.com.anteros.nextcloud.api.utils;

import java.util.concurrent.CompletableFuture;

import br.com.anteros.nextcloud.api.exception.NextcloudApiException;
import br.com.anteros.nextcloud.api.exception.NextcloudOperationFailedException;

public class NextcloudResponseHelper
{
    public static final int NC_OK= 100; // Nextcloud OK message

    private NextcloudResponseHelper() {
    }

    public static <A extends XMLAnswer> A getAndCheckStatus(CompletableFuture<A> answer)
    {
        A xmlanswer = getAndWrapException(answer);
        if(isStatusCodeOkay(xmlanswer))
        {
            return xmlanswer;
        }
        throw new NextcloudOperationFailedException(xmlanswer.getStatusCode(), xmlanswer.getMessage());
    }

    public static <A extends XMLAnswer> boolean isStatusCodeOkay(CompletableFuture<A> answer)
    {
        return isStatusCodeOkay(getAndWrapException(answer));
    }

    public static  boolean isStatusCodeOkay(XMLAnswer answer)
    {
        return answer.getStatusCode() == NC_OK;
    }

    public static <A> A getAndWrapException(CompletableFuture<A> answer)
    {
        try {
            return answer.get();
        } catch (Exception e) {
            throw new NextcloudApiException(e);
        }
    }
}
