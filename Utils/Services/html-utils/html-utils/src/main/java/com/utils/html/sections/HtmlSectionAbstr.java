package com.utils.html.sections;

import com.utils.string.StrUtils;

public abstract class HtmlSectionAbstr implements HtmlSection {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
