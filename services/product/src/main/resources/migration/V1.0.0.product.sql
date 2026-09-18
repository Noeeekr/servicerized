--liquibase formatted sql logicalFilePath:migration/V1.0.0.product.sql

--changeset migrate-product-service:create-products-table
    create table product.products (
        product_price integer not null,
        created_at timestamp(6) default CURRENT_TIMESTAMP not null,
        deleted_at timestamp(6) default NULL,
        updated_at timestamp(6) default CURRENT_TIMESTAMP not null,
        product_id uuid not null,
        product_owner_id uuid not null,
        product_description varchar(255),
        product_name varchar(255) not null,
        primary key (product_id)
    );

    alter table product.products 
        add constraint fk_product_owner_id 
        foreign key (product_owner_id) 
        references identity.users;

--rollback alter table product.products drop constraint fk_product_owner_id;
--rollback drop table if exists product.products;