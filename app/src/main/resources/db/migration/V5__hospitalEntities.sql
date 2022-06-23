create table hospital
(
    id         bigint auto_increment primary key,
    created_at datetime(6)  not null,
    updated_at datetime(6)  null,
    uuid       varchar(255) not null,
    address    varchar(255) not null,
    name       varchar(255) not null,
    constraint UK_hospital_public_id unique (uuid)
);

create table service
(
    hospital_id bigint       not null primary key,
    created_at  datetime(6)  not null,
    updated_at  datetime(6)  null,
    uuid        varchar(255) not null,
    name        varchar(255) not null,
    constraint UK_service_public_id unique (uuid),
    constraint FK_link_to_hospital_id foreign key (hospital_id) references hospital (id)
);

create table room
(
    service_id bigint       not null primary key,
    created_at datetime(6)  not null,
    updated_at datetime(6)  null,
    uuid       varchar(255) not null,
    name       varchar(255) not null,
    constraint UK_room_public_id unique (uuid),
    constraint FK_link_to_service_id foreign key (service_id) references service (hospital_id)
);

alter table auth_user add column hospital_room_uuid varchar(255) null;