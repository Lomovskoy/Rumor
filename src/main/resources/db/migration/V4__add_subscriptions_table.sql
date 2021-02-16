create table user_subscriptions
(
    channel_id    int8 not null references user_,
    subscriber_id int8 not null references user_,
    primary key (channel_id, subscriber_id)
)