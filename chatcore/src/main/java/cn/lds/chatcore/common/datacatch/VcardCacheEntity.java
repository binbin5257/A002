package cn.lds.chatcore.common.datacatch;

public class VcardCacheEntity implements Comparable<VcardCacheEntity> {
    
    IdNoCacheEntity id;
    String avatarId;
    String nickName;
    boolean isFriend = false;
    
    public String getAvatarId() {
        return this.avatarId;
    }
    
    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }
    
    public String getNickName() {
        return this.nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public boolean isFriend() {
        return this.isFriend;
    }
    
    public void setFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }
    
    public IdNoCacheEntity getId() {
        return this.id;
    }
    
    public void setId(IdNoCacheEntity id) {
        this.id = id;
    }

    @Override
    public int compareTo(VcardCacheEntity o) {
        if (o == null) {
            return -1;
        }
        return this.id.compareTo(o.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof VcardCacheEntity)) {
            return false;
        }
        final VcardCacheEntity vcard = (VcardCacheEntity) obj;
        return this.id.equals(vcard.getId());
    }
    
    @Override
    public String toString() {
        return "avatarId:" + this.avatarId + ",id:{" +
                this.id.toString() + "},isFriend:" + this.isFriend + ",nickName:" + this.nickName;
    }
}
