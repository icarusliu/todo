package com.liuqi.tool.todo.ui.common;/**
 * Created by icaru on 2017/7/21.
 */

import javafx.beans.value.ObservableValueBase;

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
 * @Date 2017/7/21 8:36
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/21 8:36
 **/
public class BeanObservableValue <T, P> extends ObservableValueBase<T> {
    private P bean;
    private String property;

    public <T> BeanObservableValue(String property, P value) {
        this.bean = value;
        this.property = property;
    }

    @Override
    public T getValue() {
        if (null == bean) {
            return null;
        }

        //从BEAN中读取指定属性的值并返回
        Class<T> clazz = (Class<T>) bean.getClass();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor pd: pds) {
                String name = pd.getName();

                if (!name.equals(property)) {
                    continue;
                }

                Method method = pd.getReadMethod();
                Object value = method.invoke(bean);

                if (method.getReturnType().equals(String.class)  && null == value) {
                    //字符串时，如果返回值为空，则替换成空字符串
                    value = "";
                }

                return (T) value;
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
}
