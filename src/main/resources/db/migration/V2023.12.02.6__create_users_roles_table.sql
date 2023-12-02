create table users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    unique (user_id, role_id),
    constraint fk_users_roles_users foreign key (user_id) references users (id),
    constraint fk_users_roles_roles foreign key (role_id) references roles (id)
);

insert into users_roles(user_id, role_id)
values(1, 1),
      (1, 2),
      (2, 1),
      (3, 1);