package com.utils.log.progress;

public abstract class ProgressIndicatorAbstr {

	public abstract void update(
			int count,
			int total);

	public abstract void update(
			double value);
}
