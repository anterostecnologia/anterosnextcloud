package br.com.anteros.nextcloud.api.webdav;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.github.sardine.Sardine;

import br.com.anteros.nextcloud.api.ServerConfig;
import br.com.anteros.nextcloud.api.exception.NextCloudApiException;
import br.com.anteros.nextcloud.api.utils.WebDavInputStream;

/**
 * 
 * @author tott 
 * modified: Edson Martins
 *
 */
public class Files extends AWebDavHandler{

    public Files(ServerConfig serverConfig) {
        super(serverConfig);
    }

    /**
     * method to check if a file already exists
     *
     * @param remotePath path of the file
     * @return boolean value if the given file exists or not
     */
    public boolean fileExists(String remotePath) {
        return pathExists(remotePath);
    }

    /**
     * Uploads a file at the specified path with the data from the InputStream
     *
     * @param inputStream InputStream of the file which should be uploaded
     * @param remotePath path where the file should be uploaded to
     */
    public void uploadFile(InputStream inputStream, String remotePath) {
        String path = buildWebdavPath(remotePath);
        Sardine sardine = buildAuthSardine();

        try
        {
            sardine.put(path, inputStream);
        } catch (IOException e)
        {
            throw new NextCloudApiException(e);
        }
    }

    /**
     * method to remove files
     *
     * @param remotePath path of the file which should be removed
     */
    public void removeFile(String remotePath) {
        deletePath(remotePath);
    }

    /**
     * Downloads the file at the specified remotepath to the download directory,
     * and returns true if the download is successful
     *
     * @param remotePath Remotepath where the file is saved in the nextcloud
     * server
     * @param downloadDirPath Path where the file is downloaded, it would be
     * created if it doesn't exist.
     * @return boolean
     * @throws IOException  In case of IO errors
     */
    public boolean downloadFile(String remotePath, String downloadDirPath) throws IOException {
        boolean status = false;
        String path = buildWebdavPath(remotePath);
        Sardine sardine = buildAuthSardine();

        File downloadFilepath = new File(downloadDirPath);
        if (!downloadFilepath.exists())
        {
            downloadFilepath.mkdir();
        }

        if (fileExists(remotePath))
        {
            //Extract the Filename from the path
            String[] segments = path.split("/");
            String filename = segments[segments.length - 1];
            downloadDirPath = downloadDirPath + "/" + filename;
        }
        InputStream in = null;
        try
        {
            in = sardine.get(path);
            byte[] buffer = new byte[AWebDavHandler.FILE_BUFFER_SIZE];
            int bytesRead;
            File targetFile = new File(downloadDirPath);
            try (OutputStream outStream = new FileOutputStream(targetFile))
            {
                while ((bytesRead = in.read(buffer)) != -1)
                {
                    outStream.write(buffer, 0, bytesRead);
                }
                outStream.flush();
                outStream.close();
            }
            status = true;
        } catch (IOException e)
        {
            throw new NextCloudApiException(e);
        } finally
        {
            sardine.shutdown();
            if (in != null)
            {
                in.close();
            }
        }
        return status;
    }

    /**
     * Downloads the file at the specified remotepath as an InputStream,
     *
     * @param remotePath Remotepath where the file is saved in the nextcloud
     * server
     * @return InputStream
     * @throws IOException  In case of IO errors
     */
    public InputStream downloadFile(String remotePath) throws IOException {
        String path = buildWebdavPath(remotePath);
        Sardine sardine = buildAuthSardine();

        WebDavInputStream in = null;
        try
        {
            in = new WebDavInputStream(sardine, sardine.get(path));
        } catch (IOException e)
        {
            sardine.shutdown();
            throw new NextCloudApiException(e);
        }
        return in;
    }
    
}