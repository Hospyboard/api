alter table log_action
    drop column last_login_at;
alter table log_action
    add column user_uuid varchar(255) null;