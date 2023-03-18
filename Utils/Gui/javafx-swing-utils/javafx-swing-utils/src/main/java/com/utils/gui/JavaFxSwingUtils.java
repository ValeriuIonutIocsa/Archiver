package com.utils.gui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.w3c.dom.Element;

import com.utils.annotations.ApiMethod;
import com.utils.html.HtmlUtils;
import com.utils.log.Logger;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public final class JavaFxSwingUtils {

	private JavaFxSwingUtils() {
	}

	@ApiMethod
	public static Element embedImageToHtml(
			final Element element,
			final Image image) {

		Element imgElement = null;
		try {
			final BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
			final byte[] imageBytes = byteArrayOutputStream.toByteArray();
			imgElement = HtmlUtils.embedImage(element, imageBytes);

		} catch (final Exception exc) {
			Logger.printError("failed to embed image to HTML");
			Logger.printException(exc);
		}
		return imgElement;
	}
}
