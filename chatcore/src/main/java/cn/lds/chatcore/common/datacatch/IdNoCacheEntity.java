package cn.lds.chatcore.common.datacatch;

public class IdNoCacheEntity implements Comparable<IdNoCacheEntity> {

    private String id;
    private String no;

    public IdNoCacheEntity(){}

    public IdNoCacheEntity(String id,String no){
        this.id = id;
        this.no = no;
    }
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return this.no;
    }

    public void setNo(String no) {
        this.no = no;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof IdNoCacheEntity)) {
            return false;
        }
        final IdNoCacheEntity idNoCacheEntity = (IdNoCacheEntity) obj;
        return this.idNoEquals(idNoCacheEntity);
    }
    
    @Override
    public int compareTo(IdNoCacheEntity idNoCacheEntity) {
        if (this.idNoEquals(idNoCacheEntity)) {
            return 0;
        } else {
            return -1;
        }
    }

    private boolean idNoEquals(IdNoCacheEntity idNoCacheEntity) {
        if (idNoCacheEntity == null) {
            return false;
        }
        if (idNoCacheEntity.getId() != null) {
            if (idNoCacheEntity.getId().equals(this.getId())
                    || idNoCacheEntity.getId().equals(this.getNo())) {
                return true;
            }
        }
        if (idNoCacheEntity.getNo() != null) {
            if (idNoCacheEntity.getNo().equals(this.getId())
                    || idNoCacheEntity.getNo().equals(this.getNo())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "id:" + this.id + ",no:" + this.no;
    }
}
