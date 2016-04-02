package cn.cococode.archite.smart.helper;

import cn.cococode.archite.smart.annotation.Controller;
import cn.cococode.archite.smart.annotation.Service;
import cn.cococode.archite.smart.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DELL on 2016-04-02.
 */
public final class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;
    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    //获取应用包名下面的所有类
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    //获取应用包名下面的所有service类
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> clazz : CLASS_SET){
            if (clazz.isAnnotationPresent(Service.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    //获取应用包名下面所有的Ctronller类
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> clazz : CLASS_SET){
            if (clazz.isAnnotationPresent(Controller.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    //获取应用包名下的所有Bean类(包括Service类、Contronller类等)
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        return beanClassSet;
    }
}
