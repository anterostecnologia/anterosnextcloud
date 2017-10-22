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
package br.com.anteros.nextcloud.api;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import br.com.anteros.nextcloud.api.filesharing.FilesharingConnector;
import br.com.anteros.nextcloud.api.filesharing.Share;
import br.com.anteros.nextcloud.api.filesharing.SharePermissions;
import br.com.anteros.nextcloud.api.filesharing.ShareType;
import br.com.anteros.nextcloud.api.filesharing.SharesXMLAnswer;
import br.com.anteros.nextcloud.api.provisioning.GroupsXMLAnswer;
import br.com.anteros.nextcloud.api.provisioning.ProvisionConnector;
import br.com.anteros.nextcloud.api.provisioning.ShareData;
import br.com.anteros.nextcloud.api.provisioning.User;
import br.com.anteros.nextcloud.api.provisioning.UserData;
import br.com.anteros.nextcloud.api.provisioning.UserXMLAnswer;
import br.com.anteros.nextcloud.api.provisioning.UsersXMLAnswer;
import br.com.anteros.nextcloud.api.utils.ListXMLAnswer;
import br.com.anteros.nextcloud.api.utils.XMLAnswer;
import br.com.anteros.nextcloud.api.webdav.Files;
import br.com.anteros.nextcloud.api.webdav.Folders;

public class AnterosNextCloudConnector {

    private final ServerConfig    _serverConfig;
    private final ProvisionConnector pc;
    private final FilesharingConnector fc;
    private final Folders fd;
    private final Files fl;

    public AnterosNextCloudConnector(String serverName, boolean useHTTPS, int port, String userName, String password)
    {
        _serverConfig= new ServerConfig(serverName, useHTTPS, port, userName, password);
        pc= new ProvisionConnector(_serverConfig);
        fc= new FilesharingConnector(_serverConfig);
        fd= new Folders(_serverConfig);
        fl= new Files(_serverConfig);
    }

    /**
     * Creates a user
     *
     * @param userId unique identifier of the user
     * @param password password needs to meet nextcloud criteria or operation will fail
     * @return true if the operation succeeded
     */
    public boolean createUser(String userId, String password)
    {
        return pc.createUser(userId, password);
    }

    /**
     * Deletes a user
     *
     * @param userId unique identifier of the user
     * @return true if the operation succeeded
     */
    public boolean deleteUser(String userId)
    {
        return pc.deleteUser(userId);
    }

   

    /**
     * Enables a user
     *
     * @param userId unique identifier of the user
     * @return true if the operation succeeded
     */
    public boolean enableUser(String userId)
    {
        return pc.enableUser(userId);
    }

  
    /**
     * Disables a user
     *
     * @param userId unique identifier of the user
     * @return true if the operation succeeded
     */
    public boolean disableUser(String userId)
    {
        return pc.disableUser(userId);
    }

       /**
     * Gets all groups of a user
     *
     * @param userId unique identifier of the user
     * @return matched group IDs
     */
    public List<String> getGroupsOfUser(String userId) {
        return pc.getGroupsOfUser(userId);
    }

    

    /**
     * Adds a user to a group
     *
     * @param userId unique identifier of the user
     * @param groupId unique identifier of the group
     * @return true if the operation succeeded
     */
    public boolean addUserToGroup(String userId, String groupId)
    {
        return pc.addUserToGroup(userId, groupId);
    }


    /**
     * Removes a user from a group
     *
     * @param userId unique identifier of the user
     * @param groupId unique identifier of the group
     * @return true if the operation succeeded
     */
    public boolean removeUserFromGroup(String userId, String groupId)
    {
        return pc.removeUserFromGroup(userId, groupId);
    }

    /**
     * Gets all groups this user is a subadministrator of
     *
     * @param userId unique identifier of the user
     * @return matched group IDs
     */
    public List<String> getSubadminGroupsOfUser(String userId) {
        return pc.getSubadminGroupsOfUser(userId);
    }

       /**
     * Promotes a user to a subadministrator of a group
     *
     * @param userId unique identifier of the user
     * @param groupId unique identifier of the group
     * @return true if the operation succeeded
     */
    public boolean promoteToSubadmin(String userId, String groupId)
    {
        return pc.promoteToSubadmin(userId, groupId);
    }

    

    /**
     * Remove subadministrator rights of a user for a group
     *
     * @param userId unique identifier of the user
     * @param groupId unique identifier of the group
     * @return true if the operation succeeded
     */
    public boolean demoteSubadmin(String userId, String groupId)
    {
        return pc.demoteSubadmin(userId, groupId);
    }

   

    /**
     * Sends the welcome email to a user
     *
     * @param userId unique identifier of the user
     * @return true if the operation succeeded
     */
    public boolean sendWelcomeMail(String userId)
    {
        return pc.sendWelcomeMail(userId);
    }

   

    /**
     * Gets all members of a group
     *
     * @param groupId unique identifier of the user
     * @return user IDs of members
     */
    public List<String> getMembersOfGroup(String groupId) {
        return pc.getMembersOfGroup(groupId);
    }


    /**
     * Gets all subadministrators of a group
     *
     * @param groupId unique identifier of the group
     * @return user IDs of subadministrators
     */
    public List<String> getSubadminsOfGroup(String groupId) {
        return pc.getSubadminsOfGroup(groupId);
    }

    

