package cn.lds.chatcore.data;

/**
 * Created by sibinbin on 18-6-22.
 */

public class DepartmentAccountModel
{

    /**
     * status : success
     * data : true
     */

    private String status;
    private DataBean data;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData( DataBean data ) {
        this.data = data;
    }

    public class DataBean{
        private int changeRemainAmount;
        private int monthRemainAmount;

        public int getChangeRemainAmount() {
            return changeRemainAmount;
        }

        public void setChangeRemainAmount( int changeRemainAmount ) {
            this.changeRemainAmount = changeRemainAmount;
        }

        public int getMonthRemainAmount() {
            return monthRemainAmount;
        }

        public void setMonthRemainAmount( int monthRemainAmount ) {
            this.monthRemainAmount = monthRemainAmount;
        }
    }
}
