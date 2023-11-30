create table books
(
    id      bigserial primary key,
    title   varchar(255) not null unique,
    author  varchar(255) not null,
    isbn    varchar(255) not null unique,
    status  varchar(255) not null default 'available' check (status in ('available', 'borrowed')),
    user_id bigint,
    constraint fk_books_user foreign key (user_id) references users (id)
)