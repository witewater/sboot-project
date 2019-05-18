package cn.wendong.admin.core.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.core.exception.ResponseException;

/**
 * 封装Example对象，拦截部分不允许的搜索规则
 * @author MB
 * @date 2018-12-02
 */
public class MyExample {

    /**
     * 不允许搜索状态为删除的数据
     * @param probe 实体对象
     * @param matcher 过滤器
     */
    public static <T> Example<T> of(T probe, ExampleMatcher matcher) {
        // 通过反射机制获取实体对象的状态
        String variable = "status";
        Byte variableValue = StatusEnum.DELETE.getCode();
        try {
            final BeanInfo bi = Introspector.getBeanInfo(probe.getClass());
            for (final PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(variable)) {
                    final Object value = pd.getReadMethod().invoke(probe, (Object[]) null);
                    if(value != null){
                        Byte intValue = Byte.valueOf(String.valueOf(value));
                        if(variableValue.equals(intValue)){
                            throw new ResponseException(ResultEnum.STATUS_ERROR);
                        }
                    }
                }
            }
        } catch (InvocationTargetException | IntrospectionException | IllegalAccessException e) {
            String message = "Error reading property [" + variable + "] from principal of type [" + probe.getClass().getName() + "]";
            throw new IllegalArgumentException(message, e);
        }
        return Example.of(probe, matcher);
    }

}
