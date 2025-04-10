package org.example.twosixtagram.domain.friend.entity;

public enum FriendStatus {
    /**
     *     PENDING  : 친구 요청
     *     ACCEPTED : 친구 수락,
     *     DECLINED : 친구 거절,
     *     REMOVED  : 삭제하기
     *
     */
    PENDING("요청"),
    ACCEPTED("수락"),
    DECLINED("거절"),
    REMOVED("삭제");

    private final String description;

    FriendStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

}
