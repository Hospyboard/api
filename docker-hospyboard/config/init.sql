CREATE DATABASE IF NOT EXISTS hospyboard_docker;

CREATE USER 'admin'@'%' IDENTIFIED BY 'adminpass';
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%';