    /**
     * Creates a group
     *
     * @param groupId unique identifier of the group
     * @return true if the operation succeeded
     */
    public boolean createGroup(String groupId)
    {
        return pc.createGroup(groupId);
    }

  
    /**
     * Gets all user IDs of this instance
     *
     * @return all user IDs
     */
    public List<String> getUsers()
    {
        return pc.getUsers();
    }

    /**
     * Get all matching user IDs
     *
     * @param search pass null when you don't wish to filter
     * @param limit pass -1 for no limit
     * @param offset pass -1 for no offset
     * @return matched user IDs
     */
    public List<String> getUsers(
            String search, int limit, int offset)
    {
        return pc.getUsers(search, limit, offset);
    }

    

    

    /**
     * Gets all available information of one user
     *
     * @param userId unique identifier of the user
     * @return user object containing all information
     */
    public User getUser(String userId)
    {
        return pc.getUser(userId);
    }

    

    /**
     * Changes a single attribute of a user
     *
     * @param userId unique identifier of the user
     * @param key the attribute to change
     * @param value the value to set
     * @return true if the operation succeeded
     */
    public boolean editUser(String userId, UserData key, String value)
    {
        return pc.editUser(userId, key, value);
    }

  

    /**
     * Deletes a group
     *
     * @param groupId unique identifier of the group
     * @return true if the operation succeeded
     */
    public boolean deleteGroup(String groupId)
    {
        return pc.deleteGroup(groupId);
    }

    

    

    /**
     * Get all group IDs of this instance
     *
     * @return all group IDs
     */
    public List<String> getGroups()
    {
        return pc.getGroups();
    }

    /**
     * Get all matching group IDs
     *
     * @param search pass null when you don't wish to filter
     * @param limit pass -1 for no limit
     * @param offset pass -1 for no offset
     * @return matching group IDs
     */
    public List<String> getGroups(String search, int limit, int offset)
    {
        return pc.getGroups(search, limit, offset);
    }

   

    /**
     * Get all subfolders of the specified path
     *
     * @param path path of the folder
     * @return found subfolders
     * @deprecated The methods naming is somehow misleading, as it lists all resources (subfolders and files) within the given {@code rootPath}. Please use {@link #listFolderContent(String)} instead.
     */
    @Deprecated
    public List<String> getFolders(String path)
    {
        return fd.getFolders(path);
    }

    /**
     * Get all subfolders of the specified path
     *
     * @param path path of the folder
     * @return found subfolders
     */
    public List<String> listFolderContent(String path)
    {
        return fd.listFolderContent(path);
    }

    /**
     * List all file names and subfolders of the specified path traversing into subfolders to the given depth.
     *
     * @param path path of the folder
     * @param depth depth of recursion while listing folder contents
     * @return found file names and subfolders
     */
    public List<String> listFolderContent(String path, int depth)
    {
        return fd.listFolderContent(path, depth);
    }

    /**
     * Checks if the folder at the specified path exists
     *
     * @param path path of the folder
     * @return true if the folder exists
     */
    public boolean folderExists(String path)
    {
        return fd.exists(path);
    }

    /**
     * Creates a folder at the specified path
     *
     * @param path path of the folder
     */
    public void createFolder(String path)
    {
        fd.createFolder(path);
    }

    /**
     * Deletes the folder at the specified path
     *
     * @param path path of the folder
     */
    public void deleteFolder(String path)
    {
        fd.deleteFolder(path);
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
        return fc.doShare(path, shareType, shareWithUserOrGroupId, publicUpload, password, permissions);
    }

    

    /**
     * Deletes a share
     *
     * @param shareId unique identifier of the share
     * @return true if the operation succeeded
     */
    public boolean deleteShare(int shareId)
    {
        return fc.deleteShare(shareId);
    }


    /**
     * Get all shares of this user
     *
     * @return all shares
     */
    public List<Share> getShares()
    {
        return fc.getShares();
    }

   

    /** Uploads a file at the specified path with the data from the InputStream
     *
     * @param inputStream          InputStream of the file which should be uploaded
     * @param remotePath           path where the file should be uploaded to
     */
    public void uploadFile(InputStream inputStream, String remotePath)
    {
        fl.uploadFile(inputStream, remotePath);
    }

    /**
     * method to remove files
     * @param path path of the file which should be removed
     */
    public void removeFile(String path){
        fl.removeFile(path);
    }

    /**
     * Return if the file exists or not
     *
     * @param path path to the file
     * @return boolean value whether the file exists or not
     */
    public boolean fileExists(String path){
        return fl.fileExists(path);
    }

    /**
     * Gets all shares from a given file/folder
     *
     * @param path      path to file/folder
     * @param reShares  returns not only the shares from the current user but all shares from the given file
     * @param subShares returns all shares within a folder, given that path defines a folder
     * @return matching shares
     */
    public Collection<Share> getShares(String path, boolean reShares, boolean subShares)
    {
        return fc.getShares(path, reShares, subShares);
    }

   

    /**
     * Get share info for a single share
     *
     * @param shareId      id of share (Not path of share)
     * @return the share if it has been found, otherwise null
     */
    public Share getShareInfo(int shareId)
    {
        return fc.getShareInfo(shareId);
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
        return fc.editShare(shareId, key, value);
    }

    

    /**
     * Changes multiple attributes of a share at once
     *
     * @param shareId unique identifier of the share
     * @param values a Map containing the attributes to set
     * @return true if the operation succeeded
     */
    public boolean editShare(int shareId, Map<ShareData, String> values)
    {
        return fc.editShare(shareId, values);
    }

    
}
