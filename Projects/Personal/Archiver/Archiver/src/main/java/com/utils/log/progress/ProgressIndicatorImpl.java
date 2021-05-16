package com.utils.log.progress;

import com.utils.gui_utils.objects.ProgressIndicatorBar;

public final class ProgressIndicatorImpl extends ProgressIndicatorAbstr {

	public static final ProgressIndicatorImpl INSTANCE = new ProgressIndicatorImpl();

	private ProgressIndicatorBar progressIndicatorBar;

	private ProgressIndicatorImpl() {
	}

	@Override
	public void update(
			final int count,
			final int total) {
		progressIndicatorBar.updateValue(count, total);
	}

	@Override
	public void update(
			final double value) {
		progressIndicatorBar.updateValue(value);
	}

	public void setProgressIndicatorBar(
			final ProgressIndicatorBar progressIndicatorBar) {
		this.progressIndicatorBar = progressIndicatorBar;
	}
}
