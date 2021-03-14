create table note
(
    id      integer not null
        constraint note_pk
            primary key autoincrement,
    title   text,
    content text
);

create unique index note_id_uindex
    on note (id);