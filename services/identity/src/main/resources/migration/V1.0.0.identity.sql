--liquibase formatted sql logicalFilePath:migration/V1.0.0.identity.sql

--changeset migrate-identity-service:create-users-table
create table identity.users (
    created_at timestamp(6) default CURRENT_TIMESTAMP not null,
    deleted_at timestamp(6) default NULL,
    updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
    user_id uuid not null,
    user_email varchar(255) not null unique,
    user_name varchar(255) not null unique,
    primary key (user_id)
);
--rollback drop table if exists identity.users;

--changeset migrate-identity-service:create-acl-permissions-table
create table identity.acl_permissions (
    permission_id bigserial not null,
    permission_name varchar(255) not null unique,
    primary key (permission_id)
);
--rollback drop table if exists identity.acl_permissions;

--changeset migrate-identity-service:create-acl-resources-table
create table identity.acl_resources (
    resource_id uuid not null,
    resource_name varchar(128) not null unique,
    primary key (resource_id)
);
--rollback drop table if exists identity.acl_resources;

--changeset migrate-identity-service:create-group-kinds-table
create table identity.group_kinds (
    group_kind_id bigserial not null,
    group_kind_name varchar(255) not null,
    primary key (group_kind_id),
    constraint uk_group_kind_name unique (group_kind_name)
);
--rollback drop table if exists identity.group_kinds;

--changeset migrate-identity-service:create-users-sessions-table
create table identity.users_sessions (
    created_at timestamp(6) default CURRENT_TIMESTAMP not null,
    deleted_at timestamp(6) default NULL,
    updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
    session_group_id uuid not null,
    session_user_id uuid not null,
    primary key (session_group_id, session_user_id)
);
--rollback drop table if exists identity.users_sessions;

--changeset migrate-identity-service:create-groups-table
create table identity.groups (
    group_kinds bigint not null,
    group_id uuid not null,
    group_password varchar(255) not null,
    group_owner_id uuid not null,
    group_name varchar(255) not null,
    primary key (group_id)
);

alter table identity.groups 
   add constraint fk_group_kind_id 
   foreign key (group_kinds) 
   references identity.group_kinds;

alter table identity.groups 
   add constraint fk_group_owner_id 
   foreign key (group_owner_id) 
   references identity.users;

--rollback alter table identity.groups drop constraint fk_group_owner_id;
--rollback alter table identity.groups drop constraint fk_group_kind_id;
--rollback drop table if exists identity.groups;

--changeset migrate-identity-service:create-groups-users-table
create table identity.groups_users (
    group_id uuid not null,
    user_id uuid not null,
    primary key (group_id, user_id)
);

alter table identity.groups_users 
   add constraint fk_group_id 
   foreign key (group_id) 
   references identity.groups;

alter table identity.groups_users 
   add constraint fk_user_id 
   foreign key (user_id) 
   references identity.users;

--rollback alter table identity.groups_users drop constraint fk_user_id;
--rollback alter table identity.groups_users drop constraint fk_group_id;
--rollback drop table if exists identity.groups_users;

--changeset migrate-identity-service:create-users-email-confirmation-table
create table identity.users_email_confirmations (
    created_at timestamp(6) default CURRENT_TIMESTAMP not null,
    deleted_at timestamp(6) default NULL,
    updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
    confirmation_token uuid not null,
    confirmed boolean not null default FALSE,
    user_id uuid not null,
    primary key (confirmation_token)
);

alter table identity.users_email_confirmations 
   add constraint fk_email_confirmation_user_id 
   foreign key (user_id) 
   references identity.users;

--rollback alter table identity.users_email_confirmations drop constraint fk_user_id;
--rollback drop table if exists identity.users_email_confirmations;

--changeset migrate-identity-service:create-access-control-list-table
create table identity.access_control_list (
    permission_id bigint not null,
    origin_group_id uuid not null,
    resource_id uuid not null,
    resource_kind_id uuid not null,
    target_group_id uuid not null,
    primary key (permission_id),
    constraint uc_acl_target_origin_perm unique (target_group_id, origin_group_id, permission_id)
);

create index idx_acl_target_origin_perm 
   on identity.access_control_list (target_group_id, origin_group_id, permission_id);

alter table identity.access_control_list 
   add constraint fk_permission_id 
   foreign key (permission_id) 
   references identity.acl_permissions;

alter table identity.access_control_list 
   add constraint fk_origin_group_id 
   foreign key (origin_group_id) 
   references identity.groups;

alter table identity.access_control_list 
   add constraint fk_resource_kind_id 
   foreign key (resource_kind_id) 
   references identity.acl_resources;

alter table identity.access_control_list 
   add constraint fk_target_group_id 
   foreign key (target_group_id) 
   references identity.groups;

--rollback alter table identity.access_control_list drop constraint fk_target_group_id;
--rollback alter table identity.access_control_list drop constraint fk_resource_kind_id;
--rollback alter table identity.access_control_list drop constraint fk_origin_group_id;
--rollback alter table identity.access_control_list drop constraint fk_permission_id;
--rollback drop index identity.idx_acl_target_origin_perm;
--rollback drop table if exists identity.access_control_list;