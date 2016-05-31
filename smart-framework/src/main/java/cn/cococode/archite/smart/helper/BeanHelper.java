package cn.cococode.archite.smart.helper;

import cn.cococode.archite.smart.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by DELL on 2016-04-03.
 */
public final class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    //饿汉式初始化所有类的实例
    static {
        Set<Class<?>> beanSet = ClassHelper.getBeanClassSet();
        for (Class<?> clazz : beanSet){
            Object obj = ReflectionUtil.newInstance(clazz);
            BEAN_MAP.put(clazz, obj);
        }
    }

    public static Map<Class<?>, Object> getBeanMap(){
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> clazz){
        if (!BEAN_MAP.containsKey(clazz)){
            throw new RuntimeException("can not get bean by class + " + clazz);
        }
        return (T)BEAN_MAP.get(clazz);
    }
}
