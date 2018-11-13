package cn.lds.chatcore.common.datacatch;

public class Test {
    public static void main(String[] args) {
        final CacheManager cache = CacheManager.getInstance();
        Test.addData(cache);
        System.out.println("----" + System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            final GroupMemberCacheEntity member = cache.getGroupMember("gid" + i, "id" + i);
            //            System.out.println(member);
        }
        System.out.println("----" + System.currentTimeMillis());
    }
    
    public static void addData(CacheManager cache) {
        for (int i = 0; i <= 10000; i++) {
            final IdNoCacheEntity id = new IdNoCacheEntity();
            id.setId("id" + i);
            id.setNo("no" + i);
            final VcardCacheEntity vcard = new VcardCacheEntity();
            vcard.setAvatarId("AvatarId" + i);
            vcard.setFriend(false);
            vcard.setId(id);
            vcard.setNickName("昵称" + i);
            cache.addVcard(vcard);
        }
        for (int i = 0; i < 100; i++) {
            final GroupCacheEntity group = new GroupCacheEntity();
            final IdNoCacheEntity id = new IdNoCacheEntity();
            id.setId("gid" + i);
            id.setNo("gno" + i);
            group.setId(id);
            group.setName("gname" + i);
            for (int j = 0; j < 500; j++) {
                final VcardCacheEntity vcard = cache.getVcard("id" + i);
                final GroupMemberCacheEntity member = new GroupMemberCacheEntity();
                member.setVcard(vcard);
                member.setName("group:" + i + "memb:" + j);
                group.addMember(member);
            }
            cache.addGroup(group);
        }

    }
}
