package br.com.anteros.nextcloud.api.utils;

import java.io.IOException;
import java.io.InputStream;
import com.github.sardine.Sardine;
import org.apache.commons.io.input.ProxyInputStream;

public class WebDavInputStream extends ProxyInputStream {

    private final Sardine sardine; // Sardine instance used
    
    public WebDavInputStream(Sardine sardine, InputStream in)
    {
        super(in);
        this.sardine= sardine;
    }

    @Override
    public void close() throws IOException {
        super.close();
        sardine.shutdown();
    }
    
    
    
}