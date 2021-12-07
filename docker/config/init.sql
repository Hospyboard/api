CREATE DATABASE keycloak;
CREATE DATABASE hospybord_user;
CREATE DATABASE hospybord_notification;
CREATE DATABASE hospybord_alert;
CREATE DATABASE hospybord_hospital;
CREATE DATABASE hospyboard_log_actions;

CREATE USER hospyboard WITH PASSWORD 'admin';
GRANT USAGE ON SCHEMA keycloak TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_user TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_notification TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_alert TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_hospital TO hospyboard;
GRANT USAGE ON SCHEMA hospyboard_log_actions TO hospyboard;
