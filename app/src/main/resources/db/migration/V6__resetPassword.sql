create table user_password_reset_codes
(
    id              bigint auto_increment primary key,
    created_at      datetime(6)   not null,
    updated_at      datetime(6)   null,
    uuid            varchar(255)  not null,
    code            varchar(1000) null,
    expiration_date datetime(6)   not null,
    user_id         bigint        not null,
    constraint UK_reset_password_public_id unique (uuid),
    constraint link_to_user_reset foreign key (user_id) references auth_user (id)
);
