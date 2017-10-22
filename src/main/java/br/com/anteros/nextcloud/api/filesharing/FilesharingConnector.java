/*
 * Copyright (C) 2017 a.schild
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.anteros.nextcloud.api.filesharing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import br.com.anteros.nextcloud.api.ServerConfig;
import br.com.anteros.nextcloud.api.exception.MoreThanOneShareFoundException;
import br.com.anteros.nextcloud.api.provisioning.ShareData;
import br.com.anteros.nextcloud.api.utils.ConnectorCommon;
import br.com.anteros.nextcloud.api.utils.NextCloudResponseHelper;
import br.com.anteros.nextcloud.api.utils.XMLAnswer;
import br.com.anteros.nextcloud.api.utils.XMLAnswerParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author a.schild
 *
 * https://docs.nextcloud.com/server/11/developer_manual/core/ocs-share-api.html
 *
 */
public class FilesharingConnector
{
    private final static String ROOT_PART= "ocs/v1.php/apps/files_sharing/api/v1/";
    private final static String SHARES_PART= ROOT_PART+"shares";

    private final ConnectorCommon connectorCommon;

    public FilesharingConnector(ServerConfig serverConfig) {
        this.connectorCommon = new ConnectorCommon(serverConfig);
    }

    /**
     * Get all shares of this user
     *
     * @return all shares
     */
    public List<Share> getShares()
    {
        return getShares(null, false, false);
    }

    /**
     * Gets all shares from a given file/folder
     *
     * @param path      path to file/folder
     * @param reShares  returns not only the shares from the current user but all shares from the given file
     * @param subShares returns all shares within a folder, given that path defines a folder
     * @return matching shares
     */
    public List<Share> getShares(String path, boolean reShares, boolean subShares)
    {
    	List<NameValuePair> queryParams= new LinkedList<>();
        if (path != null)
        {
            queryParams.add(new BasicNameValuePair("path", path));
        }
        if (reShares)
        {
            queryParams.add(new BasicNameValuePair("reshares", "true"));
        }
        if (subShares)
        {
            queryParams.add(new BasicNameValuePair("subfiles", "true"));
        }
        return connectorCommon.executeGet(SHARES_PART, queryParams, XMLAnswerParser.getInstance(SharesXMLAnswer.class)).getShares();
    }


    /**
     * Get share info for a single share
     *
     * @param shareId      id of share (Not path of share)
     * @return the share if it has been found, otherwise null
     */
    public Share getShareInfo(int shareId)
    {
        SharesXMLAnswer xa= NextCloudResponseHelper.getAndCheckStatus(connectorCommon.executeGet(SHARES_PART+"/"+Integer.toString(shareId), null, XMLAnswerParser.getInstance(SharesXMLAnswer.class)));
        if (xa.getShares() == null)
        {
            return null;
        }
        else if (xa.getShares().size() == 1)
        {
            return xa.getShares().get(0);
        }
        throw new MoreThanOneShareFoundException(shareId);
    }


    /**
     * Shares the specified path with the provided parameters
     *
     * @param path                  path to the file/folder which should be shared
     * @param shareType             0 = user; 1 = group; 3 = public link; 6 = federated cloud share
     * @param shareWithUserOrGroupId user / group id with which the file should be shared
     * @param publicUpload          allow public upload to a public shared folder (true/false)
     * @param password              password to protect public link Share with
     * @param permissions           1 = read; 2 = update; 4 = create; 8 = delete; 16 = share; 31 = all (default: 31, for public shares: 1)
     * @return created share on success
     */
    public Share doShare(
            String path,
            ShareType shareType,
            String shareWithUserOrGroupId,
            Boolean publicUpload,
            String password,
            SharePermissions permissions)
    {
        
        
        List<NameValuePair> postParams= new LinkedList<>();
        postParams.add(new BasicNameValuePair("path", path));
        postParams.add(new BasicNameValuePair("shareType", Integer.toString(shareType.getIntValue())));
        postParams.add(new BasicNameValuePair("shareWith", shareWithUserOrGroupId));
        if (publicUpload != null)
        {
            postParams.add(new BasicNameValuePair("publicUpload", publicUpload ? "true" : "false"));
        }
        if (password != null)
        {
            postParams.add(new BasicNameValuePair("password", password));
        }
        if (permissions != null)
        {
            postParams.add(new BasicNameValuePair("permissions", Integer.toString(permissions.getCurrentPermission())));
        }

        return connectorCommon.executePost(SHARES_PART, postParams, XMLAnswerParser.getInstance(SingleShareXMLAnswer.class)).getShare();
    }

   

    /**
     * Changes a single attribute of a share
     *
     * @param shareId unique identifier of the share
     * @param key the attribute to change
     * @param value the value to set
     * @return true if the operation succeeded
     */
    public boolean editShare(int shareId, ShareData key, String value)
    {
    	List<NameValuePair> queryParams= Collections.singletonList(new BasicNameValuePair(key.parameterName, value));
        return NextCloudResponseHelper.isStatusCodeOkay(connectorCommon.executePut(SHARES_PART, Integer.toString(shareId), queryParams, XMLAnswerParser.getInstance(XMLAnswer.class)));
    }


    /**
     * Changes multiple attributes of a share at once
     *
     * @param shareId unique identifier of the share
     * @param values a Map containing the attributes to set
     * @return true if the operation succeeded
     */
    public boolean editShare(int shareId, Map<ShareData,String> values)
    {
    	List<NameValuePair> queryParams = values.entrySet().stream()
                .map(e -> new BasicNameValuePair(e.getKey().parameterName, e.getValue())).collect(Collectors.toList());
        return NextCloudResponseHelper.isStatusCodeOkay(connectorCommon.executePut(SHARES_PART, Integer.toString(shareId), queryParams, XMLAnswerParser.getInstance(XMLAnswer.class)));
    }

    

    /**
     * Deletes a share
     *
     * @param shareId unique identifier of the share
     * @return true if the operation succeeded
     */
    public boolean deleteShare(int shareId)
    {
        return NextCloudResponseHelper.isStatusCodeOkay(connectorCommon.executeDelete(SHARES_PART, Integer.toString(shareId), null, XMLAnswerParser.getInstance(XMLAnswer.class)));
    }

}
