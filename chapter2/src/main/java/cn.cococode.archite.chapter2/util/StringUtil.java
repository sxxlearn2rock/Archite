package cn.cococode.archite.chapter2.util;

/**
 * Created by DELL on 2016-03-25.
 */
public final class StringUtil {
    public static boolean isEmpty(String str){
        if (str != null){
            str = str.trim();
        }
        return str.isEmpty();
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
