package com.personal.archiver.gui.workers.runnables;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicators;
import com.utils.string.StrUtils;

class ThreadTreeRunnableExecutor extends Thread {

	private final CountDownLatch countDownLatch;
	private final LinkedBlockingQueue<TreeRunnable> linkedBlockingQueue;
	private final AtomicInteger runnableCount;
	private final int totalRunnableCount;
	private final int updateProgressRate;

	ThreadTreeRunnableExecutor(
			final CountDownLatch countDownLatch,
			final LinkedBlockingQueue<TreeRunnable> linkedBlockingQueue,
			final AtomicInteger runnableCount,
			final int totalRunnableCount,
			final int updateProgressRate) {

		this.countDownLatch = countDownLatch;
		this.linkedBlockingQueue = linkedBlockingQueue;
		this.runnableCount = runnableCount;
		this.totalRunnableCount = totalRunnableCount;
		this.updateProgressRate = updateProgressRate;
	}

	@Override
	public void run() {

		while (true) {

			final int runnableCountValue = runnableCount.get();
			if (runnableCountValue == 0) {
				countDownLatch.countDown();
				break;

			} else {
				try {
					final TreeRunnable treeRunnable =
							linkedBlockingQueue.poll(100, TimeUnit.MILLISECONDS);
					if (treeRunnable != null) {

						final Runnable runnable = treeRunnable.getRunnable();
						if (runnable != null) {

							runnable.run();
							decrementItemCount();
						}

						final List<TreeRunnable> childTreeRunnableList =
								treeRunnable.getChildTreeRunnableList();
						for (final TreeRunnable childTreeRunnable : childTreeRunnableList) {
							linkedBlockingQueue.offer(childTreeRunnable);
						}
					}

				} catch (final Exception exc) {
					Logger.printException(exc);
				}
			}
		}
	}

	private void decrementItemCount() {

		final int runnableCountValue = runnableCount.getAndDecrement();
		final int finishedRunnableCount = totalRunnableCount - runnableCountValue;
		if (finishedRunnableCount % updateProgressRate == 0) {
			ProgressIndicators.getInstance().update(finishedRunnableCount, totalRunnableCount);
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
