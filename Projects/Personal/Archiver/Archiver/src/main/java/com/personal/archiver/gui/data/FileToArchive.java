package com.personal.archiver.gui.data;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class FileToArchive implements TableRowData, Comparable<FileToArchive> {

	private static final long serialVersionUID = -5500243744471361500L;

	public static final TableColumnData[] COLUMNS = {
			new TableColumnData("File Name", "FileName", 1)
	};

	@Override
	public DataItem<?>[] getTableViewData() {
		return new DataItem[] {
				FactoryDataItemObjectComparable.newInstance(fileName)
		};
	}

	private final String fileName;
	private final Path filePath;
	private final boolean folder;

	private boolean selected;
	private int selectedItemCount;
	private List<FileToArchive> childrenList;

	public FileToArchive(
			final String fileName,
			final Path filePath,
			final boolean folder) {

		this.fileName = fileName;
		this.filePath = filePath;
		this.folder = folder;
	}

	public void fillChildrenList() {

		childrenList = new ArrayList<>();
		try {
			final File file = filePath.toFile();
			final File[] files = file.listFiles();
			if (files != null) {

				for (final File fileChild : files) {

					final boolean directory = fileChild.isDirectory();
					final String fileChildName = fileChild.getName();
					final FileToArchive fileToArchive =
							new FileToArchive(fileChildName, fileChild.toPath(), directory);
					fileToArchive.setSelected(selected);
					childrenList.add(fileToArchive);
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to fill children list!");
			Logger.printException(exc);
		}
		childrenList.sort(null);
	}

	@Override
	public int compareTo(
			final FileToArchive other) {

		return new CompareToBuilder()
				.append(other.folder, folder)
				.append(fileName, other.fileName)
				.toComparison();
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(
			final Object obj) {

		final boolean result;
		if (getClass().isInstance(obj)) {
			result = compareTo(getClass().cast(obj)) == 0;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getFileName() {
		return fileName;
	}

	public Path getFilePath() {
		return filePath;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setSelected(
			final boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelectedItemCount(
			final int selectedItemCount) {
		this.selectedItemCount = selectedItemCount;
	}

	public int getSelectedItemCount() {
		return selectedItemCount;
	}

	public void setChildrenList(
            final List<FileToArchive> childrenList) {
		this.childrenList = childrenList;
	}

	public List<FileToArchive> getChildrenList() {
		return childrenList;
	}
}
