package com.kjung.springoauth.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtilsEx {
    public static String toStringOrNull(Object obj) {
        return obj != null ? String.valueOf(obj) : null;
    }
}
