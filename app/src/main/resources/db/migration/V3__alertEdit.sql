alter table alert_entity drop column patient_uuid;
alter table alert_entity drop column staff_uuid;

alter table alert_entity add column patient_id bigint         not null;
alter table alert_entity add column staff_id   bigint         null;
alter table alert_entity add column infos      varchar(10000) null;

