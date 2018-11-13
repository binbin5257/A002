package cn.lds.im.data;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.lds.chatcore.common.pickerview.ProvinceBean;


public class TimeDataModel {

    /**
     * 初始化三个选项卡数据。
     */
    public static void initData(ArrayList<ProvinceBean> options1Items, ArrayList<ArrayList<String>> options2Items, ArrayList<ArrayList<ArrayList<String>>> options3Items) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        int  curYear = calendar.get(Calendar.YEAR);
        int  curMonth = calendar.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int  curDay = calendar.get(Calendar.DATE);
        int  curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int  curMin = calendar.get(Calendar.MINUTE);
        //选项1
        if(curHour == 23 && curMin > 40){
            options1Items.add(new ProvinceBean(0, "明天", curYear + "年"+ curMonth + "月"+ (curDay+1) + "日",""));
            options1Items.add(new ProvinceBean(1,  curYear + "年"+ curMonth + "月"+ (curDay+2) + "日", "嗯～～", ""));
            //选项2
            ArrayList<String> options2Items_01 = new ArrayList<String>();
            for(int i = 0;i < 24;i++){
                options2Items_01.add(i + "时");
            }
            ArrayList<String> options2Items_02 = new ArrayList<String>();
            for(int j = 0;j < 24;j++){
                options2Items_02.add(j + "时");

            }
            options2Items.add(options2Items_01);
            options2Items.add(options2Items_02);
            //选项3
            ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> options3Items_02 = new ArrayList<ArrayList<String>>();
            for(int options2Items_02L = 0;options2Items_02L < options2Items_02.size();options2Items_02L++){
                ArrayList<String> options2Items_02LL = new ArrayList<String>();
                for(int k = 0; k < 60; k += 10){
                    options2Items_02LL.add(k + "分");
                }
                options3Items_01.add(options2Items_02LL);
                options3Items_02.add(options2Items_02LL);


            }
            options3Items.add(options3Items_01);
            options3Items.add(options3Items_02);


        }else{
            options1Items.add(new ProvinceBean(0, "今天", curYear + "年"+ curMonth + "月"+ curDay + "日",""));
            options1Items.add(new ProvinceBean(1, "明天", curYear + "年"+ curMonth + "月"+ (curDay+1) + "日",""));
            options1Items.add(new ProvinceBean(2,  curYear + "年"+ curMonth + "月"+ (curDay+1) + "日", "嗯～～", ""));


            //选项2
            ArrayList<String> options2Items_01 = new ArrayList<String>();
            if(curMin > 40){
                for(int i = curHour + 1;i < 24;i++){
                    options2Items_01.add(i + "时");
                }
            }else{

                for(int i = curHour;i < 24;i++){
                    options2Items_01.add(i + "时");
                }

            }


            ArrayList<String> options2Items_02 = new ArrayList<String>();
            ArrayList<String> options2Items_03 = new ArrayList<String>();
            for(int j = 0;j < 24;j++){
                options2Items_02.add(j + "时");
                options2Items_03.add(j + "时");

            }
            options2Items.add(options2Items_01);
            options2Items.add(options2Items_02);
            options2Items.add(options2Items_03);
            //选项3
            ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> options3Items_02 = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> options3Items_03 = new ArrayList<ArrayList<String>>();
            for(int options2Items_01L = 0;options2Items_01L < options2Items_01.size();options2Items_01L++){
                ArrayList<String> options3Items_01_01LL = new ArrayList<String>();
                if(options2Items_01L == 0){
                    if(curMin > 40 && curMin < 50){
                        for(int x = 0; x < 60; x += 10){
                            options3Items_01_01LL.add(x + "分");
                        }
                    }else{
                        for(int k = 0; k < 60; k += 10){
                            if(curMin + 10 > 60){
                                if( k > curMin + 10 -60){
                                    options3Items_01_01LL.add(k + "分");
                                }
                            }else{
                                if(k >= curMin + 10){
                                    options3Items_01_01LL.add(k + "分");
                                }

                            }

                        }
                    }
                }else{
                    for(int k = 0; k < 60; k += 10){
                        options3Items_01_01LL.add(k + "分");
                    }
                }
                options3Items_01.add(options3Items_01_01LL);


            }
            for(int options2Items_02L = 0;options2Items_02L < options2Items_02.size();options2Items_02L++){
                ArrayList<String> options2Items_02LL = new ArrayList<String>();
                for(int k = 0; k < 60; k += 10){
                    options2Items_02LL.add(k + "分");
                }
                options3Items_02.add(options2Items_02LL);

            }
            for(int options2Items_03L = 0;options2Items_03L < options2Items_03.size();options2Items_03L++){
                ArrayList<String> options2Items_03LL = new ArrayList<String>();
                for(int k = 0; k < 60; k += 10){
                    options2Items_03LL.add(k + "分");
                }
                options3Items_03.add(options2Items_03LL);

            }
            options3Items.add(options3Items_01);
            options3Items.add(options3Items_02);
            options3Items.add(options3Items_03);
        }


    }
}
