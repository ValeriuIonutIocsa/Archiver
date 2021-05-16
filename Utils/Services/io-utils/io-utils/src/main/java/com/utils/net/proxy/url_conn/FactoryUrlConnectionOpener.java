package com.utils.net.proxy.url_conn;

import com.utils.users.CurrentUser;

public final class FactoryUrlConnectionOpener {

	private FactoryUrlConnectionOpener() {
	}

	public static UrlConnectionOpener newInstance() {

		final UrlConnectionOpener urlConnectionOpener;
		final String userName = CurrentUser.USER_NAME;
		if ("uid39522".equals(userName)) {
			urlConnectionOpener = new UrlConnectionOpenerVitesco();
		} else {
			urlConnectionOpener = new UrlConnectionOpenerRegular();
		}
		return urlConnectionOpener;
	}
}
