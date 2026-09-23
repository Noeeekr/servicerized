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
    
    alter table product.products 
        add constraint fk_product_owner_id 
        foreign key (product_owner_id) 
        references identity.users;

--rollback alter table product.products drop constraint fk_product_owner_id;
--rollback drop table if exists product.products cascade;


--changeset migrate-product-service:create-categories-table

    create table products.categories (
        product_category_id uuid not null,
        product_category_name varchar(255) not null unique,
        primary key (product_category_id)
    );

--rollback drop table if exists products.categories cascade;

--changeset migrate-product-service:create-product-categories-relation-table

    create table products.products_categories (
        category_id uuid not null,
        product_id uuid not null,
        primary key (category_id, product_id)
    );

    alter table if exists products.products_categories 
       add constraint fk_category_id 
       foreign key (category_id) 
       references products.categories;

    alter table if exists products.products_categories 
       add constraint fk_product_id 
       foreign key (product_id) 
       references products.products;

--rollback alter table if exists products.products_categories drop constraint if exists fk_category_id;
--rollback alter table if exists products.products_categories drop constraint if exists fk_product_id;
--rollback drop table if exists products.products_categories cascade;
