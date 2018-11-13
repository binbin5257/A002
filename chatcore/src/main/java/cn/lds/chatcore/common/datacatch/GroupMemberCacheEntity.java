package cn.lds.chatcore.common.datacatch;

public class GroupMemberCacheEntity implements Comparable<GroupMemberCacheEntity> {
    String name;
    VcardCacheEntity vcard;
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public VcardCacheEntity getVcard() {
        return this.vcard;
    }
    
    public void setVcard(VcardCacheEntity vcard) {
        this.vcard = vcard;
    }
    
    @Override
    public int compareTo(GroupMemberCacheEntity o) {
        if (o == null) {
            return -1;
        }
        return this.vcard.compareTo(o.getVcard());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GroupMemberCacheEntity)) {
            return false;
        }
        final GroupMemberCacheEntity vcard = (GroupMemberCacheEntity) obj;
        return this.vcard.equals(vcard);
    }
    
    @Override
    public String toString() {
        return "gName:" + this.name + ",vcard:{" + this.vcard.toString() + "}";
    }
}
