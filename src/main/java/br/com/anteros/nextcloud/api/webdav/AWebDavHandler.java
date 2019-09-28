package br.com.anteros.nextcloud.api.webdav;

import java.io.IOException;

import org.apache.http.client.utils.URIBuilder;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.nextcloud.api.ServerConfig;
import br.com.anteros.nextcloud.api.exception.NextCloudApiException;


/**
 *
 * @author a.schild
 */
public abstract class AWebDavHandler {
	protected static Logger LOG = LoggerProvider.getInstance().getLogger(AWebDavHandler.class.getName());

    public static final int  FILE_BUFFER_SIZE= 4096;
    private static final String WEB_DAV_BASE_PATH = "remote.php/webdav/";
    
    private final ServerConfig _serverConfig;

    public AWebDavHandler(ServerConfig serverConfig) {
        _serverConfig = serverConfig;
    }
    
    /**
     * Build the full URL for the webdav access to a resource
     * 
     * @param remotePath remote path for file (Not including remote.php/webdav/)
     * @return Full URL including http....
     */
    protected String buildWebdavPath(String remotePath)
    {
        URIBuilder uB= new URIBuilder()
        .setScheme(_serverConfig.isUseHTTPS() ? "https" : "http")
        .setHost(_serverConfig.getServerName())
        .setPort(_serverConfig.getPort())
        .setPath( WEB_DAV_BASE_PATH + remotePath);
        return uB.toString();
    }
    
    /**
     * Create a authenticate sardine connector
     * 
     * @return sardine connector to server including authentication
     */
    protected Sardine buildAuthSardine()
    {
        Sardine sardine = SardineFactory.begin();
        sardine.setCredentials(_serverConfig.getUserName(), _serverConfig.getPassword());
        sardine.enablePreemptiveAuthentication(_serverConfig.getServerName());
        
        return sardine;
    }
    
    /**
     * method to check if a remote object already exists
     *
     * @param remotePath path of the file/folder
     * @return boolean value if the given file/folder exists or not
     */
    public boolean pathExists(String remotePath) {
        String path = buildWebdavPath(remotePath);
        Sardine sardine = buildAuthSardine();

        try
        {
            return sardine.exists(path);
        } catch (IOException e)
        {
            throw new NextCloudApiException(e);
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
     * Deletes the file/folder at the specified path
     *
     * @param remotePath path of the file/folder
     */
    public void deletePath(String remotePath)
    {
        String path=  buildWebdavPath( remotePath );

        Sardine sardine = buildAuthSardine();
        try {
            sardine.delete(path);
        } catch (IOException e) {
            throw new NextCloudApiException(e);
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
}