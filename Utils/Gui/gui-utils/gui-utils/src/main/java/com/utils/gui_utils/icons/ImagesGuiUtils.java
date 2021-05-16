package com.utils.gui_utils.icons;

import com.utils.gui_utils.GuiUtils;

import javafx.scene.image.Image;

public final class ImagesGuiUtils {

	private ImagesGuiUtils() {
	}

	public static Image createImageHelp() {
		return GuiUtils.createImageFromResourceFile("com/utils/gui_utils/icons/icon_help.png");
	}

	public static Image createImageUpArrow() {
		return GuiUtils.createImageFromResourceFile("com/utils/gui_utils/icons/icon_up_arrow.png");
	}

	public static Image createImageDownArrow() {
		return GuiUtils.createImageFromResourceFile("com/utils/gui_utils/icons/icon_down_arrow.png");
	}

	public static Image createImageLongArrow() {
		return GuiUtils.createImageFromResourceFile("com/utils/gui_utils/icons/icon_long_arrow.png");
	}

	public static Image createImageSearch() {
		return GuiUtils.createImageFromResourceFile("com/utils/gui_utils/icons/icon_search.png");
	}

	public static final Image IMAGE_FILE =
			GuiUtils.createImageFromResourceFile("com/utils/gui_utils/icons/icon_file.png");
	public static final Image IMAGE_FOLDER =
			GuiUtils.createImageFromResourceFile("com/utils/gui_utils/icons/icon_folder.png");
}
