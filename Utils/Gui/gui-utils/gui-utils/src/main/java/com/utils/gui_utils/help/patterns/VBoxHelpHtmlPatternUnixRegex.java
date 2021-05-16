package com.utils.gui_utils.help.patterns;

import com.utils.gui_utils.help.VBoxHelpHtml;

public class VBoxHelpHtmlPatternUnixRegex extends VBoxHelpHtml {

	@Override
	protected String createHtmlResourceFilePathString() {
		return "com/utils/gui_utils/help/patterns/HelpPatternUnixRegex.html";
	}

	@Override
	protected String createTitle() {
		return "Help: Unix Regex Pattern";
	}
}
