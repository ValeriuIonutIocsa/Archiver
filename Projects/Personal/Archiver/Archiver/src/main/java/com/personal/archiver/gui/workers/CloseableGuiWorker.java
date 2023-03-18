package com.personal.archiver.gui.workers;

import com.utils.gui.workers.GuiWorker;

public interface CloseableGuiWorker extends GuiWorker {

	int THREAD_COUNT = 16;

	void close();

	boolean isAlive();
}
