package br.com.anteros.nextcloud.api.utils;

import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.mashape.unirest.http.Unirest;

import br.com.anteros.nextcloud.api.ServerConfig;
import br.com.anteros.nextcloud.api.exception.NextCloudApiException;

public class ConnectorCommon {
	private final ServerConfig serverConfig;

	static {
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
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			Unirest.setHttpClient(httpclient);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ConnectorCommon(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public <R> R executeGet(String part, List<NameValuePair> queryParams, ResponseParser<R> parser) {
		try {
			URI url = buildUrl(part, queryParams);

			com.mashape.unirest.http.HttpResponse<String> response = Unirest.get(url.toString())
					.header("content-type", "application/json").header("ocs-apirequest", "true")
					.header("authorization", "Basic YWRtaW5AY3JtZ2F6aW4uY29tLmJyOkFudGVyb3NANzI3MjA0NTY3ODk=")
					.header("cache-control", "no-cache").asString();
			System.out.println(response);

			R parseResponse = parser.parseResponse(new StringReader(response.getBody()));

			return parseResponse;
		} catch (Exception e) {
			throw new NextCloudApiException(e);
		}
	}

	public <R> R executePost(String part, List<NameValuePair> postParams, ResponseParser<R> parser) {
		try {
			URI url = buildUrl(part, postParams);
			com.mashape.unirest.http.HttpResponse<String> response = Unirest.post(url.toString())
					.header("content-type", "application/json").header("ocs-apirequest", "true")
					.header("authorization", "Basic YWRtaW5AY3JtZ2F6aW4uY29tLmJyOkFudGVyb3NANzI3MjA0NTY3ODk=")
					.header("cache-control", "no-cache").asString();

			R parseResponse = parser.parseResponse(new StringReader(response.getBody()));

			return parseResponse;

		} catch (Exception e) {
			throw new NextCloudApiException(e);
		}
	}

	public <R> R executePut(String part1, String part2, List<NameValuePair> putParams, ResponseParser<R> parser) {
		try {
			URI url = buildUrl(part1 + "/" + part2, putParams);
			com.mashape.unirest.http.HttpResponse<String> response = Unirest.put(url.toString())
					.header("content-type", "application/json").header("ocs-apirequest", "true")
					.header("authorization", "Basic YWRtaW5AY3JtZ2F6aW4uY29tLmJyOkFudGVyb3NANzI3MjA0NTY3ODk=")
					.header("cache-control", "no-cache").asString();

			R parseResponse = parser.parseResponse(new StringReader(response.getBody()));

			return parseResponse;

		} catch (Exception e) {
			throw new NextCloudApiException(e);
		}
	}

	public <R> R executeDelete(String part1, String part2, List<NameValuePair> deleteParams, ResponseParser<R> parser) {
		try {
			URI url = buildUrl(part1 + "/" + part2, deleteParams);

			com.mashape.unirest.http.HttpResponse<String> response = Unirest.delete(url.toString())
					.header("content-type", "application/json").header("ocs-apirequest", "true")
					.header("authorization", "Basic YWRtaW5AY3JtZ2F6aW4uY29tLmJyOkFudGVyb3NANzI3MjA0NTY3ODk=")
					.header("cache-control", "no-cache").asString();

			R parseResponse = parser.parseResponse(new StringReader(response.getBody()));

			return parseResponse;

		} catch (Exception e) {
			throw new NextCloudApiException(e);
		}
	}

	private URI buildUrl(String subPath, List<NameValuePair> queryParams) {
		URIBuilder uB = new URIBuilder().setScheme(serverConfig.isUseHTTPS() ? "https" : "http")
				.setHost(serverConfig.getServerName()+"/")
				.setUserInfo(serverConfig.getUserName(), serverConfig.getPassword()).setPath(subPath);
		if (queryParams != null) {
			uB.addParameters(queryParams);
		}
		try {
			return uB.build();
		} catch (URISyntaxException e) {
			throw new NextCloudApiException(e);
		}
	}

	public interface ResponseParser<R> {
		public R parseResponse(Reader reader);
	}
}
