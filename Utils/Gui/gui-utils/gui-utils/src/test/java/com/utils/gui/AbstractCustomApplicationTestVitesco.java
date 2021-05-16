package com.utils.gui;

import java.util.List;

import com.utils.gui_utils.styles.vitesco.VitescoStyleUtils;
import com.utils.gui_utils.styles.vitesco.themes.GuiTheme;

import javafx.scene.image.Image;

public abstract class AbstractCustomApplicationTestVitesco extends AbstractCustomApplicationTest {

	protected AbstractCustomApplicationTestVitesco(
			final Image imageApp) {
		super(imageApp);
	}

	@Override
	protected void fillStylesheetList(
			final List<String> stylesheetList) {

		stylesheetList.add(GuiTheme.STANDARD.getStyleSheetResourcePathString());
		stylesheetList.add(VitescoStyleUtils.COMMON_STYLE_SHEET_RESOURCE_PATH_STRING);
	}
}
