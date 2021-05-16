package com.utils.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.string.env.EnvUtils;

class FileLockerTest {

	private static final Path LOCK_FILE_PATH =
			Paths.get(EnvUtils.getEnv("TEMP"), "FileLockerTest", "lock_file.txt");

	@Test
	void testLock() throws Exception {

		final FileLocker fileLocker = new FileLocker("JUnit test", LOCK_FILE_PATH);
		final boolean success = fileLocker.lock();
		Logger.printLine(success);

		Thread.sleep(10_000);
	}

	@Test
	void testLockSequence() {

		final FileLocker fileLocker = new FileLocker("JUnit test", LOCK_FILE_PATH);

		final boolean firstTimeSuccess = fileLocker.lock();
		assertTrue(firstTimeSuccess);

		final boolean secondTimeSuccess = fileLocker.lock();
		assertFalse(secondTimeSuccess);

		fileLocker.unlock();

		final boolean thirdTimeSuccess = fileLocker.lock();
		assertTrue(thirdTimeSuccess);

		fileLocker.unlock();
	}
}
