package com.utils.gui_utils.alerts;

import com.utils.gui_utils.CustomControl;

import javafx.scene.control.Dialog;

public interface CustomDialog<
		ObjectT,
		DialogT extends Dialog<ObjectT>> extends CustomControl<DialogT> {

	int getPrefWidth();

	int getPrefHeight();

	void showAndWait();

	ObjectT getResult();
}
