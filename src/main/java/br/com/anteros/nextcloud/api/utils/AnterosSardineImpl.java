package br.com.anteros.nextcloud.api.utils;

import java.net.ProxySelector;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;

import com.github.sardine.impl.SardineImpl;

public class AnterosSardineImpl extends SardineImpl {

	public AnterosSardineImpl() {
	}

	public AnterosSardineImpl(String bearerAuth) {
		super(bearerAuth);
	}

	public AnterosSardineImpl(HttpClientBuilder builder) {
		super(builder);
	}

	public AnterosSardineImpl(String username, String password) {
		super(username, password);
	}

	public AnterosSardineImpl(String username, String password, ProxySelector selector) {
		super(username, password, selector);
	}

	public AnterosSardineImpl(HttpClientBuilder builder, String username, String password) {
		super(builder, username, password);
	}

	@Override
	protected SSLConnectionSocketFactory createDefaultSecureSocketFactory() {
		SSLConnectionSocketFactory sslsf = null;
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}

			} };

			SSLContext sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			sslsf = new SSLConnectionSocketFactory(sslcontext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sslsf;
	}

}
