package com.personal.archiver.gui.res;

import com.utils.gui.GuiUtils;

import javafx.scene.image.Image;

public final class ImagesArchiver {

	private ImagesArchiver() {
	}

	public static Image createImageApp() {
		return GuiUtils.createImageFromResourceFile("com/personal/archiver/gui/res/icon_app.png");
	}
}
