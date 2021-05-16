package com.utils.gui_utils.objects.list_select;

import java.util.Comparator;
import java.util.List;

import com.utils.data_types.table.TableColumnData;

class CustomListSelectionViewTestImpl extends CustomListSelectionViewOneToOne<Text, TextWithLength> {

	CustomListSelectionViewTestImpl(
			final List<Text> leftTableRowDataList,
			final List<TextWithLength> rightTableRowDataList) {
		super(0.45, leftTableRowDataList, rightTableRowDataList,
				Comparator.comparing(Text::getText),
				Comparator.comparing(textWithLength -> textWithLength.getText().length()));
	}

	@Override
	protected TableColumnData[] getTableColumnDataArrayLeft() {
		return Text.COLUMNS;
	}

	@Override
	protected TableColumnData[] getTableColumnDataArrayRight() {
		return TextWithLength.COLUMNS;
	}

	@Override
	protected TextWithLength convertLeftToRight(
			final Text leftItem) {
		return new TextWithLength(leftItem.getText());
	}

	@Override
	protected Text convertRightToLeft(
			final TextWithLength rightItem) {
		return new Text(rightItem.getText());
	}
}
