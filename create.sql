create table quote
(
    quote_id  CHAR(16) FOR BIT DATA not null,
    created   timestamp             not null,
    text      varchar(4096)         not null,
    updated   timestamp             not null,
    source_id CHAR(16) FOR BIT DATA,
    primary key (quote_id)
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

create index IDXd3o3ejkgydlmqi8o4uhtibkwk on quote (text);

create unique index UKcpie5uqwth4crepikwt8aiq74 on quote (source_id, text);

create index IDXjwsoi1n9xffw1c10aupg3obyv on source (created);

alter table source
    add constraint UK_4a1uurs8rtj4xnah2j9uguec0 unique (name);

alter table quote
    add constraint FK4gnwxqrpbw5culhb0cxc6lnv0 foreign key (source_id) references source;
