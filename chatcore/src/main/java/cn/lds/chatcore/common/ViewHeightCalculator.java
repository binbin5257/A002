package cn.lds.chatcore.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * View高度计算器
 * <ul>
 * <li>作用：为了解决ScrollView嵌套ListView或GridView的问题需要动态计算View的高度</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 *
 * @author guotl111930
 */
public class ViewHeightCalculator {

    /**
     * 动态设置ListView对象的高度
     *
     * @param listView 目标对象
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);
            listItem
                    .setLayoutParams(new ListView.LayoutParams(
                            ListView.LayoutParams.WRAP_CONTENT,
                            ListView.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            int h = listItem.getHeight();
            int h1 = listItem.getMeasuredHeightAndState();
            int h3 = listItem.getMeasuredHeight();
            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);


    }


    /**
     * 动态设置GridView组建的高度
     *
     * @param context         上下文环境
     * @param gridView        目标对象
     * @param columnCount     表格列数
     * @param itemExtraHeight (单位：dp)在结算结果的基础上，为每个单元格添加额外的自定义高度
     */
    public static void setGrideViewHeightBasedOnChildren(Context context,
                                                         GridView gridView, int columnCount, int itemExtraHeight) {

        ListAdapter listAdapter = gridView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int count = listAdapter.getCount() / columnCount;
        if (listAdapter.getCount() % columnCount > 0) {
            count = listAdapter.getCount() / columnCount + 1;
        } else {
            count = listAdapter.getCount() / columnCount;
        }
        int pxItemExtraHeight = 0;
        if (itemExtraHeight > 0) {
            pxItemExtraHeight = DimensHelper.dip2px(context, itemExtraHeight);
        }

        for (int i = 0; i < count; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            // 添加额外的自定义预留高度
            if (pxItemExtraHeight > 0) {
                totalHeight += pxItemExtraHeight;
            }
        }

        gridView.getLayoutParams().height = totalHeight;

        // gridView.setLayoutParams(params);
    }

    /**
     * 动态设置ListView对象的高度
     *
     * @param listView    目标对象
     * @param extraHeight 额外高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, int extraHeight) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + extraHeight;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}
