package br.com.anteros.nextcloud.api.utils;

import java.util.concurrent.CompletableFuture;

import br.com.anteros.nextcloud.api.exception.NextCloudApiException;
import br.com.anteros.nextcloud.api.exception.NextCloudOperationFailedException;

public class NextCloudResponseHelper
{
    public static final int NC_OK= 100; // Nextcloud OK message

    private NextCloudResponseHelper() {
    }

    public static <A extends XMLAnswer> A getAndCheckStatus(CompletableFuture<A> answer)
    {
        A xmlanswer = getAndWrapException(answer);
        if(isStatusCodeOkay(xmlanswer))
        {
            return xmlanswer;
        }
        throw new NextCloudOperationFailedException(xmlanswer.getStatusCode(), xmlanswer.getMessage());
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
            throw new NextCloudApiException(e);
        }
    }
}