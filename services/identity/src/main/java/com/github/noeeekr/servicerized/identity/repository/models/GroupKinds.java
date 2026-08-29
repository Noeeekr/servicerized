package com.github.noeeekr.servicerized.identity.repository.models;

public interface GroupKinds {
    long getId();

    String getName();

    public static final GroupKinds AccessGroup = new GroupKinds.NewGroupKind("AccessGroup", 0);
    public static final GroupKinds FriendGroup = new GroupKinds.NewGroupKind("FriendGroup", 1);

    public record NewGroupKind(String name, long id) implements GroupKinds {
        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
