package com.utils.gui_utils.help.patterns;

import com.utils.gui_utils.help.VBoxHelpHtml;

public class VBoxHelpHtmlPatternGlob extends VBoxHelpHtml {

	@Override
	protected String createHtmlResourceFilePathString() {
		return "com/utils/gui_utils/help/patterns/HelpPatternGlob.html";
	}

	@Override
	protected String createTitle() {
		return "Help: Glob Pattern";
	}
}
