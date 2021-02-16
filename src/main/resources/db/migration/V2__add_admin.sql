insert into user_ (id, username, password, active)
values (1, 'admin', 'vp3m84q3r8cmguq038tm', true);

insert into user_role (user_id, roles)
values (1, 'USER'), (1, 'ADMIN');