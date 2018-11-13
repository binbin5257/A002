package cn.lds.chatcore.event;


/**
 * 切换pager
 *
 * @author xuqm
 */
public class SelectPagerEvent {
    private int id;

    public SelectPagerEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
