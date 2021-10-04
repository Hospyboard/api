CREATE DATABASE keycloak;
CREATE DATABASE hospybord_user;

CREATE USER hospyboard WITH PASSWORD 'admin';
GRANT USAGE ON SCHEMA keycloak TO hospyboard;
GRANT USAGE ON SCHEMA hospybord_user TO hospyboard;