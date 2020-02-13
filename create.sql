create table quote
(
    quote_id CHAR(16) FOR BIT DATA not null,
    created  timestamp             not null,
    text     varchar(4096)         not null,
    updated  timestamp             not null,
    primary key (quote_id)
);
create table quote_sources
(
    quote_id  CHAR(16) FOR BIT DATA not null,
    source_id CHAR(16) FOR BIT DATA not null
);
create table source
(
    source_id CHAR(16) FOR BIT DATA not null,
    created   timestamp             not null,
    name      varchar(1024)         not null,
    updated   timestamp             not null,
    primary key (source_id)
);
create index IDXjur47yst2mbsaj3h6ppomf3kh on quote (created);
alter table quote
    add constraint UK_d3o3ejkgydlmqi8o4uhtibkwk unique (text);
create index IDXjwsoi1n9xffw1c10aupg3obyv on source (created);
alter table source
    add constraint UK_4a1uurs8rtj4xnah2j9uguec0 unique (name);
alter table quote_sources
    add constraint FKot1d45b0l8e1ffe0ggndnlsbr foreign key (source_id) references source;
alter table quote_sources
    add constraint FKgidslffhug0bnrra9c5d0vqcj foreign key (quote_id) references quote;
