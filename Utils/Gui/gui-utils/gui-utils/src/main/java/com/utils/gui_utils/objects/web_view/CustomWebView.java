package com.utils.gui_utils.objects.web_view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.clipboard.ClipboardUtils;
import com.utils.io.DesktopUtils;
import com.utils.string.StrUtils;

import javafx.concurrent.Worker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CustomWebView extends CustomControlAbstr<WebView> {

	@Override
	protected WebView createRoot() {

		final WebView webViewRoot = new WebView();
		webViewRoot.setContextMenuEnabled(false);
		configureWebViewCopy(webViewRoot);
		configureUrlHandling(webViewRoot);
		return webViewRoot;
	}

	private static void configureWebViewCopy(
			final WebView webView) {

		webView.addEventFilter(KeyEvent.KEY_PRESSED, (
				KeyEvent event) -> {

			if (event.isControlDown() && event.getCode() == KeyCode.C) {

				final String selectedText = (String) webView.getEngine()
						.executeScript("window.getSelection().toString()");
				if (StringUtils.isNotBlank(selectedText)) {

					final String trimmedSelectedText = StringUtils.stripEnd(selectedText, "\n\r");
					ClipboardUtils.putStringInClipBoard(trimmedSelectedText);
				}
				event.consume();
			}
		});
	}

	private void configureUrlHandling(
			final WebView webView) {

		webView.getEngine().getLoadWorker().stateProperty().addListener((
				observable,
				oldValue,
				newValue) -> {

			if (newValue == Worker.State.SUCCEEDED) {

				final Document document = webView.getEngine().getDocument();
				if (document != null) {

					final NodeList nodeList = document.getElementsByTagName("a");
					final int nodeListLength = nodeList.getLength();
					for (int i = 0; i < nodeListLength; i++) {

						final Node node = nodeList.item(i);
						final EventTarget eventTarget = (EventTarget) node;
						eventTarget.addEventListener("click", evt -> {

							final EventTarget target = evt.getCurrentTarget();
							final HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
							final String href = anchorElement.getHref();
							if (!"#".equals(href)) {

								if ("#top".equals(href)) {
									webView.getEngine().executeScript("window.scrollTo(0,0)");
								} else {
									handleHref(href);
									evt.preventDefault();
								}
							}
						}, false);
					}
				}
			}
		});
	}

	public void handleHref(
			final String href) {
		DesktopUtils.tryBrowse(href);
	}

	public void search(
			final String text,
			final boolean caseSensitive) {

		final boolean notEmpty = StringUtils.isNotEmpty(text);
		if (notEmpty) {

			try {
				final WebEngine webEngine = getRoot().getEngine();
				final Field pageField = webEngine.getClass().getDeclaredField("page");
				pageField.setAccessible(true);

				final Object webPage = pageField.get(webEngine);
				final Method method = webPage.getClass().getMethod("find",
						String.class, boolean.class, boolean.class, boolean.class);
				method.invoke(webPage, text, true, true, caseSensitive);

			} catch (final Exception ignored) {
			}
		}
	}

	public void clear() {
		getRoot().getEngine().loadContent("");
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
