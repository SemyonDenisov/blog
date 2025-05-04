
create table if not exists posts(
                                    id bigserial primary key,
                                    title varchar(30) not null,
                                    text varchar(256) not null,
                                    imageUrl varchar(256) not null,
                                    likes integer
    );


create table if not exists tags(
                                    id bigserial primary key,
                                    tag varchar(30) not null
    );

create table if not exists posts_tags(
                                         post_id bigserial,
                                         tag_id bigserial
);


insert into posts(title, text, imageUrl,likes) values ('Пост 1', 'Текст 1', 'imageUrl',9);
insert into posts(title, text, imageUrl,likes) values ('Пост 2', 'Текст 2', 'ImageUrl', 1);
insert into posts(title, text, imageUrl,likes) values ('Пост 3', 'Текст 3', 'ImageUrl', 3);

insert into tags(tag) values('спорт');
insert into tags(tag) values('искусство');
insert into tags(tag) values('политика');


insert into posts_tags(post_id,tag_id) values(1,1);
insert into posts_tags(post_id,tag_id) values(1,2);
insert into posts_tags(post_id,tag_id) values(3,3);

create table if not exists comments(
                                   id bigserial primary key,
                                   text varchar(60) not null
    );

create table if not exists posts_comments(
                                         post_id bigserial,
                                         comment_id bigserial
);

insert into comments(text) values('Класс');
insert into comments(text) values('Не очень ');
insert into comments(text) values('Неплохо');


insert into posts_comments(post_id,comment_id) values(1,1);
insert into posts_comments(post_id,comment_id) values(1,2);
insert into posts_comments(post_id,comment_id) values(3,3);