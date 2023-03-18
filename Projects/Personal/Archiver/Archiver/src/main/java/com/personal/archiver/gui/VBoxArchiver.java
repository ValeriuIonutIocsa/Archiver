package com.personal.archiver.gui;

import java.io.File;
import java.util.Deque;

import org.apache.commons.lang3.StringUtils;

import com.personal.archiver.gui.data.FileToArchive;
import com.personal.archiver.gui.workers.AbstractGuiWorkerCreateArchive;
import com.personal.archiver.gui.workers.CloseableGuiWorker;
import com.personal.archiver.gui.workers.GuiWorkerCreateArchiveCli;
import com.personal.archiver.gui.workers.GuiWorkerCreateArchiveGui;
import com.personal.archiver.gui.workers.GuiWorkerExtractHere;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.alerts.CustomAlertConfirm;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.ProgressIndicatorBar;
import com.utils.gui.objects.tables.tree_table.CustomTreeTableView;
import com.utils.gui.objects.tables.tree_table.UnfilteredTreeItem;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.log.progress.ProgressIndicator;
import com.utils.log.progress.ProgressIndicators;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

class VBoxArchiver extends AbstractCustomControl<VBox> {

	private static final String WORKING_DIR_PATH_STRING = PathUtils.computeAbsolutePath(null, null, "");

	private final ParserOutputFolderPaths parserOutputFolderPaths;
	private AbstractGuiWorkerCreateArchive abstractGuiWorkerCreateArchive;
	private GuiWorkerExtractHere guiWorkerExtractHere;

	private final CheckBox checkBoxCloseOnCompletion;
	private final TextField textFieldWorkingDirPath;
	private final CustomTreeTableView<FileToArchive> customTreeTableView;
	private final ComboBox<String> comboBoxOutputFolderPath;
	private final TextField textFieldOutputFileName;
	private final Button buttonCreateArchive;

	VBoxArchiver() {

		parserOutputFolderPaths = new ParserOutputFolderPaths();
		parserOutputFolderPaths.parse();

		checkBoxCloseOnCompletion =
				BasicControlsFactories.getInstance().createCheckBox("close on completion");
		checkBoxCloseOnCompletion.setSelected(true);

		textFieldWorkingDirPath = new TextField(WORKING_DIR_PATH_STRING);
		textFieldWorkingDirPath.setOnAction(event -> updateRootPath());

		customTreeTableView = createCustomTreeTableView();
		comboBoxOutputFolderPath = createComboBoxOutputFolderPath();
		textFieldOutputFileName = new TextField();
		buttonCreateArchive =
				BasicControlsFactories.getInstance().createButton("Create Archive");
		buttonCreateArchive.setOnAction(event -> createArchive());

		updateRootPath();
		setOutputFolderPaths();
	}

	private static ComboBox<String> createComboBoxOutputFolderPath() {

		final ComboBox<String> comboBoxOutputFolderPath = new ComboBox<>();
		comboBoxOutputFolderPath.setMinWidth(0);
		comboBoxOutputFolderPath.setMaxWidth(Double.POSITIVE_INFINITY);
		comboBoxOutputFolderPath.setEditable(true);
		return comboBoxOutputFolderPath;
	}

	private CustomTreeTableView<FileToArchive> createCustomTreeTableView() {

		final CustomTreeTableView<FileToArchive> customTreeTableView =
				new CustomTreeTableView<>(FileToArchive.COLUMNS, false, false, false, false, 0);
		customTreeTableView.setRowFactory(
				param -> new CustomTreeTableRowFileToArchive(customTreeTableView));
		customTreeTableView.getColumnList().get(0)
				.setCellFactory(param -> new CustomTreeTableCellFileToArchive(this));
		return customTreeTableView;
	}

