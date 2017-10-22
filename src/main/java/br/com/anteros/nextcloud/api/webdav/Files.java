package br.com.anteros.nextcloud.api.webdav;

import java.io.IOException;
import java.io.InputStream;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import br.com.anteros.nextcloud.api.ServerConfig;
import br.com.anteros.nextcloud.api.exception.NextCloudApiException;
import br.com.anteros.nextcloud.api.utils.AnterosSardineFactory;

/**
 * 
 * @author tott 
 * modified: Edson Martins
 *
 */
public class Files {

	private static final String WEB_DAV_BASE_PATH = "remote.php/webdav/";
	
	private final ServerConfig _serverConfig;
	
	public Files(ServerConfig _serverConfig) {
		this._serverConfig = _serverConfig;
	}
	
	/**
	 * method to check if a file already exists
	 * 
	 * @param rootPath path of the file
	 * @return boolean value if the given file exists or not
	 */
	public boolean fileExists(String rootPath){
		String path = (_serverConfig.isUseHTTPS() ? "https" : "http") + "://" + _serverConfig.getServerName() + "/" + WEB_DAV_BASE_PATH + rootPath;
		
		Sardine sardine = AnterosSardineFactory.begin();
		
		sardine.setCredentials(_serverConfig.getUserName(), _serverConfig.getPassword());
		sardine.enablePreemptiveAuthentication(_serverConfig.getServerName());
		
		try {
			return sardine.exists(path);
		} catch (IOException e) {
			throw new NextCloudApiException(e);
		}
	}

    /** Uploads a file at the specified path with the data from the InputStream
     *
     * @param inputStream          InputStream of the file which should be uploaded
     * @param remotePath           path where the file should be uploaded to
     */
    public void uploadFile(InputStream inputStream, String remotePath)
    {
    	String path = (_serverConfig.isUseHTTPS() ? "https" : "http") + "://" + _serverConfig.getServerName() + "/" + WEB_DAV_BASE_PATH + remotePath;
		
		Sardine sardine = AnterosSardineFactory.begin();
        sardine.setCredentials(_serverConfig.getUserName(), _serverConfig.getPassword());
        sardine.enablePreemptiveAuthentication(_serverConfig.getServerName());

        try {
        	sardine.exists(path);
            sardine.put(path, inputStream);
        } catch (IOException e) {
            throw new NextCloudApiException(e);
        }
    }
	
	/**
	 * method to remove files
	 * @param rootPath path of the file which should be removed
	 */
	public void removeFile(String rootPath) {
		String path = (_serverConfig.isUseHTTPS() ? "https" : "http") + "://" + _serverConfig.getServerName() + "/" + WEB_DAV_BASE_PATH + rootPath;
		
		Sardine sardine = AnterosSardineFactory.begin();
		
		sardine.setCredentials(_serverConfig.getUserName(), _serverConfig.getPassword());
        sardine.enablePreemptiveAuthentication(_serverConfig.getServerName());
		try {
			sardine.delete(path);
		} catch ( IOException e ) {
			throw new NextCloudApiException(e);
		}
	}
}
