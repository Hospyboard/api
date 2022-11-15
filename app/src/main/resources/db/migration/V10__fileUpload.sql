create table file_entity
(
    id         bigint auto_increment primary key,
    created_at datetime(6)  not null,
    updated_at datetime(6)  null,
    uuid       varchar(255) not null,
    file_name  varchar(255) not null,
    file_path  varchar(255) not null,
    file_size  bigint       not null,
    file_owner bigint       not null,
    constraint UK_file_public_id unique (uuid),
    constraint link_file_and_user foreign key (file_owner) references auth_user (id)
);