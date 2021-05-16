package com.personal.archiver.gui.workers.runnables;

import java.util.ArrayList;
import java.util.List;

import com.utils.string.StrUtils;

public class TreeRunnable {

	private final Runnable runnable;
	private final List<TreeRunnable> childTreeRunnableList;

	public TreeRunnable(
			final Runnable runnable) {

		this.runnable = runnable;

		childTreeRunnableList = new ArrayList<>();
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	Runnable getRunnable() {
		return runnable;
	}

	public List<TreeRunnable> getChildTreeRunnableList() {
		return childTreeRunnableList;
	}
}
