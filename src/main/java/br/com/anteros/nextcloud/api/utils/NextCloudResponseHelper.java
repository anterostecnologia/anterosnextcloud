package br.com.anteros.nextcloud.api.utils;

import br.com.anteros.nextcloud.api.exception.NextCloudOperationFailedException;

public class NextCloudResponseHelper
{
    public static final int NC_OK= 100; // Nextcloud OK message

    private NextCloudResponseHelper() {
    }

    public static <A extends XMLAnswer> A getAndCheckStatus(A answer)
    {
        if(isStatusCodeOkay(answer))
        {
            return answer;
        }
        throw new NextCloudOperationFailedException(answer.getStatusCode(), answer.getMessage());
    }



    public static  boolean isStatusCodeOkay(XMLAnswer answer)
    {
        return answer.getStatusCode() == NC_OK;
    }

}
