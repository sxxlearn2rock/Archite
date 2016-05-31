package cn.cococode.archite.smart.helper;

import cn.cococode.archite.smart.annotation.Inject;
import cn.cococode.archite.smart.util.ArrayUtil;
import cn.cococode.archite.smart.util.CollectionUtil;
import cn.cococode.archite.smart.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by DELL on 2016-04-03.
 */
public final class IocHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
            //遍历beanMap
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()){
                //获取bean类与bean实例
                Class<?> beanClass = entry.getKey();
                Object beanInstance = entry.getValue();
                //获取bean类所有的成员变量
                Field[] fields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(fields)){
                    for (Field field : fields){
                        //查看当前字段是否带有Inject注解
                        if (field.isAnnotationPresent(Inject.class)){
                            Class<?> beanFieldClass = field.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null){
                                ReflectionUtil.setField(beanInstance, field, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
