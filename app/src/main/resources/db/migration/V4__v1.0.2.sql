alter table auth_user add column infos      varchar(10000) null;

drop table hospital_room;
drop table hospital_room_tool;
drop table hospyboard_action;

create table log_action
(
    id          bigint auto_increment primary key,
    uuid        varchar(255) not null,
    when_access datetime(6)  not null default CURRENT_DATE,
    route       varchar(255) not null,
    ip          varchar(255) not null,
    http_method varchar(255) not null,
    created_at  datetime(6)  not null default CURRENT_DATE,
    updated_at  datetime(6)  null,
    constraint  UK_uuid_log_action unique (uuid)
);
