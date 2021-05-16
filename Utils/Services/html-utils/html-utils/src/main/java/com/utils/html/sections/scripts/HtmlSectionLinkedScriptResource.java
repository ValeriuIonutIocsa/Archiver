package com.utils.html.sections.scripts;

import com.utils.io.IoUtils;

public class HtmlSectionLinkedScriptResource extends HtmlSectionLinkedScript {

	public HtmlSectionLinkedScriptResource(
			final String scriptResourceFilePath) {
		super(IoUtils.resourceToUrl(scriptResourceFilePath).toExternalForm());
	}
}
