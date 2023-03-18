package com.utils.gui;

import java.util.List;

import com.utils.gui.styles.vitesco.VitescoStyleUtils;
import com.utils.gui.styles.vitesco.themes.GuiTheme;

public abstract class AbstractCustomApplicationTestVitesco extends AbstractCustomApplicationTest {

	@Override
	public void fillStylesheetList(
			final List<String> stylesheetList) {

		stylesheetList.add(GuiTheme.STANDARD.getStyleSheetResourcePathString());
		stylesheetList.add(VitescoStyleUtils.COMMON_STYLE_SHEET_RESOURCE_PATH_STRING);
	}
}
