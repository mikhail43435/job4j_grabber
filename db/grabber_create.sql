create table if not exists
post(
    id serial primary key,
    name varchar(200),
    text varchar(10000),
    link varchar(255) NOT NULL,
    created_date timestamp,
    CONSTRAINT link_unique UNIQUE (link)
);