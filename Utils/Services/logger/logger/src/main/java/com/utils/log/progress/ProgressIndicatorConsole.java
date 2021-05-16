package com.utils.log.progress;

import com.utils.log.Logger;

public final class ProgressIndicatorConsole extends ProgressIndicatorAbstr {

	public static final ProgressIndicatorConsole INSTANCE = new ProgressIndicatorConsole();

	private ProgressIndicatorConsole() {
	}

	@Override
	public void update(
			final int count,
			final int total) {
		Logger.printStatus("done " + count + "/" + total);
	}

	@Override
	public void update(
			final double value) {
	}
}
