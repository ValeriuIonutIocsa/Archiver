package com.utils.gui_utils.objects.combo_box;

import java.util.List;
import java.util.Locale;

import com.utils.gui_utils.GuiUtils;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FilterComboBox<
		ObjectT> extends ComboBox<ObjectT> {

	public FilterComboBox(
			final String... styleClassElements) {

		setMinWidth(0);
		setMaxWidth(Double.POSITIVE_INFINITY);

		if (styleClassElements != null) {
			getStyleClass().addAll(styleClassElements);
		}

		final KeyHandler keyHandler = new KeyHandler();
		setOnKeyReleased(keyHandler);
		setOnMousePressed(mouseEvent -> keyHandler.clearText());
	}

	private class KeyHandler implements EventHandler<KeyEvent> {

		private String typedString;

		KeyHandler() {

			typedString = "";
		}

		@Override
		public void handle(
				final KeyEvent event) {

			final KeyCode keyCode = event.getCode();

			if (keyCode == KeyCode.DELETE) {
				clearText();

			} else if (keyCode == KeyCode.BACK_SPACE) {
				if (typedString.length() > 0) {
					typedString = typedString.substring(0, typedString.length() - 1);
				}

			} else if (keyCode != KeyCode.TAB) {
				typedString += event.getText();
			}

			if (typedString.length() == 0) {
				getSelectionModel().selectFirst();

			} else {
				final List<ObjectT> itemList = getItems();
				for (final ObjectT item : itemList) {

					final String displayString = item.toString().toLowerCase(Locale.US);
					if (displayString.startsWith(typedString)) {

						getSelectionModel().select(item);

						final ListView<?> listView =
								GuiUtils.getComboBoxListView(FilterComboBox.this);
						if (listView != null) {

							final List<?> listViewItemList = listView.getItems();
							final int itemIndex = listViewItemList.indexOf(item);
							listView.getFocusModel().focus(itemIndex);
							listView.scrollTo(itemIndex);
						}

						break;
					}
				}
			}
		}

		void clearText() {
			typedString = "";
		}
	}
}
