package cn.lds.chatcore.data;

/**
 * Created by xuqm on 2016/6/12.
 */
public class OrderPayModel {

    /**
     * amount : 0
     * businessId : 0
     * businessType : ORDER
     * id : 0
     * ticketId : 0
     */

    private String amount;
    private int businessId;
    private String businessType;
    private int id;
    private String ticketId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
