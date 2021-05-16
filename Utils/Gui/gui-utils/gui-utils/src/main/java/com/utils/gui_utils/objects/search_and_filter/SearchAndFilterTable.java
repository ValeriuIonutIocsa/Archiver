package com.utils.gui_utils.objects.search_and_filter;

import java.util.Collection;

import com.utils.annotations.ApiMethod;
import com.utils.string.regex.custom_patterns.CustomPatterns;

public interface SearchAndFilterTable {

	@ApiMethod
	void searchTable(
			int searchColumnIndex,
			CustomPatterns searchCustomPatterns);

	@ApiMethod
	void filterTable(
			FilterType filterType,
			int filterColumnIndex,
			CustomPatterns filterCustomPatterns);

	@ApiMethod
	Collection<String> getTableColumnNames();
}
