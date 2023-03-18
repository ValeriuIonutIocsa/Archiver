package com.personal.archiver.gui;

import com.utils.gui.objects.ProgressIndicatorBar;
import com.utils.log.progress.AbstractProgressIndicator;

final class ProgressIndicatorImpl extends AbstractProgressIndicator {

	private final ProgressIndicatorBar progressIndicatorBar;

	ProgressIndicatorImpl(
            final ProgressIndicatorBar progressIndicatorBar) {

		this.progressIndicatorBar = progressIndicatorBar;
	}

	@Override
	public void update(
			final double value) {
		progressIndicatorBar.updateValue(value);
	}
}
