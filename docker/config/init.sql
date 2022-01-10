CREATE DATABASE keycloak;
CREATE DATABASE hospyboard;

CREATE USER hospyboard_user WITH PASSWORD 'admin';
GRANT USAGE ON SCHEMA keycloak TO hospyboard_user;
GRANT USAGE ON SCHEMA hospyboard TO hospyboard_user;
