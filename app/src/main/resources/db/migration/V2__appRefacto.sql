create table user_tokens
(
    id              bigint auto_increment primary key,
    created_at      datetime(6)   not null default CURRENT_DATE,
    updated_at      datetime(6)   null,
    uuid            varchar(255)  not null,
    expiration_date datetime(6)   not null,
    token           varchar(2000) not null,
    user_id         bigint        not null,
    constraint unique_key_uuid unique (uuid),
    constraint token_unique_key unique (token) using hash,
    constraint link_table_user foreign key (user_id) references auth_user (id)
);

alter table alert_entity add  created_at   datetime(6)  not null default CURRENT_DATE;
alter table alert_entity add  updated_at   datetime(6)  null;
alter table alert_entity add  uuid         varchar(255) not null;
alter table alert_entity add  constraint   uuid_unique  UNIQUE (uuid);
alter table alert_entity drop alert_uuid;

alter table auth_user add created_at   datetime(6)  not null default CURRENT_DATE;
alter table auth_user add updated_at   datetime(6)  null;
