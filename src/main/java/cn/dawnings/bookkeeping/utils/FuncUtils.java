package cn.dawnings.bookkeeping.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * The type Function utils.
 *
 * @author fanchen.
 */
@Slf4j
public class FuncUtils {

    /**
     * 序列化过的Function接口.
     *
     * @param <T> <T>
     * @param <R> <R>
     * @author fanchen.
     */
    @FunctionalInterface
    public interface SFunction<T, R> extends Function<T, R>, Serializable {
    }

    /**
     * 验证参数是否非空.
     *
     * @param <T>    the type parameter
     * @param t      the t
     * @param params the params
     * @throws Exception the exception
     */
    @SafeVarargs
    public static <T> void parmaFiledNonEmpty(T t, SFunction<? super T, Object>... params) throws Exception {
        if (t == null) {
            throw new Exception("传入实例为null!");
        }
        if (null == params || params.length == 0) {
            return;
        }
        for (SFunction<? super T, Object> param : params) {
            String fieldName = "";
            try {
                //这里我们只是想要个名字,就不用再验证了
                fieldName = FuncUtils.getFieldName(param, false);
            } catch (Exception e) {
                log.error("获取字段属性名失败", e);
            }
            Object v = param.apply(t);
            if (v instanceof String) {
                if (StrUtil.isEmpty((String) v)) {
                    throw new Exception("参数\"" + fieldName + "\"缺失或不允许为空!");
                }
            } else {
                if (v == null) {
                    throw new Exception("参数\"" + fieldName + "\"缺失或不允许为空!");
                }
            }

        }
    }

    public static <T> String getFieldName(SFunction<T, ?> tsFunction) {
        try {
            return getFieldName(tsFunction, false);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取参数的名称.
     *
     * @param <T>        <T>
     * @param tsFunction 传入的字段lambda
     * @param needCheck  是否需要检查
     * @return field name
     * @throws NoSuchMethodException     no such method exception
     * @throws InvocationTargetException invocation target exception
     * @throws IllegalAccessException    illegal access exception
     */
    public static <T> String getFieldName(SFunction<T, ?> tsFunction, boolean needCheck) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        Method sWRMethod = tsFunction.getClass().getDeclaredMethod("writeReplace");
        boolean originalAccessStatus = sWRMethod.isAccessible();
        sWRMethod.setAccessible(true);
        SerializedLambda func = (SerializedLambda) sWRMethod.invoke(tsFunction);
        sWRMethod.setAccessible(originalAccessStatus);//最好设置回原状态,否则在哪里都可强制读了
        String methodName = func.getImplMethodName();
        if (!methodName.startsWith("get")) {
            return null;
        }
        String fieldName = methodName.substring(3);
        //一般属性名首字母是小写的,方法名去掉get后首字母转小写就是获得的属性名了
        String fChar = (fieldName.charAt(0) + "").toLowerCase();
        fieldName = fChar + fieldName.substring(1);
        if (needCheck)
        //如果有必要,再验证一下字段是否存在
        {
            Class.forName(func.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        }
        return fieldName;
    }


}
