package com.utils.gui_utils.objects;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class HBoxWindowButtons extends CustomControlAbstr<HBox> {

	private final String okButtonName;
	private final Runnable okRunnable;

	public HBoxWindowButtons(
			final String okButtonName,
			final Runnable okRunnable) {

		this.okButtonName = okButtonName;
		this.okRunnable = okRunnable;
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactory.createHBox();

		final Region region = new Region();
		GuiUtils.addToHBox(hBoxRoot, region,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final Button buttonCancel = createButtonCancel();
		if (okRunnable != null) {

			GuiUtils.addToHBox(hBoxRoot, buttonCancel,
					Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 0);

			final Button buttonOk = createButtonOk();
			GuiUtils.addToHBox(hBoxRoot, buttonOk,
					Pos.CENTER_LEFT, Priority.NEVER, 0, 10, 0, 7);

		} else {
			GuiUtils.addToHBox(hBoxRoot, buttonCancel,
					Pos.CENTER_LEFT, Priority.NEVER, 0, 10, 0, 7);
		}

		return hBoxRoot;
	}

	private static Button createButtonCancel() {

		final Button buttonCancel = BasicControlsFactory.createButton("Cancel", "fontSize10");
		buttonCancel.setMinWidth(80);
		buttonCancel.setOnAction(event -> buttonCancel.getScene().getWindow().hide());
		return buttonCancel;
	}

	private Button createButtonOk() {

		final Button buttonOk = BasicControlsFactory.createButton(okButtonName, "fontSize10");
		buttonOk.setMinWidth(80);
		buttonOk.setOnAction(event -> okRunnable.run());
		return buttonOk;
	}
}
