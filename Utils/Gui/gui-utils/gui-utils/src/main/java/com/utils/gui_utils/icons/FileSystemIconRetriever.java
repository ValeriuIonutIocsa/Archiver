package com.utils.gui_utils.icons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import com.utils.annotations.ApiMethod;
import com.utils.string.StrUtils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class FileSystemIconRetriever {

	private final Path filePath;

	public FileSystemIconRetriever(
			final Path filePath) {

		this.filePath = filePath;
	}

	@ApiMethod
	public Image work() {

		Image image = null;
		final File file = filePath.toFile();
		final Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
		if (icon instanceof ImageIcon) {

			final BufferedImage bufferedImage;
			final ImageIcon imageIcon = (ImageIcon) icon;
			final java.awt.Image awtImage = imageIcon.getImage();
			if (awtImage instanceof BufferedImage) {
				bufferedImage = (BufferedImage) awtImage;

			} else {
				bufferedImage = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null),
						BufferedImage.TYPE_INT_ARGB);
				final Graphics2D graphics = bufferedImage.createGraphics();
				graphics.drawImage(awtImage, 0, 0, null);
				graphics.dispose();
			}
			image = SwingFXUtils.toFXImage(bufferedImage, null);
		}
		return image;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
