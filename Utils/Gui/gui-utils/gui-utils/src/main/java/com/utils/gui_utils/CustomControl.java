package com.utils.gui_utils;

import javafx.event.EventTarget;

public interface CustomControl<
		EventTargetT extends EventTarget> {

	EventTargetT getRoot();
}
