--liquibase formatted sql logicalFilePath:migration/V1.0.0.product.sql

set client_min_messages = WARNING;

--changeset migrate-product-service:create-products-table
create table products.products (
    product_price integer not null,
    created_at timestamp(6) default CURRENT_TIMESTAMP not null,
    deleted_at timestamp(6) default NULL,
    updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
    product_id uuid not null,
    product_owner_id uuid not null,
    product_description varchar(255),
    product_name varchar(255) not null unique,
    primary key (product_id)
);

--rollback alter table products.products drop constraint if exists fk_product_owner_id;
--rollback drop table if exists products.products cascade;

--changeset migrate-product-service:create-categories-table
create table products.categories (
    created_at timestamp(6) default CURRENT_TIMESTAMP not null,
    deleted_at timestamp(6) default NULL,
    updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
    product_category_id uuid not null,
    product_category_name varchar(255) not null unique,
    primary key (product_category_id)
);

--rollback drop table if exists products.categories cascade;

--changeset migrate-product-service:create-product-categories-relation-table
create table products.products_categories (
    created_at timestamp(6) default CURRENT_TIMESTAMP not null,
    deleted_at timestamp(6) default NULL,
    updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
    category_id uuid not null,
    product_id uuid not null,
    primary key (category_id, product_id)
);

alter table if exists products.products_categories
    add constraint fk_category_id
    foreign key (category_id)
    references products.categories;

alter table if exists products.products_categories
    add constraint fk_category_target_product_id
    foreign key (product_id)
    references products.products;

--rollback alter table if exists products.products_categories drop constraint if exists fk_category_target_product_id;
--rollback alter table if exists products.products_categories drop constraint if exists fk_category_id;
--rollback drop table if exists products.products_categories cascade;

--changeset migrate-product-service:create-product-kinds-table
create table product_kinds (
    product_kind_id bigint not null,
    product_kind_name varchar(255) not null unique,
    primary key (product_kind_id)
);

--rollback drop table if exists product_kinds cascade;

--changeset migrate-product-service:create-products-kinds-relation-table
create table products_kinds (
    kindId bigint not null,
    kind_id bigint not null unique,
    id uuid not null,
    primary key (kindId, id)
);

alter table if exists products_kinds
    add constraint fk_product_kind_id
    foreign key (kind_id)
    references product_kinds;

alter table if exists products_kinds
    add constraint fk_kind_target_product_id
    foreign key (id)
    references products.products;

--rollback alter table if exists products_kinds drop constraint if exists fk_product_id;
--rollback alter table if exists products_kinds drop constraint if exists fk_product_kind_id;
--rollback drop table if exists products_kinds cascade;

--changeset migrate-product-service:create-services-table
create table services (
    provision_hours integer not null,
    id uuid not null,
    primary key (id)
);

alter table if exists services
    add constraint fk_virtual_product_target_product_id
    foreign key (id)
    references products.products;

--rollback alter table if exists services drop constraint if exists fk_virtual_product_target_product_id;
--rollback drop table if exists services cascade;
