package cn.lds.chatcore.common.datacatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupCacheEntity implements Comparable<GroupCacheEntity> {
    IdNoCacheEntity id;
    String name;
    List<GroupMemberCacheEntity> members = Collections.synchronizedList(new ArrayList<GroupMemberCacheEntity>());

    public IdNoCacheEntity getId() {
        return this.id;
    }

    public void setId(IdNoCacheEntity id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupMemberCacheEntity> getMembers() {
        return this.members;
    }
    
    public GroupMemberCacheEntity getMember(String userIdOrNo) {
        final IdNoCacheEntity idno = new IdNoCacheEntity();
        idno.setId(userIdOrNo);

        final GroupMemberCacheEntity[] groupMemberArray = this.members.toArray(new GroupMemberCacheEntity[0]);
        for (final GroupMemberCacheEntity gmember : groupMemberArray) {
            if (gmember.getVcard().getId().equals(idno)) {
                return gmember;
            }
        }
        return null;
    }
    
    public synchronized boolean addMember(GroupMemberCacheEntity groupMember) {
        this.members.add(groupMember);
        return true;
    }
    
    public synchronized boolean removeMember(IdNoCacheEntity id) {
        final VcardCacheEntity vcard = new VcardCacheEntity();
        vcard.setId(id);
        final GroupMemberCacheEntity groupMember = new GroupMemberCacheEntity();
        groupMember.setVcard(vcard);
        return this.members.remove(groupMember);
    }
    
    public synchronized boolean removeMember(GroupMemberCacheEntity groupMember) {

        return this.members.remove(groupMember);
    }
    
    @Override
    public int compareTo(GroupCacheEntity o) {
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
        if (!(obj instanceof GroupCacheEntity)) {
            return false;
        }
        final GroupCacheEntity group = (GroupCacheEntity) obj;
        return this.id.equals(group.getId());
    }
    
    @Override
    public String toString() {
        return "id:" + this.id + ",name:" + this.name + "members:" + this.members;
    }
    
}
