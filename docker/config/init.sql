CREATE DATABASE keycloak;
CREATE DATABASE hospybord;

CREATE USER hospyboard_user WITH PASSWORD 'admin';
GRANT USAGE ON SCHEMA keycloak TO hospyboard_user;
GRANT USAGE ON SCHEMA hospybord TO hospyboard_user;
