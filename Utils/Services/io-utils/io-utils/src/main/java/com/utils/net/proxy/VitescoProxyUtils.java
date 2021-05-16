package com.utils.net.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

import com.utils.annotations.ApiMethod;

public final class VitescoProxyUtils {

	private VitescoProxyUtils() {
	}

	@ApiMethod
	public static Proxy createProxy() {

		final String httpHost = VitescoProxyUtils.getHTTPHost();
		final int httpPort = VitescoProxyUtils.getHTTPPort();
		final SocketAddress socketAddress = new InetSocketAddress(httpHost, httpPort);
		return new Proxy(Proxy.Type.HTTP, socketAddress);
	}

	@ApiMethod
	public static String getHTTPHost() {
		return "cias3basic.conti.de";
	}

	@ApiMethod
	public static int getHTTPPort() {
		return 8080;
	}

	@ApiMethod
	public static String getHTTPUsername() {
		return "uid39522";
	}

	@ApiMethod
	public static String getHTTPPassword() {
		return "crocrocro_010";
	}
}
