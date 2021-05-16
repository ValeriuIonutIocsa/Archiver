package com.utils.log.progress;

public final class ProgressIndicator {

	private static ProgressIndicatorAbstr instance = ProgressIndicatorNoOp.INSTANCE;

	private ProgressIndicator() {
	}

	public static void setInstance(
			final ProgressIndicatorAbstr instance) {
		ProgressIndicator.instance = instance;
	}

	public static ProgressIndicatorAbstr getInstance() {
		return instance;
	}
}
