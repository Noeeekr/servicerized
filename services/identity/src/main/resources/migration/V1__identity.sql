    create table identity.access_control_list (
        permission_id bigint not null,
        origin_group_id uuid not null,
        resource_id uuid not null,
        resource_kind_id uuid not null,
        target_group_id uuid not null,
        primary key (permission_id),
        constraint uc_acl_target_origin_perm unique (target_group_id, origin_group_id, permission_id)
    );

    create table identity.acl_permissions (
        permission_id bigserial not null,
        permission_name varchar(255) not null unique,
        primary key (permission_id)
    );

    create table identity.acl_resources (
        resource_id uuid not null,
        resource_name varchar(128) not null unique,
        primary key (resource_id)
    );

    create table identity.group_kinds (
        group_kind_id bigserial not null,
        group_kind_name varchar(255) not null,
        primary key (group_kind_id),
        constraint uk_group_kind_name unique (group_kind_name)
    );

    create table identity.groups (
        group_kinds bigint not null,
        group_id uuid not null,
        group_owner_id uuid not null,
        primary key (group_id)
    );

    create table identity.groups_users (
        group_id uuid not null,
        user_id uuid not null,
        primary key (group_id, user_id)
    );

    create table identity.users (
        created_at timestamp(6) default CURRENT_TIMESTAMP not null,
        deleted_at timestamp(6) default NULL,
        updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
        user_id uuid not null,
        user_email varchar(255) not null unique,
        user_name varchar(255) not null unique,
        user_password varchar(255) not null,
        primary key (user_id)
    );

    create table identity.users_sessions (
        created_at timestamp(6) default CURRENT_TIMESTAMP not null,
        deleted_at timestamp(6) default NULL,
        updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
        user_session_id uuid not null,
        primary key (user_session_id)
    );

    create index idx_acl_target_origin_perm 
       on identity.access_control_list (target_group_id, origin_group_id, permission_id);

    alter table if exists identity.access_control_list 
       add constraint fk_permission_id 
       foreign key (permission_id) 
       references identity.acl_permissions;

    alter table if exists identity.access_control_list 
       add constraint fk_origin_group_id 
       foreign key (origin_group_id) 
       references identity.groups;

    alter table if exists identity.access_control_list 
       add constraint fk_resource_kind_id 
       foreign key (resource_kind_id) 
       references identity.acl_resources;

    alter table if exists identity.access_control_list 
       add constraint fk_target_group_id 
       foreign key (target_group_id) 
       references identity.groups;

    alter table if exists identity.groups 
       add constraint fk_group_kind_id 
       foreign key (group_kinds) 
       references identity.group_kinds;

    alter table if exists identity.groups 
       add constraint fk_group_owner_id 
       foreign key (group_owner_id) 
       references identity.users;

    alter table if exists identity.groups_users 
       add constraint fk_group_id 
       foreign key (group_id) 
       references identity.groups;

    alter table if exists identity.groups_users 
       add constraint fk_user_id 
       foreign key (user_id) 
       references identity.users;
