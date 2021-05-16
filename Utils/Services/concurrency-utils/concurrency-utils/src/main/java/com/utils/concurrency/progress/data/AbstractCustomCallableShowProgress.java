package com.utils.concurrency.progress.data;

import java.util.concurrent.atomic.AtomicInteger;

import com.utils.concurrency.no_progress.data.AbstractCustomCallable;
import com.utils.log.progress.ProgressIndicator;

public abstract class AbstractCustomCallableShowProgress extends AbstractCustomCallable {

	private final AtomicInteger completedRunnablesCount;
	private final int runnableCount;
	private final int showProgressInterval;

	AbstractCustomCallableShowProgress(
			final Runnable runnable,
			final AtomicInteger completedRunnablesCount,
			final int runnableCount,
			final int showProgressInterval) {

		super(runnable);

		this.completedRunnablesCount = completedRunnablesCount;
		this.runnableCount = runnableCount;
		this.showProgressInterval = showProgressInterval;
	}

	@Override
	public Void call() {

		final Void result = super.call();

		final int runnableIndex = completedRunnablesCount.incrementAndGet();
		if (runnableIndex % showProgressInterval == 0) {
			ProgressIndicator.getInstance().update(runnableIndex, runnableCount);
		}

		return result;
	}
}
