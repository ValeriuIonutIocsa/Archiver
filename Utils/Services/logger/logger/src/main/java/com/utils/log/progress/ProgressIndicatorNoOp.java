package com.utils.log.progress;

public final class ProgressIndicatorNoOp extends ProgressIndicatorAbstr {

	public static final ProgressIndicatorNoOp INSTANCE = new ProgressIndicatorNoOp();

	private ProgressIndicatorNoOp() {
		super();
	}

	@Override
	public void update(
			final int count,
			final int total) {
	}

	@Override
	public void update(
			final double value) {
	}
}
