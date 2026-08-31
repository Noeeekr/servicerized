package com.github.noeeekr.servicerized.identity.repository.models;

public interface GroupKinds {
    Long getId();

    String getName();

    public static final GroupKinds AccessGroup = new GroupKinds.NewGroupKind("AccessGroup", Long.valueOf(0));
    public static final GroupKinds FriendGroup = new GroupKinds.NewGroupKind("FriendGroup", Long.valueOf(1));

    public record NewGroupKind(String name, Long id) implements GroupKinds {
        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
