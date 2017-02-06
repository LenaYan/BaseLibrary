package com.rayman.interview.lib.tools;

import com.rayman.interview.lib.model.model.RomanNumber;

public class ConvertUtil {

    public static int convertRomanToNumber(String s) {
        int result = 0;
        if (StringUtil.isEmpty(s) || !StringUtil.isValideRoman(s))
            return result;

        char[] romanChars = s.toCharArray();
        int length = romanChars.length;
        int last = -1;
        for (int i = length - 1; i >= 0; i--) {
            int value = convertCharToNumber(romanChars[i]);
            result += last != -1 && isReverse(value, last) ? -value : value;
            last = value;
        }
        return result;
    }

    public static int convertCharToNumber(char roman) {
        int value = -1;
        switch (roman) {
            case 'I':
                value = RomanNumber.I.getValue();
                break;
            case 'V':
                value = RomanNumber.V.getValue();
                break;
            case 'X':
                value = RomanNumber.X.getValue();
                break;
            case 'L':
                value = RomanNumber.L.getValue();
                break;
            case 'C':
                value = RomanNumber.C.getValue();
                break;
            case 'D':
                value = RomanNumber.D.getValue();
                break;
            case 'M':
                value = RomanNumber.M.getValue();
                break;
        }
        return value;
    }

    public static RomanNumber getRomanFromValue(char value) {
        switch (value) {
            case 'I':
                return RomanNumber.I;
            case 'V':
                return RomanNumber.V;
            case 'X':
                return RomanNumber.X;
            case 'L':
                return RomanNumber.L;
            case 'C':
                return RomanNumber.C;
            case 'D':
                return RomanNumber.D;
            case 'M':
                return RomanNumber.M;
        }
        return null;
    }

    private static boolean isReverse(int current, int last) {
        return current < last;
    }
}
