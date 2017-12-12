package com.liuqi.learn.common;/**
 * Created by icaru on 2017/7/22.
 */

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/22 0:27
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/22 0:27
 **/
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, Enum> {
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        if (!targetType.isEnum()) {
            throw new UnsupportedOperationException("只支持转换到枚举类型");
        }
        return new StringToBaseEnumConverter(targetType);
    }

    private class StringToBaseEnumConverter<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToBaseEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String s) {
            for (T t : enumType.getEnumConstants()) {
                if (s.equals(t.toString())) {
                    return t;
                }
            }
            return null;
        }
    }
}
