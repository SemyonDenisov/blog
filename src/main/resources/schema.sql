
create table if not exists posts(
                                    id bigserial primary key,
                                    title varchar(30) not null,
                                    text varchar(256) not null,
                                    imageUrl varchar(256) not null,
                                    likes integer,
                                    tags varchar(256) not null
    );






insert into posts(title, text, imageUrl,likes,tags) values ('Post 1', 'Text 1', 'E:\Yandex\app\Blog\storage\1.jpg',9,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 2', 'Text 2', 'E:\Yandex\app\Blog\storage\2.jpg', 1,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 3', 'Text 3', 'E:\Yandex\app\Blog\storage\3.jpg', 3,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 4', 'Text 1', 'E:\Yandex\app\Blog\storage\4.jpg',9,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 5', 'Text 2', 'E:\Yandex\app\Blog\storage\5.jpg', 1,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 6', 'Text 3', 'E:\Yandex\app\Blog\storage\6.jpg', 3,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 7', 'Text 1', 'E:\Yandex\app\Blog\storage\7.jpg',9,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 8', 'Text 2', 'E:\Yandex\app\Blog\storage\8.jpg', 1,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 9', 'Text 3', 'E:\Yandex\app\Blog\storage\9.jpg', 3,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 10', 'Text 1', 'E:\Yandex\app\Blog\storage\10.jpg',9,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 11', 'Text 2', 'E:\Yandex\app\Blog\storage\11.jpg', 1,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 12', 'Text 3', 'E:\Yandex\app\Blog\storage\12.jpg', 3,'reading');


create table if not exists comments(
                                   id bigserial primary key,
                                   text varchar(60) not null
    );

create table if not exists posts_comments(
                                         post_id bigserial,
                                         comment_id bigserial
);

insert into comments(text) values('Good');
insert into comments(text) values('Bad');
insert into comments(text) values('Not bad');


insert into posts_comments(post_id,comment_id) values(1,1);
insert into posts_comments(post_id,comment_id) values(1,2);
insert into posts_comments(post_id,comment_id) values(3,3);