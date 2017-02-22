package com.rayman.interview.lib.tools;

import com.rayman.interview.lib.model.model.MatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static final String REGEX_UNIT_DEFINE = "([a-zA-Z]+)\\s+is\\s+([IVXLCDM])";
    public static final String REGEX_PRICE_DEFINE = "([A-Za-z\\s]*)\\sis\\s+([0-9]+)\\s+([c|C]redits)";
    public static final String REGEX_ROMAN_CONVERT = "how\\s+much\\s+is\\s+(([A-Za-z\\s])+)\\s+\\?";
    public static final String REGEX_PRICE_CALCULATE = "how\\s+many\\s+[c|C]redits\\s+is\\s([A-Za-z\\s]+)\\s+\\?";

    private final static String REGEX_VALIDATOR = "^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";

    public static boolean isEmpty(String s) {
        if (s == null || s.length() == 0 || s.trim().length() == 0)
            return true;
        return false;
    }

    static boolean isValideRoman(String s) {
        return s.matches(REGEX_VALIDATOR);
    }

    public static boolean isInteger(String input) {
        Matcher mer = Pattern.compile("^[0-9]+$").matcher(input);
        return mer.find();
    }

    public static MatchResult matchString(String input, String regex, int groupCount) {
        MatchResult matchResult = new MatchResult();
        if (StringUtil.isEmpty(input))
            return matchResult;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            List<String> result = new ArrayList<String>();
            for (int i = 1; i <= groupCount; i++) {
                result.add(matcher.group(i));
            }
            matchResult.setGroupList(result);
            matchResult.setMatch(true);
        } else {
            matchResult.setMatch(false);
        }
        return matchResult;
    }

}
