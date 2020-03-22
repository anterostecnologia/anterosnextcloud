package br.com.anteros.nextcloud.api.utils;

import com.github.sardine.Sardine;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.input.ProxyInputStream;

public class WebdavInputStream extends ProxyInputStream {

	private final Sardine sardine; // Sardine instance used

	public WebdavInputStream(Sardine sardine, InputStream in) {
		super(in);
		this.sardine = sardine;
	}

	@Override
	public void close() throws IOException {
		super.close();
		sardine.shutdown();
	}

}
