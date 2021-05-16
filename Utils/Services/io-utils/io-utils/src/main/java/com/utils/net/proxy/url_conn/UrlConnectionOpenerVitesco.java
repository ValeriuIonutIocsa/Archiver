package com.utils.net.proxy.url_conn;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import com.utils.net.proxy.VitescoProxyUtils;

class UrlConnectionOpenerVitesco extends AbstractUrlConnectionOpener {

	UrlConnectionOpenerVitesco() {
	}

	@Override
	public void configureProperties() {

		System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

		final Authenticator authenticator = new Authenticator() {

			@Override
			public PasswordAuthentication getPasswordAuthentication() {

				final String httpUsername = VitescoProxyUtils.getHTTPUsername();
				final String httpPassword = VitescoProxyUtils.getHTTPPassword();
				return new PasswordAuthentication(httpUsername, httpPassword.toCharArray());
			}
		};
		Authenticator.setDefault(authenticator);
	}

	@Override
	public URLConnection openURLConnection(
			final URL url) throws IOException {

		final Proxy proxy = VitescoProxyUtils.createProxy();
		return url.openConnection(proxy);
	}
}
