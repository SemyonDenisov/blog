create table if not exists posts(
                                    id bigserial primary key,
                                    title varchar(30) not null,
                                    text varchar(256) not null,
                                    imageUrl varchar(256) not null,
                                    likes integer);

insert into posts(title, text, imageUrl,likes) values ('Пост 1', 'Текст 1', 'imageUrl',9);
insert into posts(title, text, imageUrl,likes) values ('Пост 2', 'Текст 2', 'ImageUrl', 1);
insert into posts(title, text, imageUrl,likes) values ('Пост 3', 'Текст 3', 'ImageUrl', 3);
