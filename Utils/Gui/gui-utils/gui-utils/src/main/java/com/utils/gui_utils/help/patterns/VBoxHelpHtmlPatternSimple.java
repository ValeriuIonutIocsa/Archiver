package com.utils.gui_utils.help.patterns;

import com.utils.gui_utils.help.VBoxHelpHtml;

public class VBoxHelpHtmlPatternSimple extends VBoxHelpHtml {

	@Override
	protected String createHtmlResourceFilePathString() {
		return "com/utils/gui_utils/help/patterns/HelpPatternSimple.html";
	}

	@Override
	protected String createTitle() {
		return "Help: Simple Pattern";
	}
}
