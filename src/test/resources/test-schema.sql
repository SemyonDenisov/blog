
create table if not exists posts(
                                    id bigserial primary key,
                                    title varchar(30) not null,
                                    text varchar(256) not null,
                                    imageUrl varchar(256) not null,
                                    likes integer,
                                    tags varchar(256) not null
    );


create table if not exists comments(
                                   id bigserial primary key,
                                   text varchar(60) not null
    );

create table if not exists posts_comments(
                                         post_id bigserial,
                                         comment_id bigserial
);