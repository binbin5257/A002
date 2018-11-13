package cn.lds.chatcore.common.datacatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.lds.chatcore.common.LogHelper;

public class CacheManager {
    List<GroupCacheEntity> groups = Collections.synchronizedList(new ArrayList<GroupCacheEntity>());
    List<VcardCacheEntity> vcards = Collections.synchronizedList(new ArrayList<VcardCacheEntity>());

    private static CacheManager cacheManager = null;

    private CacheManager() {

    }

    public static CacheManager getInstance() {
        if (CacheManager.cacheManager == null) {
            return CacheManager.cacheManager = new CacheManager();
        } else {
            return CacheManager.cacheManager;
        }
    }

    public static void resetCache() {
        CacheManager.cacheManager = null;
        LogHelper.d("缓存管理 (addVcard)：重置缓存");
    }

    public boolean addVcard(VcardCacheEntity vcard) {
        LogHelper.d("缓存管理 (addVcard)：id=" + vcard.getId() + ",nickName=" + vcard.getNickName() + ",avatarId=" + vcard.getAvatarId());
        final VcardCacheEntity[] vcardArray = this.vcards.toArray(new VcardCacheEntity[0]);
        for (final VcardCacheEntity vd : vcardArray) {
            if (vd.equals(vcard)) {
                vd.setNickName(vcard.getNickName());
                vd.setAvatarId(vcard.getAvatarId());
                return true;
            }
        }
        return this.vcards.add(vcard);
    }

    //    private boolean removeVcard(String idOrNo) {
    //        final IdNoCacheEntity idNoCacheEntity = new IdNoCacheEntity();
    //        idNoCacheEntity.setId(idOrNo);
    //        //需要考虑，如果vcard在群中，则需要从群中删除？？
    //        return this.vcards.remove(idNoCacheEntity);
    //    }

    public VcardCacheEntity getVcard(String idOrNo) {
        final IdNoCacheEntity idNoCacheEntity = new IdNoCacheEntity();
        idNoCacheEntity.setId(idOrNo);
        final VcardCacheEntity[] vcardArray = this.vcards.toArray(new VcardCacheEntity[0]);
        for (final VcardCacheEntity vcard : vcardArray) {
            if (vcard.getId().equals(idNoCacheEntity)) {
                LogHelper.d("缓存管理 (getVcard)：" + vcard.toString());
                return vcard;
            }
        }
        LogHelper.d("缓存管理 (getVcard)：idOrNo=" + idOrNo + "的Vcard缓存不存在");
        return null;
    }

    public boolean addGroup(GroupCacheEntity group) {
        LogHelper.d("缓存管理 (addGroup)：group.getId()=" + group.getId() + ",group.getName()=" + group.getName());
        return this.groups.add(group);
    }

    public boolean removeGroup(String idOrNo) {
        final IdNoCacheEntity idNoCacheEntity = new IdNoCacheEntity();
        idNoCacheEntity.setId(idOrNo);
        return this.groups.remove(idNoCacheEntity);
    }

    public GroupCacheEntity getGroup(String idOrNo) {
        final IdNoCacheEntity idNoCacheEntity = new IdNoCacheEntity();
        idNoCacheEntity.setId(idOrNo);
        final GroupCacheEntity group = new GroupCacheEntity();
        group.setId(idNoCacheEntity);
        // TODO:这个不好用
        final int index = this.groups.indexOf(group);
        if (index == -1) {
            LogHelper.d("缓存管理 (getGroup)：idOrNo=" + idOrNo + "的Group缓存不存在");
            return null;
        } else {
            LogHelper.d("缓存管理 (getGroup)：" + this.groups.get(index).toString());
            return this.groups.get(index);
        }
    }

    public boolean addGroupMember(String groupIdOrNo, GroupMemberCacheEntity groupMember) {
        LogHelper.d("缓存管理 (addGroupMember)：groupIdOrNo=" + groupIdOrNo + ",nickName=" + groupMember.getName());
        final GroupCacheEntity group = this.getGroup(groupIdOrNo);
        if ((group == null) || (groupMember == null)) {
            return false;
        } else {
            GroupMemberCacheEntity cacheMember = this.getGroupMember(groupIdOrNo, groupMember.getVcard().getId().getId());
//            if (cacheMember == null) {
//                cacheMember = this.getGroupMember(groupIdOrNo, groupMember.getVcard().getId().getNo());
//            }
            if (cacheMember != null) {
                group.removeMember(cacheMember);
            }
            group.addMember(groupMember);
            return true;
        }

    }

    public boolean removeGroupMember(String groupIdOrNo, String userIdOrNo) {
        final GroupCacheEntity group = this.getGroup(groupIdOrNo);
        if (group == null) {
            return false;
        } else {
            final List<GroupMemberCacheEntity> members = group.getMembers();
            final IdNoCacheEntity idno = new IdNoCacheEntity();
            idno.setId(userIdOrNo);

            final GroupMemberCacheEntity[] groupMemberArray = members.toArray(new GroupMemberCacheEntity[0]);
            for (final GroupMemberCacheEntity gmember : groupMemberArray) {
                if (gmember.getVcard().getId().equals(idno)) {
                    return group.removeMember(gmember.getVcard().getId());
                }
            }
            return false;
        }
    }

    public GroupMemberCacheEntity getGroupMember(String groupIdOrNo, String userIdOrNo) {
        final GroupCacheEntity group = this.getGroup(groupIdOrNo);
        if (group == null) {
            LogHelper.d("缓存管理 (getGroupMember)：groupIdOrNo=" + groupIdOrNo + ",userIdOrNo=" + userIdOrNo + "的group缓存不存在");
            return null;
        } else {
            final List<GroupMemberCacheEntity> members = group.getMembers();
            final IdNoCacheEntity idno = new IdNoCacheEntity();
            idno.setId(userIdOrNo);

            final GroupMemberCacheEntity[] groupMemberArray = members.toArray(new GroupMemberCacheEntity[0]);
            for (final GroupMemberCacheEntity gmember : groupMemberArray) {
                if (gmember.getVcard().getId().equals(idno)) {
                    LogHelper.d("缓存管理 (getGroupMember)：" + gmember.toString());
                    return gmember;
                }
            }
            LogHelper.d("缓存管理 (getGroupMember)：groupIdOrNo=" + groupIdOrNo + ",userIdOrNo=" + userIdOrNo + "的GroupMember缓存不存在");
            return null;
        }
    }

    /**
     * 输出所有缓存信息
     */
    public void printAllFromCache() {
        try {
            LogHelper.e("缓存管理 vcard sta -----------------------");
            for (final VcardCacheEntity vcard : this.vcards) {
                LogHelper.e("缓存管理 " + vcard.toString());
            }
            LogHelper.e("缓存管理 vcard end -----------------------");
            LogHelper.e("缓存管理 group sta -----------------------");
            for (final GroupCacheEntity group : this.groups) {
                LogHelper.e("缓存管理 " + group.toString());
            }
            LogHelper.e("缓存管理 group end -----------------------");
        } catch (Exception ex) {
            LogHelper.e("缓存管理" + ex);
        }
    }
}
