//package cn.lds.chatcore.view.widget.popupwindow;
//
//import android.content.Context;
//import android.graphics.drawable.ColorDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//
//import java.util.ArrayList;
//
//import cn.lds.chatcore.MyApplication;
//import cn.lds.chatcore.R;
//import cn.lds.chatcore.common.Constants;
//import cn.lds.chatcore.common.CoreHttpApi;
//import cn.lds.chatcore.common.LoadingDialog;
//import cn.lds.chatcore.common.ToolsHelper;
//import cn.lds.chatcore.common.WebViewActivityHelper;
//import cn.lds.chatcore.data.PublicMenuInfoModel;
//
///**
// * 第一个自定义菜单的二级菜单显示
// *
// * @author xqm
// */
//public class PublicMenuPopLeft extends PopupWindow {
//    private View conentView;
//    private ArrayList<PublicMenuInfoModel> childrens = new ArrayList<PublicMenuInfoModel>();
//
//    /**
//     * @param context
//     * @param appid    公众号id
//     * @param children 二级菜单数据
//     * @param size     一级菜单的数量 用来设置二级菜单显示的位置 (左右比例)
//     */
//    public PublicMenuPopLeft(final Context context, final String appid, ArrayList<PublicMenuInfoModel> children, final int size) {
//        this.childrens.clear();
//        for (int i = 0; i < children.size(); i++) {
//            childrens.add(children.get(children.size() - 1 - i));
//        }
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        switch (size) {// 根据size的不同 加载不同的布局文件 适配位置
//            case 1:
//                conentView = inflater.inflate(R.layout.pop_size_left, null);
//                break;
//            case 2:
//                conentView = inflater.inflate(R.layout.pop_size_mid, null);
//                conentView.findViewById(R.id.pop_lyt_left).setVisibility(View.VISIBLE);
//                conentView.findViewById(R.id.pop_lyt_mid).setVisibility(View.INVISIBLE);
//                break;
//            case 3:
//                conentView = inflater.inflate(R.layout.pop_size_right, null);
//                conentView.findViewById(R.id.pop_lyt_left).setVisibility(View.VISIBLE);
//                conentView.findViewById(R.id.pop_lyt_mid).setVisibility(View.INVISIBLE);
//                conentView.findViewById(R.id.pop_lyt_right).setVisibility(View.INVISIBLE);
//                break;
//
//            default:
//                conentView = inflater.inflate(R.layout.pop_size_right, null);
//                conentView.findViewById(R.id.pop_lyt_left).setVisibility(View.VISIBLE);
//                conentView.findViewById(R.id.pop_lyt_mid).setVisibility(View.INVISIBLE);
//                conentView.findViewById(R.id.pop_lyt_right).setVisibility(View.INVISIBLE);
//                break;
//        }
//
//        int h = MyApplication.getInstance().height;
//        int w = MyApplication.getInstance().width;
//        // 设置SelectPicPopupWindow的View
//        this.setContentView(conentView);
//        // 设置SelectPicPopupWindow弹出窗体的宽
//        this.setWidth(w);
//        // 设置SelectPicPopupWindow弹出窗体的高
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);
//        this.setOutsideTouchable(true);
//        // 刷新状态
//        this.update();
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0000000000);
//        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//        this.setBackgroundDrawable(dw);
//        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
//        // 设置SelectPicPopupWindow弹出窗体动画效果
//        // this.setAnimationStyle(R.style.AnimationPreview);
//
//        Button public_menu_left1 = (Button) conentView.findViewById(R.id.public_menu_left1);
//        Button public_menu_left2 = (Button) conentView.findViewById(R.id.public_menu_left2);
//        Button public_menu_left3 = (Button) conentView.findViewById(R.id.public_menu_left3);
//        Button public_menu_left4 = (Button) conentView.findViewById(R.id.public_menu_left4);
//        Button public_menu_left5 = (Button) conentView.findViewById(R.id.public_menu_left5);
//
//        LinearLayout iv_public_menu_left5 = (LinearLayout) conentView.findViewById(R.id.iv_public_menu_left5);
//        LinearLayout iv_public_menu_left4 = (LinearLayout) conentView.findViewById(R.id.iv_public_menu_left4);
//        LinearLayout iv_public_menu_left3 = (LinearLayout) conentView.findViewById(R.id.iv_public_menu_left3);
//        LinearLayout iv_public_menu_left2 = (LinearLayout) conentView.findViewById(R.id.iv_public_menu_left2);
//        conentView.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                PublicMenuPopLeft.this.dismiss();
//            }
//        });
//
//        if (childrens.size() > 0) {
//            public_menu_left1.setText(childrens.get(0).getName());
//            public_menu_left1.setVisibility(View.VISIBLE);
//            public_menu_left1.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//
//                    if ("click".equals(childrens.get(0).getType())) {
//                        LoadingDialog.showDialog(context, "数据加载,请稍后…");
//
//                        CoreHttpApi.clickSubscriptionMenu(appid, childrens.get(0).getCode());
//
//                    } else if ("view".equals(childrens.get(0).getType())) {
//                        String url = Constants.getCoreUrls().clickSubscriptionMenu();
//                        url = url.replace("{appId}", ToolsHelper.toString(appid));
//                        url = url.replace("{menuCode}", ToolsHelper.toString(childrens.get(0).getCode()));
//                        WebViewActivityHelper.startWebViewActivity(context, url, childrens.get(0).getName());
//
//                    }
//                    PublicMenuPopLeft.this.dismiss();
//                }
//            });
//        }
//        if (childrens.size() > 1) {
//            public_menu_left2.setText(childrens.get(1).getName());
//            public_menu_left2.setVisibility(View.VISIBLE);
//            iv_public_menu_left2.setVisibility(View.VISIBLE);
//            public_menu_left2.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//                    if ("click".equals(childrens.get(1).getType())) {
//                        LoadingDialog.showDialog(context, "数据加载,请稍后…");
//
//                        CoreHttpApi.clickSubscriptionMenu(appid, childrens.get(1).getCode());
//
//                    } else if ("view".equals(childrens.get(1).getType())) {
//                        String url = Constants.getCoreUrls().clickSubscriptionMenu();
//                        url = url.replace("{appId}", ToolsHelper.toString(appid));
//                        url = url.replace("{menuCode}", ToolsHelper.toString(childrens.get(1).getCode()));
//                        WebViewActivityHelper.startWebViewActivity(context, url, childrens.get(1).getName());
//
//                    }
//                    PublicMenuPopLeft.this.dismiss();
//                }
//            });
//        }
//        if (childrens.size() > 2) {
//            public_menu_left3.setText(childrens.get(2).getName());
//            public_menu_left3.setVisibility(View.VISIBLE);
//            iv_public_menu_left3.setVisibility(View.VISIBLE);
//            public_menu_left3.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//                    if ("click".equals(childrens.get(2).getType())) {
//                        LoadingDialog.showDialog(context, "数据加载,请稍后…");
//
//                        CoreHttpApi.clickSubscriptionMenu(appid, childrens.get(2).getCode());
//
//                    } else if ("view".equals(childrens.get(2).getType())) {
//                        String url = Constants.getCoreUrls().clickSubscriptionMenu();
//                        url = url.replace("{appId}", ToolsHelper.toString(appid));
//                        url = url.replace("{menuCode}", ToolsHelper.toString(childrens.get(2).getCode()));
//                        WebViewActivityHelper.startWebViewActivity(context, url, childrens.get(2).getName());
//
//                    }
//                    PublicMenuPopLeft.this.dismiss();
//                }
//            });
//        }
//        if (childrens.size() > 3) {
//            public_menu_left4.setText(childrens.get(3).getName());
//            public_menu_left4.setVisibility(View.VISIBLE);
//            iv_public_menu_left4.setVisibility(View.VISIBLE);
//            public_menu_left4.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//
//                    // TODO Auto-generated method stub
//                    if ("click".equals(childrens.get(3).getType())) {
//                        LoadingDialog.showDialog(context, "数据加载,请稍后…");
//
//                        CoreHttpApi.clickSubscriptionMenu(appid, childrens.get(3).getCode());
//
//                    } else if ("view".equals(childrens.get(3).getType())) {
//                        String url = Constants.getCoreUrls().clickSubscriptionMenu();
//                        url = url.replace("{appId}", ToolsHelper.toString(appid));
//                        url = url.replace("{menuCode}", ToolsHelper.toString(childrens.get(3).getCode()));
//                        WebViewActivityHelper.startWebViewActivity(context, url, childrens.get(3).getName());
//
//                    }
//                    PublicMenuPopLeft.this.dismiss();
//                }
//            });
//        }
//        if (childrens.size() > 4) {
//            public_menu_left5.setText(childrens.get(4).getName());
//            public_menu_left5.setVisibility(View.VISIBLE);
//            iv_public_menu_left5.setVisibility(View.VISIBLE);
//            public_menu_left5.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//                    if ("click".equals(childrens.get(4).getType())) {
//                        LoadingDialog.showDialog(context, "数据加载,请稍后…");
//
//                        CoreHttpApi.clickSubscriptionMenu(appid, childrens.get(4).getCode());
//
//                    } else if ("view".equals(childrens.get(4).getType())) {
//                        String url = Constants.getCoreUrls().clickSubscriptionMenu();
//                        url = url.replace("{appId}", ToolsHelper.toString(appid));
//                        url = url.replace("{menuCode}", ToolsHelper.toString(childrens.get(4).getCode()));
//                        WebViewActivityHelper.startWebViewActivity(context, url, childrens.get(4).getName());
//
//                    }
//                    PublicMenuPopLeft.this.dismiss();
//                }
//            });
//        }
//    }
//
//    /**
//     * 显示popupWindow
//     *
//     * @param parent
//     */
//    public void showPopupWindow(View parent) {
//        if (!this.isShowing()) {
//            // 以下拉方式显示popupwindow
//            this.showAsDropDown(parent);
//        } else {
//            this.dismiss();
//        }
//    }
//}
