package cn.lds.im.data;

/**
 * Created by colin on 15/12/25.
 */
public class TraveledModel {

    /**
     * status : success
     * data : http://api.map.baidu.com/staticimage/v2?ak=pnnGotiumCYVOmhDTA7uvh8Q&center=121.40911674027986,31.18079034482493&width=675&height=300&zoom=15&paths=121.40911674027986,31.18479034482493;121.40982535701605,31.18430082516098;121.40894100458644,31.183840624376046;121.40790468029788,31.182283388943926;121.40877185226414,31.178834733192712;121.40897880469554,31.17815405574857;121.40950420558578,31.176226478575607;121.40998994580075,31.17449040679796;121.41011737395708,31.17408452866403;121.41014923279775,31.173929033884225;121.41014923279775,31.173929033884225;121.41188524292689,31.17402514154165;121.41387566205479,31.17442681755377;121.414257967626,31.175124414761385;121.4142339731695,31.176004407246502;121.41419414506778,31.175701496853375;121.41376422201587,31.17502300094694;121.41289642543471,31.1744363109871;121.41236322294444,31.177301747206876;121.4122996213297,31.177844842465046;121.4122996213297,31.177844842465046;121.41212470648442,31.178996188262374;;121.4116780631184,31.181151747957102;121.41129781146405,31.1825488000442;121.41106743681372,31.18374724751782;121.41039874228323,31.18422923450823;121.40945109325484,31.18450146474415;121.40918028904757,31.18446728680442;121.40901314898171,31.18471444410418;121.40922816517445,31.184626242989793;&pathStyles=0x238E23,5,1
     */

    private String status;
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
