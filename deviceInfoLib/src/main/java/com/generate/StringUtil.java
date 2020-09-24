package com.generate;

import android.text.TextUtils;

/**
 * @author TAO
 * @desc
 * @since 2020/9/23
 */
public class StringUtil {

    public static String isEmptyText(String text) {
        if (null == text || TextUtils.isEmpty(text)) {
            return "";
        }
        return text;
    }


}
