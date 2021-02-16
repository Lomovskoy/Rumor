create extension if not exists pgcrypto;

update user_ set password = crypt(password, gen_salt('bf', 8));