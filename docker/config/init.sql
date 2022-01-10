CREATE DATABASE keycloak;
CREATE DATABASE hospybord_user;
CREATE DATABASE hospybord_notification;
CREATE DATABASE hospybord_alert;
CREATE DATABASE hospybord_hospital;
CREATE DATABASE hospybord_asset;

CREATE USER hospyboard WITH PASSWORD 'admin';
GRANT USAGE ON SCHEMA keycloak TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_user TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_notification TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_alert TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_hospital TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_asset TO hospyboard;
