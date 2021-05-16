package com.personal.archiver.gui.workers;

public interface CloseableGuiWorker {

	int THREAD_COUNT = 16;

	void close();

	boolean isAlive();
}
