package com.utils.gui_utils.logger;

import java.util.regex.Pattern;

import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.objects.web_view.CustomWebView;
import com.utils.log.Logger;
import com.utils.log.MessageConsumer;
import com.utils.log.MessageLevel;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebView;

public class CustomWebViewGuiLogger extends CustomWebView {

	public static final String HTML_FONT =
			"<style> * { font-family: \"Helvetica\"; font-size: 12px; } </style>";
	private static final String HTML_HEADER = "<html>" +
			"<head>" +
			HTML_FONT +
			"<script language=\"javascript\" type=\"text/javascript\">" +
			"function toBottom(){window.scrollTo(0,document.body.scrollHeight);}" +
			"</script>" +
			"</head>" +
			"<body onload='toBottom()'>";
	private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\R");

	private final StringBuilder stringBuilder;

	public CustomWebViewGuiLogger() {

		stringBuilder = new StringBuilder();
		initStringBuilder();
	}

	@Override
	protected WebView createRoot() {

		final WebView webViewRoot = super.createRoot();

		final ContextMenu contextMenu = new ContextMenu();

		final MenuItem menuItemClear = new MenuItem("clear");
		menuItemClear.setOnAction(event -> clear());
		contextMenu.getItems().add(menuItemClear);

		webViewRoot.setOnMouseClicked(mouseEvent -> {
			if (GuiUtils.isRightClick(mouseEvent)) {
				contextMenu.show(webViewRoot, mouseEvent.getScreenX(), mouseEvent.getScreenY());
			} else {
				contextMenu.hide();
			}
		});

		return webViewRoot;
	}

	public void useAsLogger() {

		Logger.setMessageConsumer((
				messageLevel,
				message) -> {

			MessageConsumer.DEFAULT_MESSAGE_CONSUMER.accept(messageLevel, message);
			final String text;
			if (messageLevel == MessageLevel.PROGRESS || messageLevel == MessageLevel.STATUS) {
				text = "<b>" + message + "</b><br>";
			} else if (messageLevel == MessageLevel.WARNING) {
				text = "<b><font color=\"DarkOrange\">" + message + "</font></b><br>";
			} else if (messageLevel == MessageLevel.ERROR || messageLevel == MessageLevel.EXCEPTION) {
				text = "<b><font color=\"red\">" + message + "</font></b><br>";
			} else {
				text = message + "<br>";
			}
			log(text);
		});
	}

	public void log(
			final String textParam) {

		String text = textParam;
		text = NEW_LINE_PATTERN.matcher(text).replaceAll("<br>");
		stringBuilder.append(text);
		if (stringBuilder.length() > 100_000) {
			stringBuilder.replace(HTML_HEADER.length(), 36_000, "");
		}
		final String content = stringBuilder + "<br></body></html>";
		loadContent(content);
	}

	@Override
	public void clear() {

		loadContent("");
		initStringBuilder();
	}

	private void initStringBuilder() {

		stringBuilder.setLength(0);
		stringBuilder.append(HTML_HEADER);
	}

	private void loadContent(
			final String content) {
		GuiUtils.run(() -> getRoot().getEngine().loadContent(content));
	}
}
