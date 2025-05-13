
create table if not exists posts(
                                    id bigserial primary key,
                                    title varchar(30) not null,
                                    text varchar(256) not null,
                                    imageUrl varchar(256) not null,
                                    likes integer,
                                    tags varchar(256) not null
    );






insert into posts(title, text, imageUrl,likes,tags) values ('Post 1', 'Text 1', 'imageUrl',9,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 2', 'Text 2', 'ImageUrl', 1,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 3', 'Text 3', 'ImageUrl', 3,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 4', 'Text 1', 'imageUrl',9,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 5', 'Text 2', 'ImageUrl', 1,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 6', 'Text 3', 'ImageUrl', 3,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 7', 'Text 1', 'imageUrl',9,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 8', 'Text 2', 'ImageUrl', 1,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 9', 'Text 3', 'ImageUrl', 3,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 10', 'Text 1', 'imageUrl',9,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 11', 'Text 2', 'ImageUrl', 1,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 12', 'Text 3', 'ImageUrl', 3,'reading');


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