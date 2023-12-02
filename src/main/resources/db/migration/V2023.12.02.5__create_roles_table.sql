create table roles
(
    id   bigserial primary key,
    name varchar(50) not null unique
);

insert into roles(name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');