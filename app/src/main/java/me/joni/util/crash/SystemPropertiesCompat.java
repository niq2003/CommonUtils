package me.joni.util.crash;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by niqiang on 16/3/24.
 */
public class SystemPropertiesCompat {
    private static Class<?> sClass;

    private static Class getMyClass() throws ClassNotFoundException {
        if (sClass == null) {
            sClass = Class.forName("android.os.SystemProperties");
        }
        return sClass;
    }

    private static String getInner(String key, String defaultValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Class clazz = getMyClass();
        return (String) MethodUtils.invokeStaticMethod(clazz, "get", key, defaultValue);
    }

    public static String get(String key, String defaultValue) {
        try {
            return getInner(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
