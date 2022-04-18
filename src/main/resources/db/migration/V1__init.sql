create table auth_user
(
    id         bigint auto_increment primary key,
    uuid       varchar(255) not null,
    username   varchar(255) not null,
    email      varchar(255) not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    password   varchar(255) not null,
    user_role  varchar(255) not null,
    constraint UK_uuid unique (uuid),
    constraint UK_username unique (username)
);

create table alert_entity
(
    id           bigint auto_increment primary key,
    alert_uuid   varchar(255) not null,
    importance   varchar(255) not null,
    patient_uuid varchar(255) not null,
    staff_uuid   varchar(255),
    status       varchar(255) not null,
    type         varchar(255) not null,
    constraint UK_alert_uuid unique (alert_uuid)
);

create table hospital_room
(
    id          bigint auto_increment primary key,
    uuid        varchar(255) not null,
    room_number varchar(255),
    constraint UK_uuid unique (uuid)
);

create table hospital_room_tool
(
    id        bigint auto_increment primary key,
    uuid      varchar(255) not null,
    tool_type varchar(255),
    constraint UK_uuid unique (uuid)
);

create table hospyboard_action
(
    id           bigint auto_increment primary key,
    requested_at varchar(255),
    route_name   varchar(255),
    service      varchar(255),
    user_uuid    varchar(255),
    uuid         varchar(255),
    constraint UK_uuid unique (uuid)
);
