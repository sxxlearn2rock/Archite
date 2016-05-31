package cn.cococode.archite.smart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by DELL on 2016-04-02.
 */
public class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public static Object newInstance(Class<?> clazz){
        Object instance = null;
        try {
            instance = clazz.newInstance();
        }catch (Exception e){
            LOGGER.error("new instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    //调用方法
    public static Object invokeMethod(Object obj, Method method, Object... args){
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        }catch (Exception e){
            LOGGER.error("invoke method failure", e);
            throw new RuntimeException(e);
        }
        return  result;
    }

    //设置成员变量的值
    public static void setField(Object obj, Field field, Object value){
        try {
            field.set(obj, value);
        }catch (Exception e){
            LOGGER.error("set field failure", e);
            throw new RuntimeException(e);
        }
    }
}
