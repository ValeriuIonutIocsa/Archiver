package com.utils.html.sections.scripts;

public class HtmlSectionScriptText extends HtmlSectionScript {

	private final String jsScriptContent;

	public HtmlSectionScriptText(
			final String jsScriptContent) {

		this.jsScriptContent = jsScriptContent;
	}

	@Override
	protected String createJsScriptContent() {
		return jsScriptContent;
	}
}
