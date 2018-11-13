package cn.lds.im.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

import cn.lds.chat.R;


public class MyActivityFragment extends Fragment {
    protected View view;

    protected String clzzname = MyActivityFragment.class.getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        System.out.println(clzzname + "____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(clzzname + "____onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println(clzzname + "____onCreateView");
        ViewUtils.inject(MyActivityFragment.class, this, view);
        return inflater.inflate(R.layout.fragment_my_activity, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println(clzzname + "____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println(clzzname + "____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(clzzname + "____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println(clzzname + "____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println(clzzname + "____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println(clzzname + "____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println(clzzname + "____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println(clzzname + "____onDetach");
    }

    public void show(FragmentManager manager, MyActivityFragment fragment) {
        if (null != manager.getFragments() && manager.getFragments().size() > 0) {
            if (!manager.getFragments().contains(fragment)) {
                for (Fragment tempFragment : manager.getFragments()) {
                    manager.beginTransaction().hide(tempFragment).commit();
                }
                manager.beginTransaction().add(R.id.frame_m, fragment).commit();
            } else {
                for (Fragment tempFragment : manager.getFragments()) {
                    if (this.getClass().getName().equals(tempFragment.getClass().getName())) {
                        manager.beginTransaction().show(fragment).commit();
                    } else {
                        manager.beginTransaction().hide(tempFragment).commit();
                    }
                }
            }
        } else {
            manager.beginTransaction().add(R.id.frame_m, fragment).commit();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent == null || intent.getComponent() == null) {
            super.startActivity(intent);
        } else {
            String className = intent.getComponent().getClassName();
            String activityName = "";
            int length = 0;
            if (!TextUtils.isEmpty(className)) {
                if (className.contains(".")) {
                    length = className.split("\\.").length;
                    if (length > 0) {
                        activityName = className.split("\\.")[length - 1];
                    }
                    activityName = "cn.lds.im.view.appview.App" + activityName;
                }
            }
            try {
                Class<?> c = (Class<?>) Class.forName(activityName);
                if (c == null) {
                    super.startActivity(intent);
                } else {
                    intent.setClassName(intent.getComponent().getPackageName(), activityName);
                    super.startActivity(intent);
                }
            } catch (ClassNotFoundException e) {
//                LogHelper.e("startActivity", e);
                super.startActivity(intent);
            }
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null || intent.getComponent() == null) {
            super.startActivityForResult(intent, requestCode);
        } else {
            String className = intent.getComponent().getClassName();
            String activityName = "";
            int length = 0;
            if (!TextUtils.isEmpty(className)) {
                if (className.contains(".")) {
                    length = className.split("\\.").length;
                    if (length > 0) {
                        activityName = className.split("\\.")[length - 1];
                    }
                    activityName = "cn.lds.im.view.appview.App" + activityName;
                }
            }
            try {
                Class<?> c = (Class<?>) Class.forName(activityName);
                if (c == null) {
                    super.startActivityForResult(intent, requestCode);
                } else {
                    intent.setClassName(intent.getComponent().getPackageName(), activityName);
                    super.startActivityForResult(intent, requestCode);
                }
            } catch (ClassNotFoundException e) {
//                LogHelper.e("startActivity", e);
                super.startActivityForResult(intent, requestCode);
            }
        }
    }
}