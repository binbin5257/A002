//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lidroid.xutils;

import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.view.View;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.EventListenerManager;
import com.lidroid.xutils.view.ResLoader;
import com.lidroid.xutils.view.ViewFinder;
import com.lidroid.xutils.view.ViewInjectInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.PreferenceInject;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.EventBase;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewUtils {
    private ViewUtils() {
    }

    public static void inject(View view) {
        injectObject(view, new ViewFinder(view));
    }

    public static void inject(Activity activity) {
        injectObject(activity, new ViewFinder(activity));
    }
    public static void inject(Class<?> clazz,Activity activity) {
        injectObject(clazz,activity, new ViewFinder(activity));
    }
    public static void inject(Class<?> clazz,Object handler,View view) {
        injectObject(clazz,handler, new ViewFinder(view));
    }

    public static void inject(PreferenceActivity preferenceActivity) {
        injectObject(preferenceActivity, new ViewFinder(preferenceActivity));
    }

    public static void inject(Object handler, View view) {
        injectObject(handler, new ViewFinder(view));
    }

    public static void inject(Object handler, Activity activity) {
        injectObject(handler, new ViewFinder(activity));
    }

    public static void inject(Object handler, PreferenceGroup preferenceGroup) {
        injectObject(handler, new ViewFinder(preferenceGroup));
    }

    public static void inject(Object handler, PreferenceActivity preferenceActivity) {
        injectObject(handler, new ViewFinder(preferenceActivity));
    }

    private static void injectObject(Object handler, ViewFinder finder) {
        Class handlerType = handler.getClass();
        ContentView contentView = (ContentView)handlerType.getAnnotation(ContentView.class);
        if(contentView != null) {
            try {
                Method fields = handlerType.getMethod("setContentView", new Class[]{Integer.TYPE});
                fields.invoke(handler, new Object[]{Integer.valueOf(contentView.value())});
            } catch (Throwable var28) {
                LogUtils.e(var28.getMessage(), var28);
            }
        }

        Field[] var30 = handlerType.getDeclaredFields();
        int var7;
        if(var30 != null && var30.length > 0) {
            Field[] var8 = var30;
            var7 = var30.length;

            for(int method = 0; method < var7; ++method) {
                Field methods = var8[method];
                ViewInject viewInject = (ViewInject)methods.getAnnotation(ViewInject.class);
                if(viewInject != null) {
                    try {
                        View annotations = finder.findViewById(viewInject.value(), viewInject.parentId());
                        if(annotations != null) {
                            methods.setAccessible(true);
                            methods.set(handler, annotations);
                        }
                    } catch (Throwable var27) {
                        LogUtils.e(var27.getMessage(), var27);
                    }
                } else {
                    ResInject var35 = (ResInject)methods.getAnnotation(ResInject.class);
                    if(var35 != null) {
                        try {
                            Object annotation = ResLoader.loadRes(var35.type(), finder.getContext(), var35.id());
                            if(annotation != null) {
                                methods.setAccessible(true);
                                methods.set(handler, annotation);
                            }
                        } catch (Throwable var26) {
                            LogUtils.e(var26.getMessage(), var26);
                        }
                    } else {
                        PreferenceInject var37 = (PreferenceInject)methods.getAnnotation(PreferenceInject.class);
                        if(var37 != null) {
                            try {
                                Preference e = finder.findPreference(var37.value());
                                if(e != null) {
                                    methods.setAccessible(true);
                                    methods.set(handler, e);
                                }
                            } catch (Throwable var25) {
                                LogUtils.e(var25.getMessage(), var25);
                            }
                        }
                    }
                }
            }
        }

        Method[] var31 = handlerType.getDeclaredMethods();
        if(var31 != null && var31.length > 0) {
            Method[] var34 = var31;
            int var33 = var31.length;

            for(var7 = 0; var7 < var33; ++var7) {
                Method var32 = var34[var7];
                Annotation[] var36 = var32.getDeclaredAnnotations();
                if(var36 != null && var36.length > 0) {
                    Annotation[] var14 = var36;
                    int var13 = var36.length;

                    for(int var39 = 0; var39 < var13; ++var39) {
                        Annotation var38 = var14[var39];
                        Class annType = var38.annotationType();
                        if(annType.getAnnotation(EventBase.class) != null) {
                            var32.setAccessible(true);

                            try {
                                Method e1 = annType.getDeclaredMethod("value", new Class[0]);
                                Method parentIdMethod = null;

                                try {
                                    parentIdMethod = annType.getDeclaredMethod("parentId", new Class[0]);
                                } catch (Throwable var24) {
                                    ;
                                }

                                Object values = e1.invoke(var38, new Object[0]);
                                Object parentIds = parentIdMethod == null?null:parentIdMethod.invoke(var38, new Object[0]);
                                int parentIdsLen = parentIds == null?0:Array.getLength(parentIds);
                                int len = Array.getLength(values);

                                for(int i = 0; i < len; ++i) {
                                    ViewInjectInfo info = new ViewInjectInfo();
                                    info.value = Array.get(values, i);
                                    info.parentId = parentIdsLen > i?((Integer)Array.get(parentIds, i)).intValue():0;
                                    EventListenerManager.addEventMethod(finder, info, var38, handler, var32);
                                }
                            } catch (Throwable var29) {
                                LogUtils.e(var29.getMessage(), var29);
                            }
                        }
                    }
                }
            }
        }

    }
    private static void injectObject(Class<?> h,Object handler, ViewFinder finder) {
        Class handlerType = h;
        ContentView contentView = (ContentView)handlerType.getAnnotation(ContentView.class);
        if(contentView != null) {
            try {
                Method fields = handlerType.getMethod("setContentView", new Class[]{Integer.TYPE});
                fields.invoke(handler, new Object[]{Integer.valueOf(contentView.value())});
            } catch (Throwable var28) {
                LogUtils.e(var28.getMessage(), var28);
            }
        }

        Field[] var30 = handlerType.getDeclaredFields();
        int var7;
        if(var30 != null && var30.length > 0) {
            Field[] var8 = var30;
            var7 = var30.length;

            for(int method = 0; method < var7; ++method) {
                Field methods = var8[method];
                ViewInject viewInject = (ViewInject)methods.getAnnotation(ViewInject.class);
                if(viewInject != null) {
                    try {
                        View annotations = finder.findViewById(viewInject.value(), viewInject.parentId());
                        if(annotations != null) {
                            methods.setAccessible(true);
                            methods.set(handler, annotations);
                        }
                    } catch (Throwable var27) {
                        LogUtils.e(var27.getMessage(), var27);
                    }
                } else {
                    ResInject var35 = (ResInject)methods.getAnnotation(ResInject.class);
                    if(var35 != null) {
                        try {
                            Object annotation = ResLoader.loadRes(var35.type(), finder.getContext(), var35.id());
                            if(annotation != null) {
                                methods.setAccessible(true);
                                methods.set(handler, annotation);
                            }
                        } catch (Throwable var26) {
                            LogUtils.e(var26.getMessage(), var26);
                        }
                    } else {
                        PreferenceInject var37 = (PreferenceInject)methods.getAnnotation(PreferenceInject.class);
                        if(var37 != null) {
                            try {
                                Preference e = finder.findPreference(var37.value());
                                if(e != null) {
                                    methods.setAccessible(true);
                                    methods.set(handler, e);
                                }
                            } catch (Throwable var25) {
                                LogUtils.e(var25.getMessage(), var25);
                            }
                        }
                    }
                }
            }
        }

        Method[] var31 = handlerType.getDeclaredMethods();
        if(var31 != null && var31.length > 0) {
            Method[] var34 = var31;
            int var33 = var31.length;

            for(var7 = 0; var7 < var33; ++var7) {
                Method var32 = var34[var7];
                Annotation[] var36 = var32.getDeclaredAnnotations();
                if(var36 != null && var36.length > 0) {
                    Annotation[] var14 = var36;
                    int var13 = var36.length;

                    for(int var39 = 0; var39 < var13; ++var39) {
                        Annotation var38 = var14[var39];
                        Class annType = var38.annotationType();
                        if(annType.getAnnotation(EventBase.class) != null) {
                            var32.setAccessible(true);

                            try {
                                Method e1 = annType.getDeclaredMethod("value", new Class[0]);
                                Method parentIdMethod = null;

                                try {
                                    parentIdMethod = annType.getDeclaredMethod("parentId", new Class[0]);
                                } catch (Throwable var24) {
                                    ;
                                }

                                Object values = e1.invoke(var38, new Object[0]);
                                Object parentIds = parentIdMethod == null?null:parentIdMethod.invoke(var38, new Object[0]);
                                int parentIdsLen = parentIds == null?0:Array.getLength(parentIds);
                                int len = Array.getLength(values);

                                for(int i = 0; i < len; ++i) {
                                    ViewInjectInfo info = new ViewInjectInfo();
                                    info.value = Array.get(values, i);
                                    info.parentId = parentIdsLen > i?((Integer)Array.get(parentIds, i)).intValue():0;
                                    EventListenerManager.addEventMethod(finder, info, var38, handler, var32);
                                }
                            } catch (Throwable var29) {
                                LogUtils.e(var29.getMessage(), var29);
                            }
                        }
                    }
                }
            }
        }

    }
}
