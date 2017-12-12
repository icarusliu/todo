package com.liuqi.tool.todo.util;/**
 * Created by icaru on 2017/7/21.
 */

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/21 11:17
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/21 11:17
 **/
public class ReflectUtil {
    public static <T, P> P getBeanValue(T t, String property) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor prop : props) {
                if (!prop.getName().equals(property)) {
                    continue;
                }

                Method method = prop.getReadMethod();
                Object obj = method.invoke(t);

                return (P) obj;
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T, P> void setBeanValue(T t, String property, P value) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor prop : props) {
                if (!prop.getName().equals(property)) {
                    continue;
                }

                Method method = prop.getWriteMethod();
                method.invoke(t, value);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
