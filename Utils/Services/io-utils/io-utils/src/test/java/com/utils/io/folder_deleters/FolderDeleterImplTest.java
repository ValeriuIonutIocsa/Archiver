package com.utils.io.folder_deleters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.utils.io.IoUtils;

class FolderDeleterImplTest {

	@Test
	void testDeleteFolder() {

		final Path folderPath = Paths.get("");
		assertTrue(IoUtils.directoryExists(folderPath));
		FolderDeleterImpl.INSTANCE.deleteFolder(folderPath, true);
		assertFalse(IoUtils.directoryExists(folderPath));
	}
}
