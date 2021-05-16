package com.utils.gui_utils.workers;

import java.time.Instant;

import com.utils.gui_utils.GuiUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.string.exc.SilentException;

import javafx.scene.Cursor;
import javafx.scene.Scene;

public abstract class GuiWorker extends Thread {

	private final Scene scene;
	private final ComponentDisabler componentDisabler;

	public GuiWorker(
			final Scene scene,
			final ComponentDisabler componentDisabler) {

		this.scene = scene;
		this.componentDisabler = componentDisabler;
	}

	@Override
	public void run() {

		final Instant start = Instant.now();
		try {
			GuiUtils.run(() -> setComponentsDisabled(true));
			work();

		} catch (final SilentException ignored) {
		} catch (final Exception exc) {
			Logger.printException(exc);
			try {
				error();
			} catch (final Exception exc2) {
				Logger.printException(exc2);
			}

		} finally {
			GuiUtils.run(() -> {

				try {
					finish();
					setComponentsDisabled(false);
					Logger.printFinishMessage(start);

				} catch (final Exception exc2) {
					Logger.printException(exc2);
				}
			});
		}
	}

	private void setComponentsDisabled(
			final boolean b) {

		if (scene != null) {

			final Cursor cursor;
			if (b) {
				cursor = Cursor.WAIT;
			} else {
				cursor = Cursor.DEFAULT;
			}
			scene.setCursor(cursor);
		}
		if (componentDisabler != null) {
			componentDisabler.setComponentsDisabled(b);
		}
	}

	protected abstract void work() throws Exception;

	protected abstract void error();

	protected abstract void finish();

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public Scene getScene() {
		return scene;
	}
}
