package com.taccotap.taigidict.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class TailoNumberToneUtils {
    private static final String TAG = TailoNumberToneUtils.class.getSimpleName();

    private static Pattern sTailoWordPattern = Pattern.compile("(?:(ph|p|m|b|th|tsh|ts|t|n|l|kh|k|ng|g|h|s|j)?([aiueo+]+(?:nn)?|ng|m)(?:(ng|m|n)|(p|t|h|k))?([1-9])?|(ph|p|m|b|th|tsh|ts|t|n|l|kh|k|ng|g|h|s|j)-?-?)", Pattern.CASE_INSENSITIVE);

    private static String[] sTonedLomajiLowerCaseO = {"o", "ó", "ò", "o", "ô", "ó", "ō", "o̍"};
    private static String[] sTonedLomajiLowerCaseA = {"a", "á", "à", "a", "â", "á", "ā", "a̍"};
    private static String[] sTonedLomajiLowerCaseE = {"e", "é", "è", "e", "ê", "é", "ē", "e̍"};
    private static String[] sTonedLomajiLowerCaseU = {"u", "ú", "ù", "u", "û", "ú", "ū", "u̍"};
    private static String[] sTonedLomajiLowerCaseI = {"i", "í", "ì", "i", "î", "í", "ī", "i̍"};
    private static String[] sTonedLomajiLowerCaseN = {"n", "ń", "ǹ", "n", "n̂", "ń", "n̄", "n̍"};
    private static String[] sTonedLomajiLowerCaseM = {"m", "ḿ", "m̀", "m", "m̂", "ḿ", "m̄", "m̍"};

    private static String[] sTonedLomajiUpperCaseO = {"O", "Ó", "Ò", "O", "Ô", "Ó", "Ō", "O̍"};
    private static String[] sTonedLomajiUpperCaseA = {"A", "Á", "À", "A", "Â", "Á", "Ā", "A̍"};
    private static String[] sTonedLomajiUpperCaseE = {"E", "É", "È", "E", "Ê", "É", "Ē", "E̍"};
    private static String[] sTonedLomajiUpperCaseU = {"U", "Ú", "Ù", "U", "Û", "Ú", "Ū", "U̍"};
    private static String[] sTonedLomajiUpperCaseI = {"I", "Í", "Ì", "I", "Î", "Í", "Ī", "I̍"};
    private static String[] sTonedLomajiUpperCaseN = {"N", "Ń", "Ǹ", "N", "N̂", "Ń", "N̄", "N̍"};
    private static String[] sTonedLomajiUpperCaseM = {"M", "Ḿ", "M̀", "M", "M̂", "Ḿ", "M̄", "M̍"};

    private static String[] sTonedTailoLowerCase9 = {"ő", "a̋", "e̋", "ű", "i̋", "n̋", "m̋"};
    private static String[] sTonedTailoUpperCase9 = {"Ő", "A̋", "E̋", "Ű", "I̋", "N̋", "M̋"};
    private static String[] sTonedPojLowerCase9 = {"ŏ", "ă", "ĕ", "ŭ", "ĭ", "n̆", "m̆"};
    private static String[] sTonedPojUpperCase9 = {"Ŏ", "Ă", "Ĕ", "Ŭ", "Ĭ", "N̆", "M̆"};

    // [o, a , e ,u, i, n, m]
    public static int[] sLomajiNumberToWordTempArray = {0, 0, 0, 0, 0, 0, 0};

    public static void resetTempArray() {
        int count = sLomajiNumberToWordTempArray.length;
        for (int i = 0; i < count; i++) {
            sLomajiNumberToWordTempArray[i] = 0;
        }
    }

    public static String numberTone2LomaijiTone(String input) {
        if (!input.matches(".*\\d+.*")) {
            return input;
        }

        final Matcher matcher = sTailoWordPattern.matcher(input);

        int groupCount = matcher.groupCount();
        if (groupCount == 0) {
            return input;
        }

        StringBuilder stringBuilder = new StringBuilder();
        boolean isMatcherFound = false;
        while (matcher.find()) {
            final String foundTaigiWord = matcher.group();
            final String lomaji = parseNumberToneWordToLomaji(foundTaigiWord);
            stringBuilder.append(lomaji);

            stringBuilder.append("-");

            isMatcherFound = true;
        }
        if (isMatcherFound) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    private static String parseNumberToneWordToLomaji(String numberToneWord) {
        final int numberToneIndex = findCorrectNumberToneIndex(numberToneWord);
        if (numberToneIndex == -1) {
            return numberToneWord;
        }

        char foundNumberChar = numberToneWord.charAt(numberToneIndex);
        int numberTone = Character.getNumericValue(foundNumberChar);

        String correctNumberToneWord = numberToneWord.substring(0, numberToneIndex + 1);

        resetTempArray();

        // calculate tone position
        int count = correctNumberToneWord.length();
        for (int i = 0; i < count; i++) {
            final char c = correctNumberToneWord.charAt(i);

            switch (c) {
                case 'o':
                case 'O':
                    sLomajiNumberToWordTempArray[0] = i + 1;
                    break;
                case 'a':
                case 'A':
                    sLomajiNumberToWordTempArray[1] = i + 1;
                    break;
                case 'e':
                case 'E':
                    sLomajiNumberToWordTempArray[2] = i + 1;
                    break;
                case 'u':
                case 'U':
                    sLomajiNumberToWordTempArray[3] = i + 1;
                    break;
                case 'i':
                case 'I':
                    sLomajiNumberToWordTempArray[4] = i + 1;
                    break;
                case 'n':
                case 'N':
                    sLomajiNumberToWordTempArray[5] = i + 1;
                    break;
                case 'm':
                case 'M':
                    sLomajiNumberToWordTempArray[6] = i + 1;
                    break;
            }
        }
        int foundToneCharPosition = -1;
        int size = sLomajiNumberToWordTempArray.length;
        for (int i = 0; i < size; i++) {
            final int pos = sLomajiNumberToWordTempArray[i];
            if (pos > 0) {
                foundToneCharPosition = sLomajiNumberToWordTempArray[i] - 1;
                break;
            }
        }

        if (foundToneCharPosition != -1) {
            foundToneCharPosition = findCorrectToneCharPosition(correctNumberToneWord, foundToneCharPosition);
            char toneChar = correctNumberToneWord.charAt(foundToneCharPosition);

            String tonedLomaji = getTonedLomaji(toneChar, numberTone);
            return correctNumberToneWord.substring(0, foundToneCharPosition) + tonedLomaji + correctNumberToneWord.substring(foundToneCharPosition + 1, correctNumberToneWord.length() - 1);
        } else {
            return numberToneWord;
        }
    }

    // fix tailo's oo: oó -> óo
    private static int findCorrectToneCharPosition(String correctNumberToneWord, int foundToneCharPosition) {
        final char foundToneChar = correctNumberToneWord.charAt(foundToneCharPosition);

        if (foundToneChar == 'o' || foundToneChar == 'O') {
            if (foundToneCharPosition - 1 >= 0) {
                final char beforeFoundToneChar = correctNumberToneWord.charAt(foundToneCharPosition - 1);
                if (beforeFoundToneChar == 'o' || beforeFoundToneChar == 'O') {
                    return foundToneCharPosition - 1;
                }
            }
        }

        return foundToneCharPosition;
    }

    // ex: handle tai5566 -> tai5
    private static int findCorrectNumberToneIndex(String numberToneWord) {
        int foundNumberIndex = -1;

        final int count = numberToneWord.length();
        for (int i = count - 1; i >= 0; i--) {
            char c = numberToneWord.charAt(i);
            if (Character.isDigit(c)) {
                foundNumberIndex = i;
            } else {
                break;
            }
        }

        return foundNumberIndex;
    }

    private static String getTonedLomaji(char lomajiChar, int numberTone) {
        String tonedLomaji = "";

        if (numberTone != 9) {
            switch (lomajiChar) {
                case 'o':
                    tonedLomaji = sTonedLomajiLowerCaseO[numberTone - 1];
                    break;
                case 'a':
                    tonedLomaji = sTonedLomajiLowerCaseA[numberTone - 1];
                    break;
                case 'e':
                    tonedLomaji = sTonedLomajiLowerCaseE[numberTone - 1];
                    break;
                case 'u':
                    tonedLomaji = sTonedLomajiLowerCaseU[numberTone - 1];
                    break;
                case 'i':
                    tonedLomaji = sTonedLomajiLowerCaseI[numberTone - 1];
                    break;
                case 'n':
                    tonedLomaji = sTonedLomajiLowerCaseN[numberTone - 1];
                    break;
                case 'm':
                    tonedLomaji = sTonedLomajiLowerCaseM[numberTone - 1];
                    break;

                case 'O':
                    tonedLomaji = sTonedLomajiUpperCaseO[numberTone - 1];
                    break;
                case 'A':
                    tonedLomaji = sTonedLomajiUpperCaseA[numberTone - 1];
                    break;
                case 'E':
                    tonedLomaji = sTonedLomajiUpperCaseE[numberTone - 1];
                    break;
                case 'U':
                    tonedLomaji = sTonedLomajiUpperCaseU[numberTone - 1];
                    break;
                case 'I':
                    tonedLomaji = sTonedLomajiUpperCaseI[numberTone - 1];
                    break;
                case 'N':
                    tonedLomaji = sTonedLomajiUpperCaseN[numberTone - 1];
                    break;
                case 'M':
                    tonedLomaji = sTonedLomajiUpperCaseM[numberTone - 1];
                    break;
            }
        } else {
            switch (lomajiChar) {
                case 'o':
                    tonedLomaji = sTonedPojLowerCase9[0];
                    break;
                case 'a':
                    tonedLomaji = sTonedPojLowerCase9[1];
                    break;
                case 'e':
                    tonedLomaji = sTonedPojLowerCase9[2];
                    break;
                case 'u':
                    tonedLomaji = sTonedPojLowerCase9[3];
                    break;
                case 'i':
                    tonedLomaji = sTonedPojLowerCase9[4];
                    break;
                case 'n':
                    tonedLomaji = sTonedPojLowerCase9[5];
                    break;
                case 'm':
                    tonedLomaji = sTonedPojLowerCase9[6];
                    break;

                case 'O':
                    tonedLomaji = sTonedPojUpperCase9[0];
                    break;
                case 'A':
                    tonedLomaji = sTonedPojUpperCase9[1];
                    break;
                case 'E':
                    tonedLomaji = sTonedPojUpperCase9[2];
                    break;
                case 'U':
                    tonedLomaji = sTonedPojUpperCase9[3];
                    break;
                case 'I':
                    tonedLomaji = sTonedPojUpperCase9[4];
                    break;
                case 'N':
                    tonedLomaji = sTonedPojUpperCase9[5];
                    break;
                case 'M':
                    tonedLomaji = sTonedPojUpperCase9[6];
                    break;
            }
        }

        return tonedLomaji;
    }
}
