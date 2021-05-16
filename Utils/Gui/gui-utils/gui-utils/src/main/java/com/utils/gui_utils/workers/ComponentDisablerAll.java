package com.utils.gui_utils.workers;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ComponentDisablerAll implements ComponentDisabler {

	private final Scene scene;

	public ComponentDisablerAll(
			final Scene scene) {

		this.scene = scene;
	}

	@Override
	public void setComponentsDisabled(
			final boolean b) {

		if (scene != null) {

			final Parent root = scene.getRoot();
			for (final Node node : root.getChildrenUnmodifiable()) {
				node.setDisable(b);
			}
		}
	}
}
