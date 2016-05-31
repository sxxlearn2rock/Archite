package cn.cococode.archite.smart.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by DELL on 2016-04-03.
 */
public final class ArrayUtil {
    public static boolean isEmpty(Object[] array){
        return ArrayUtils.isEmpty(array);
    }

    public static boolean isNotEmpty(Object[] array){
        return !ArrayUtils.isEmpty(array);
    }
}
