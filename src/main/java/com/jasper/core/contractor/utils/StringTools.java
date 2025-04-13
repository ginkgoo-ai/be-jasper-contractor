/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.utils;

/**
 * String Tools
 */
public class StringTools {
    private static final String PERCENT = "%";

    private StringTools() {

    }

    public static String firstCharToLowerCase(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String likePattern(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("The keyword cannot be null");
        }
        if (keyword.startsWith(PERCENT) || keyword.endsWith(PERCENT)) {
            return keyword;
        } else {
            return PERCENT + keyword + PERCENT;
        }

    }

    public static String getPluralName(String word) {
        if (word == null || word.length() == 0) {
            return word;
        }
        if (word.endsWith("y")) {
            return word.substring(0, word.length() - 1) + "ies";
        }
        if (word.endsWith("s") || word.endsWith("x")) {
            return word.substring(0, word.length() - 1) + "es";
        }
        if (word.endsWith("ch") || word.endsWith("sh")) {
            return word.substring(0, word.length() - 2) + "es";
        }

        if (word.endsWith("f")) {
            return word.substring(0, word.length() - 1) + "ves";
        }
        if (word.endsWith("fe")) {
            return word.substring(0, word.length() - 2) + "ves";
        }
        return word + "s";
    }

    public static String splitWords(String words, String joinString) {
        if (words == null || words.length() == 0) {
            return words;
        }
        words = firstCharToLowerCase(words);
        return words.replaceAll("[A-Z]", joinString + "$0").toLowerCase();
    }
}
