package br.com.anteros.nextcloud.api.utils;

import java.net.ProxySelector;

import com.github.sardine.Sardine;
import com.github.sardine.impl.SardineImpl;

public class AnterosSardineFactory {

	private AnterosSardineFactory() {
	}

	/**
	 * Default begin() for when you don't need anything but no authentication
	 * and default settings for SSL.
	 */
	public static Sardine begin() {
		return begin(null, null);
	}

	/**
	 * Pass in a HTTP Auth username/password for being used with all connections
	 *
	 * @param username
	 *            Use in authentication header credentials
	 * @param password
	 *            Use in authentication header credentials
	 */
	public static Sardine begin(String username, String password) {
		return begin(username, password, null);
	}

	/**
	 * @param username
	 *            Use in authentication header credentials
	 * @param password
	 *            Use in authentication header credentials
	 * @param proxy
	 *            Proxy configuration
	 */
	public static Sardine begin(String username, String password, ProxySelector proxy) {
		return new AnterosSardineImpl(username, password, proxy);
	}
	
}
