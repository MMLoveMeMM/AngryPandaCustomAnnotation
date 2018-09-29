package com.liu.zhibao.angrypandacustomannotation;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zhibao.Liu on 2018/9/29.
 *
 * @version :
 * @date : 2018/9/29
 * @des :
 * @see{@link}
 */

public class ViewUtils {

    public static void injectContentView(Activity activity) {
        Class a = activity.getClass();
        if (a.isAnnotationPresent(ContentView.class)) {
            // 得到activity这个类的ContentView注解
            ContentView contentView = (ContentView) a.getAnnotation(ContentView.class);
            // 得到注解的值
            int layoutId = contentView.value();
            // 使用反射调用setContentView
            try {
                Method method = a.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(activity, layoutId);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void injectViews(Activity activity) {
        Class a = activity.getClass();
        // 得到activity所有字段
        Field[] fields = a.getDeclaredFields();
        // 得到被ViewInject注解的字段
        for (Field field : fields) {
            if (field.isAnnotationPresent(ViewInject.class)) {
                // 得到字段的ViewInject注解
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                // 得到注解的值
                int viewId = viewInject.value();
                // 使用反射调用findViewById，并为字段设置值
                try {
                    Method method = a.getMethod("findViewById", int.class);
                    method.setAccessible(true);
                    Object resView = method.invoke(activity, viewId);
                    field.setAccessible(true);
                    field.set(activity, resView);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void injectEvents(Activity activity) {
        Class a = activity.getClass();
        // 得到Activity的所有方法
        Method[] methods = a.getDeclaredMethods();
        for (Method method : methods) {
            // 得到被OnClick注解的方法
            if (method.isAnnotationPresent(OnClick.class)) {
                // 得到该方法的OnClick注解
                OnClick onClick = method.getAnnotation(OnClick.class);
                // 得到OnClick注解的值
                int[] viewIds = onClick.value();
                // 得到OnClick注解上的EventBase注解
                EventBase eventBase = onClick.annotationType().getAnnotation(EventBase.class);
                // 得到EventBase注解的值
                String listenerSetter = eventBase.listenerSetter();
                Class<?> listenerType = eventBase.listenerType();
                String methodName = eventBase.methodName();
                // 使用动态代理
                DynamicHandler handler = new DynamicHandler(activity);
                Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class<?>[]{listenerType}, handler);
                handler.addMethod(methodName, method);
                // 为每个view设置点击事件
                for (int viewId : viewIds) {
                    try {
                        Method findViewByIdMethod = a.getMethod("findViewById", int.class);
                        findViewByIdMethod.setAccessible(true);
                        View view = (View) findViewByIdMethod.invoke(activity, viewId);
                        Method setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        setEventListenerMethod.setAccessible(true);
                        setEventListenerMethod.invoke(view, listener);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }

}
