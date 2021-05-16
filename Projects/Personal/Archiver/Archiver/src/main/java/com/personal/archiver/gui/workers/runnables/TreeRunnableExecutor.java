package com.personal.archiver.gui.workers.runnables;

import static com.personal.archiver.gui.workers.CloseableGuiWorker.THREAD_COUNT;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TreeRunnableExecutor {

	private final TreeRunnable treeRunnableRoot;
	private final AtomicInteger runnableCount;

	public TreeRunnableExecutor(
			final TreeRunnable treeRunnableRoot,
			final AtomicInteger runnableCount) {

		this.treeRunnableRoot = treeRunnableRoot;
		this.runnableCount = runnableCount;
	}

	public void work() throws Exception {

		final LinkedBlockingQueue<TreeRunnable> linkedBlockingQueue = new LinkedBlockingQueue<>();
		linkedBlockingQueue.offer(treeRunnableRoot);

		final int threadCount = THREAD_COUNT;
		final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
		final int totalRunnableCount = runnableCount.get();
		final int updateProgressRate = totalRunnableCount / 1000 + 1;
		for (int i = 0; i < threadCount; i++) {
			new ThreadTreeRunnableExecutor(countDownLatch, linkedBlockingQueue,
					runnableCount, totalRunnableCount, updateProgressRate).start();
		}
		countDownLatch.await();
	}
}
