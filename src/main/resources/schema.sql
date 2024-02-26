drop table if exists users;
create table users
(
    id         int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name       varchar(128)    not null,
    login      varchar(128)    not null,
    password   varchar(128)    not null
);