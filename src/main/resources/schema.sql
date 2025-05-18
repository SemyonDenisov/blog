
create table if not exists posts(
                                    id bigserial primary key,
                                    title varchar(30) not null,
                                    text varchar(350) not null,
                                    imageUrl varchar(70) not null,
                                    likes integer,
                                    tags varchar(256) not null
    );






insert into posts(title, text, imageUrl,likes,tags) values ('Post 1', 'So she was considering in her own mind (as well as she could, ' ||
                                                                      'for the hot day made her feel very sleepy and stupid), ' ||
                                                                      'whether the pleasure of making a daisy-chain would be ' ||
                                                                      'worth the trouble of getting up and picking the daisies, ' ||
                                                                      'when suddenly a White Rabbit with pink eyes ran close by her.',
                                                            'E:\Yandex\app\Blog\storage\1.jpg',9,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 2', 'There was nothing so very remarkable in that; ' ||
                                                                      'nor did Alice think it so very much out of the way to ' ||
                                                                      'hear the Rabbit say to itself, “Oh dear! Oh dear!',
                                                            'E:\Yandex\app\Blog\storage\2.jpg', 1,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 3', 'In another moment down went Alice after it, ' ||
                                                                      'never once considering how in the world she was to get out again.',
                                                            'E:\Yandex\app\Blog\storage\3.jpg', 3,'sport');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 4', 'The rabbit-hole went straight on like a tunnel for some way, and then dipped suddenly down, so suddenly that Alice had not a moment to think about stopping herself before she found herself falling down a very deep well.', 'E:\Yandex\app\Blog\storage\4.jpg',9,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 5', 'Either the well was very deep, or she fell very slowly, for she had plenty of time as she went down to look about her and to wonder what was going to happen next.', 'E:\Yandex\app\Blog\storage\5.jpg', 1,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 6', 'Text 3', 'E:\Yandex\app\Blog\storage\6.jpg', 3,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 7', 'Down, down, down. Would the fall never come to an end?', 'E:\Yandex\app\Blog\storage\7.jpg',9,'health');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 8', 'Alice was not a bit hurt, and she jumped up on to her feet in a moment: she looked up, but it was all dark overhead;', 'E:\Yandex\app\Blog\storage\8.jpg', 1,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 9', 'There were doors all round the hall, but they were all locked; and when Alice had been all the way down one side and up the other, trying every door, she walked sadly down the middle, wondering how she was ever to get out again.', 'E:\Yandex\app\Blog\storage\9.jpg', 3,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 10', 'However, on the second time round, she came upon a low curtain she had not noticed before, and behind it was a little door about fifteen inches high: she tried the little golden key in the lock, and to her great delight it fitted!', 'E:\Yandex\app\Blog\storage\10.jpg',9,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 11', 'Alice opened the door and found that it led into a small passage, not much larger than a rat-hole: she knelt down and looked along the passage into the loveliest garden you ever saw.', 'E:\Yandex\app\Blog\storage\11.jpg', 1,'reading');
insert into posts(title, text, imageUrl,likes,tags) values ('Post 12', 'I think I could, if I only knew how to begin.', 'E:\Yandex\app\Blog\storage\12.jpg', 3,'reading');


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