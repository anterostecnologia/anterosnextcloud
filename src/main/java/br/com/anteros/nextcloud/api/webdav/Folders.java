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
package br.com.anteros.nextcloud.api.webdav;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;

import br.com.anteros.nextcloud.api.ServerConfig;
import br.com.anteros.nextcloud.api.exception.NextcloudApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author a.schild
 */
public class Folders extends AWebdavHandler{

    private static final Log LOG = LogFactory.getLog(Folders.class);

    public Folders(ServerConfig serverConfig) {
        super(serverConfig);
    }

    /**
     * Get all subfolders of the specified path
     *
     * @param remotePath path of the folder
     * @return found subfolders
     *
     * @deprecated The methods naming is somehow misleading, as it lists 
     * all resources (subfolders and files) within the given {@code rootPath}. 
     * Please use {@link #listFolderContent(String)} instead.
     */
    @Deprecated
    public List<String> getFolders(String remotePath)
    {
        return listFolderContent(remotePath);
    }

    /**
     * List all file names and subfolders of the specified path
     *
     * @param remotePath path of the folder
     * @return found file names and subfolders
     */
    public List<String> listFolderContent(String remotePath)
    {
        return listFolderContent(remotePath, 1);
    }

    /**
     * List all file names and subfolders of the specified path traversing 
     * into subfolders to the given depth.
     *
     * @param remotePath path of the folder
     * @param depth depth of recursion while listing folder contents
     * @return found file names and subfolders
     */
    public List<String> listFolderContent(String remotePath, int depth)
    {
        String path=  buildWebdavPath(remotePath );

        List<String> retVal= new LinkedList<>();
        Sardine sardine = buildAuthSardine();
        List<DavResource> resources;
        try {
            resources = sardine.list(path, depth);
        } catch (IOException e) {
            throw new NextcloudApiException(e);
        }
        finally
        {
            try
            {
                sardine.shutdown();
            }
            catch (IOException ex)
            {
                LOG.warn("error in closing sardine connector", ex);
            }
        }
        for (DavResource res : resources)
        {
        	if (!res.getPath().endsWith(remotePath)) {
        		retVal.add(res.getName());
        	}
        }
        Collections.sort(retVal);
        return retVal;
    }

    /**
     * Checks if the folder at the specified path exists
     *
     * @param remotePath path of the folder
     * @return true if the folder exists
     */
    public boolean exists(String remotePath)
    {
        return pathExists(remotePath);
    }

    /**
     * Creates a folder at the specified path
     *
     * @param remotePath path of the folder
     */
    public void createFolder(String remotePath)
    {
        String path=  buildWebdavPath(remotePath );
        Sardine sardine = buildAuthSardine();

        try {
            sardine.createDirectory(path);
        } catch (IOException e) {
            throw new NextcloudApiException(e);
        }
        finally
        {
            try
            {
                sardine.shutdown();
            }
            catch (IOException ex)
            {
                LOG.warn("error in closing sardine connector", ex);
            }
        }
    }

    /**
     * Deletes the folder at the specified path
     *
     * @param remotePath path of the folder
     */
    public void deleteFolder(String remotePath)
    {
        deletePath(remotePath);
    }
    
