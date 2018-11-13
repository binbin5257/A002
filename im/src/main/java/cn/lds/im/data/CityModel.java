package cn.lds.im.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E0608 on 2017/9/26.
 */

public class CityModel {


    public List<CityBean> initCityData() {
        List<CityBean> mList = new ArrayList<>();

        CityBean beijing = new CityBean();
        beijing.setName("北京");
        beijing.setCode("131");
        beijing.setCityAddress("北京市");
        beijing.setAddress("北京市东城区正义路2号-10号楼-403号");
        beijing.setLog(116.41355796839203);
        beijing.setLat(39.91101012309198);
        beijing.setSelectCity(false);

        CityBean dalian = new CityBean();
        dalian.setName("大连");
        dalian.setCityAddress("大连市");
        dalian.setAddress("辽宁省大连市西岗区人民广场1号");
        dalian.setCode("167");
        dalian.setLog(121.62159195915041);
        dalian.setLat(38.9189774576885);
        dalian.setSelectCity(false);

        CityBean suzhou = new CityBean();
        suzhou.setName("苏州");
        suzhou.setCode("224");
        suzhou.setCityAddress("苏州市");
        suzhou.setAddress("江苏省苏州市姑苏区三香路128号");
        suzhou.setLog(120.59200808194429);
        suzhou.setLat(31.303570630955864);
        suzhou.setSelectCity(false);

        CityBean wuhan = new CityBean();
        wuhan.setName("武汉");
        wuhan.setCode("218");
        wuhan.setCityAddress("武汉市");
        wuhan.setAddress("湖北省武汉市江岸区沿江大道188号");
        wuhan.setLog(114.31196324474351);
        wuhan.setLat(30.5985034167207);
        wuhan.setSelectCity(false);

        mList.add(beijing);
        mList.add(dalian);
        mList.add(suzhou);
        mList.add(wuhan);

        return mList;

    }
}
