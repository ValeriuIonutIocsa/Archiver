package com.utils.string.regex.glob;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.utils.string.regex.custom_patterns.patterns.CustomPatternGlobRegex;

public final class GlobRegexMatcher {

	private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\R");
	private static final Pattern WHITE_SPACE_PATTERN = Pattern.compile("\\s+");

	private GlobRegexMatcher() {
	}

	public static List<String[]> parseFilterPatterns(
			final String filterPatternsText) {

		List<String[]> patterns = null;
		if (StringUtils.isNotBlank(filterPatternsText)) {

			patterns = new ArrayList<>();
			final String[] linesSplit = NEW_LINE_PATTERN.split(filterPatternsText, -1);
			for (final String patternLine : linesSplit) {

				if (!isEmptyPattern(patternLine)) {
					patterns.add(WHITE_SPACE_PATTERN.split(patternLine, -1));
				}
			}
		}
		return patterns;
	}

	private static boolean isEmptyPattern(
			final String patternParam) {

		String pattern = patternParam;
		pattern = pattern.trim();
		return pattern.isEmpty() || "*".equals(pattern);
	}

	public static boolean matchesPatterns(
			final String text,
			final List<String[]> patterns,
			final boolean caseSensitive) {

		boolean matches = false;
		if (text != null) {

			if (patterns == null) {
				matches = true;

			} else {
				for (final String[] andPatterns : patterns) {

					if (matchesAndPatterns(text, andPatterns, caseSensitive)) {
						matches = true;
						break;
					}
				}
			}
		}
		return matches;
	}

	private static boolean matchesAndPatterns(
			final String text,
			final String[] andPatterns,
			final boolean caseSensitive) {

		boolean matches = true;
		for (final String pattern : andPatterns) {

			if (!matchesPattern(text, pattern, caseSensitive)) {
				matches = false;
				break;
			}
		}
		return matches;
	}

	public static boolean matchesPattern(
			final String text,
			final String pattern,
			final boolean caseSensitive) {

		final CustomPatternGlobRegex patternGlobRegex = new CustomPatternGlobRegex(pattern, false, caseSensitive);
		return patternGlobRegex.checkMatches(text);
	}
}