	@Override
	protected VBox createRoot() {

		final VBox vBoxRoot = LayoutControlsFactories.getInstance().createVBox();

		final HBox hBoxTop = createHBoxTop();
		GuiUtils.addToVBox(vBoxRoot, hBoxTop,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 0, 7, 0);

		GuiUtils.addToVBox(vBoxRoot, customTreeTableView,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 7, 7, 7);

		final GridPane gridPaneBottom = createGridPaneBottom();
		GuiUtils.addToVBox(vBoxRoot, gridPaneBottom,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		return vBoxRoot;
	}

	private HBox createHBoxTop() {

		final HBox hBoxTop = LayoutControlsFactories.getInstance().createHBox();

		final Label labelRootPath =
				BasicControlsFactories.getInstance().createLabel("root path:", "bold");
		GuiUtils.addToHBox(hBoxTop, labelRootPath,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 10);

		GuiUtils.addToHBox(hBoxTop, textFieldWorkingDirPath,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 7, 0, 0);

		final Button buttonUpdateRootPath =
				BasicControlsFactories.getInstance().createButton("Update");
		buttonUpdateRootPath.setOnAction(event -> updateRootPath());
		GuiUtils.addToHBox(hBoxTop, buttonUpdateRootPath,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 0);

		GuiUtils.addToHBox(hBoxTop, checkBoxCloseOnCompletion,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		return hBoxTop;
	}

	private GridPane createGridPaneBottom() {

		final GridPane gridPaneBottom =
				LayoutControlsFactories.getInstance().createGridPane();
		int row = 0;

		final Label labelOutputFolderPath =
				BasicControlsFactories.getInstance().createLabel("output folder path:", "bold");
		GuiUtils.addToGridPane(gridPaneBottom, labelOutputFolderPath, 0, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 0, 7, 7, 10);

		GuiUtils.addToGridPane(gridPaneBottom, comboBoxOutputFolderPath, 1, row, 2, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.ALWAYS, 0, 7, 7, 0);
		row++;

		final Label labelOutputPath =
				BasicControlsFactories.getInstance().createLabel("output file name:", "bold");
		GuiUtils.addToGridPane(gridPaneBottom, labelOutputPath, 0, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 0, 7, 7, 10);

		textFieldOutputFileName.setOnAction(event -> createArchive());
		GuiUtils.addToGridPane(gridPaneBottom, textFieldOutputFileName, 1, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.ALWAYS, 0, 7, 7, 0);

		GuiUtils.addToGridPane(gridPaneBottom, buttonCreateArchive, 2, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 0, 7, 7, 0);
		row++;

		final ProgressIndicatorBar progressIndicatorBar = new ProgressIndicatorBar();
		final ProgressIndicator progressIndicator = new ProgressIndicatorImpl(progressIndicatorBar);
		ProgressIndicators.setInstance(progressIndicator);
		GuiUtils.addToGridPane(gridPaneBottom, progressIndicatorBar.getRoot(), 0, row, 3, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 0, 7, 7, 7);

		return gridPaneBottom;
	}

	private void updateRootPath() {

		String workingDirPathString = textFieldWorkingDirPath.getText();
		if (StringUtils.isBlank(workingDirPathString)) {
			workingDirPathString = ".";
		}
		workingDirPathString = PathUtils.computePath(workingDirPathString);
		if (workingDirPathString == null) {
			new CustomAlertError("error!", "failed to parse the working dir path").showAndWait();

		} else {
			final UnfilteredTreeItem<FileToArchive> unfilteredTreeItemRoot =
					customTreeTableView.getUnfilteredTreeItemRoot();
			final ParserFilesToArchive parserFilesToArchive =
					new ParserFilesToArchive(workingDirPathString, unfilteredTreeItemRoot);
			parserFilesToArchive.work();

			customTreeTableView.setFilteredItems();

			final FileToArchive fileToArchiveWorkingDir =
					parserFilesToArchive.getFileToArchiveWorkingDir();
			customTreeTableView.getRoot().setValue(fileToArchiveWorkingDir);

			comboBoxOutputFolderPath.getEditor().setText(workingDirPathString);
			textFieldOutputFileName.setText("custom_archive.zip");
		}
	}

	void createNewOutputPath(
			final FileToArchive fileToArchive) {

		final boolean folder = fileToArchive.isFolder();
		if (folder) {

			final String fileName = fileToArchive.getFileName();
			final String newOutputFileName = fileName + ".zip";
			textFieldOutputFileName.setText(newOutputFileName);
		}
	}

	private void createArchive() {

		final String outputFolderPathString = comboBoxOutputFolderPath.getEditor().getText();
		final String outputFileName = textFieldOutputFileName.getText();
		String outputPathString = outputFolderPathString + File.separator + outputFileName;

		boolean keepGoing = false;
		outputPathString = PathUtils.computePath(outputPathString);
		if (outputPathString == null) {
			new CustomAlertError("error!", "failed to parse the output path").showAndWait();

		} else {
			if (IoUtils.fileExists(outputPathString)) {

				final CustomAlertConfirm customAlertConfirm = new CustomAlertConfirm(
						"Output file already exists. Overwrite?",
						"A file already exists in the location:" + System.lineSeparator() +
								outputPathString + System.lineSeparator() +
								"Do you wish to overwrite the file with a newly created archive?",
						ButtonType.NO, ButtonType.YES);
				customAlertConfirm.showAndWait();
				final ButtonType buttonType = customAlertConfirm.getResult();
				if (buttonType == ButtonType.YES) {
					keepGoing = true;
				}

			} else {
				keepGoing = true;
			}
		}

		if (keepGoing) {

			String workingDirPathString = textFieldWorkingDirPath.getText();
			workingDirPathString = PathUtils.computePath(workingDirPathString);
			if (workingDirPathString == null) {
				new CustomAlertError("error!", "failed to parse the working dir path").showAndWait();

			} else {
				parserOutputFolderPaths.save(outputFolderPathString);
				setOutputFolderPaths();

				abstractGuiWorkerCreateArchive = new GuiWorkerCreateArchiveGui(
						getRoot().getScene(), this::setComponentsDisabled,
						workingDirPathString, outputPathString, customTreeTableView, checkBoxCloseOnCompletion);
				abstractGuiWorkerCreateArchive.start();
			}
		}
	}

	public void createArchive(
			final String folderPathString,
			final String outputPathString) {

		final String workingDirPathString = PathUtils.computePath(folderPathString);

		abstractGuiWorkerCreateArchive = new GuiWorkerCreateArchiveCli(
				getRoot().getScene(), this::setComponentsDisabled,
				workingDirPathString, outputPathString, folderPathString);
		abstractGuiWorkerCreateArchive.start();
	}

	private void setOutputFolderPaths() {

		comboBoxOutputFolderPath.getItems().clear();
		final Deque<String> outputFolderPathStrings = parserOutputFolderPaths.getOutputFolderPathStrings();
		comboBoxOutputFolderPath.getItems().addAll(outputFolderPathStrings);
	}

	void extractHere(
			final FileToArchive fileToArchive) {

		guiWorkerExtractHere = new GuiWorkerExtractHere(
				getRoot().getScene(), this::setComponentsDisabled,
				fileToArchive, checkBoxCloseOnCompletion);
		guiWorkerExtractHere.start();
	}

	private void setComponentsDisabled(
			final boolean b) {

		buttonCreateArchive.setDisable(b);
		customTreeTableView.setDisable(b);
	}

	void close() {

		closeWorker(abstractGuiWorkerCreateArchive);
		closeWorker(guiWorkerExtractHere);
	}

	private static void closeWorker(
			final CloseableGuiWorker closeableGuiWorker) {

		if (closeableGuiWorker != null && closeableGuiWorker.isAlive()) {
			closeableGuiWorker.close();
		}
	}

	public void configureFolderPathString(
			final String folderPathString) {

		textFieldWorkingDirPath.setText(folderPathString);
	}
}