    /**
     * List all file names and subfolders of the specified path traversing 
     * into subfolders to the given depth.
     *
     * @param remotePath path of the folder
     * @param depth depth of recursion while listing folder contents
     * @param excludeFolderNames excludes the folder names from the list
     * @return found file names and subfolders
     */
    public List<String> listFolderContent(String remotePath, int depth, boolean excludeFolderNames)
    {
        String path = buildWebdavPath(remotePath);

        List<String> retVal = new LinkedList<>();
        Sardine sardine = buildAuthSardine();
        List<DavResource> resources;
        try {
            resources = sardine.list(path, depth);
        } catch (IOException e) {
            throw new NextcloudApiException(e);
        }
        finally
        {
            try
            {
                sardine.shutdown();
            }
            catch (IOException ex)
            {
                LOG.warn("error in closing sardine connector", ex);
            }
        }
        for (DavResource res : resources)
        {
            if (excludeFolderNames) {
                if (!res.isDirectory()) {
                    retVal.add(res.getName());
                }
            }
            else {
                retVal.add(res.getName());
            }
        }
        return retVal;
    }
    
    
    /**
     * List all file names and subfolders of the specified path traversing 
     * into subfolders to the given depth.
     *
     * @param remotePath path of the folder
     * @param depth depth of recursion while listing folder contents
     * @param excludeFolderNames excludes the folder names from the list
     * @return found file names and subfolders
     */
    public List<FolderItemDetail> listDetailsFolderContent(String remotePath, int depth, boolean excludeFolderNames)
    {
        String path = buildWebdavPath(remotePath);

        List<FolderItemDetail> retVal = new LinkedList<>();
        Sardine sardine = buildAuthSardine();
        List<DavResource> resources;
        try {
            resources = sardine.list(path, depth);
        } catch (IOException e) {
            throw new NextcloudApiException(e);
        }
        finally
        {
            try
            {
                sardine.shutdown();
            }
            catch (IOException ex)
            {
                LOG.warn("error in closing sardine connector", ex);
            }
        }
        for (DavResource res : resources)
        {
        	FolderItemDetail detail = new FolderItemDetail();
        	detail.setContentLength(res.getContentLength());
        	detail.setContentType(res.getContentType());
        	detail.setCreation(res.getCreation());
        	detail.setModified(res.getModified());
        	detail.setDisplayName(res.getDisplayName());
        	detail.setDirectory(res.isDirectory());
        	detail.setPath(res.getPath());
        	detail.setName(res.getName());
        	
        	
            if (excludeFolderNames) {
                if (!res.isDirectory()) {
                    retVal.add(detail);
                }
            }
            else {
                retVal.add(detail);
            }
        }
        return retVal;
    }

    /**
     * Downloads the folder at the specified remotePath to the rootDownloadDirPath
     *
     * @param remotePath the path in the nextcloud server with respect to the specific folder
     * @param rootDownloadDirPath the local path in the system where the folder needs be saved
     * @throws IOException  In case of IO errors
     */
    public void downloadFolder(String remotePath, String rootDownloadDirPath) throws IOException {
        int depth=1;
        String rootPath = buildWebdavPath("");
        String[] segments = remotePath.split("/");
        String folderName = segments[segments.length - 1];
        String newDownloadDir = rootDownloadDirPath + "/" + folderName;
        File nefile1 = new File(newDownloadDir);
        if(!nefile1.exists()) {
            LOG.info("Creating new download directory: "+newDownloadDir);
            nefile1.mkdir();
        }
        String rootPathNew= rootPath+remotePath ;
        int count = 0;
        String filePath;
        List<String> retVal= new LinkedList<>();
        List<DavResource> resources;
        Sardine sardine = buildAuthSardine();
        try
        {
            try {
                resources = sardine.list(rootPathNew, depth);
            } catch (IOException e) {
                throw new NextcloudApiException(e);
            }

            for (DavResource res : resources)
            {
                System.out.println(res.getName());
                //Skip the Documents folder which is listed as default as first by the sardine output
                if(count != 0) {
                    if(res.isDirectory()) {
                        String fileName = res.getName();
                        String pathtosend = remotePath + "/" + fileName;
                        downloadFolder(pathtosend,newDownloadDir);
                    }
                    else {
                            String fileName = res.getName();
                            filePath = rootPathNew + "/" + fileName;
                            retVal.add(res.getName());

                            InputStream in = null;
                            if (sardine.exists(filePath)) {
                                in = sardine.get(filePath);
                                byte[] buffer = new byte[AWebdavHandler.FILE_BUFFER_SIZE];
                                int bytesRead;
                                File targetFile = new File(newDownloadDir + "/" + fileName);
                                try (OutputStream outStream = new FileOutputStream(targetFile))
                                {
                                    while ((bytesRead = in.read(buffer)) != -1)
                                    {
                                        outStream.write(buffer, 0, bytesRead);
                                    }
                                    outStream.flush();
                                    outStream.close();
                                    in.close();
                                }
                            }
                    }
                }
                count ++;
            }
        }
        finally
        {
            try
            {
                sardine.shutdown();
            }
            catch (IOException ex)
            {
                LOG.warn("error in closing sardine connector", ex);
            }
        }
    }


    /**
     * method to rename/move folder
     *
     * @param oldPath path of the folder which should be renamed/moved
     * @param newPath path of the folder which should be renamed/moved
     * @param overwriteExisting overwrite if target already exists
     */
    public void renameFolder(String oldPath, String newPath, boolean overwriteExisting) {
        renamePath(oldPath, newPath, overwriteExisting);
    }
}
