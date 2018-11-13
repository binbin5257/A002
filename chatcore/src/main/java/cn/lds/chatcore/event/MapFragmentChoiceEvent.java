package cn.lds.chatcore.event;


/**
 *
 * @author pengwb
 */
public class MapFragmentChoiceEvent {

    public int choiceType = -1;//默认值

    public MapFragmentChoiceEvent(int type) {
        choiceType = type;
    }

    public int getChoiceType() {
        return choiceType;
    }
}
