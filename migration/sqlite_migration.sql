create table main._admins
(
    id              TEXT               not null
        primary key,
    avatar          INTEGER default 0  not null,
    email           TEXT               not null
        unique,
    tokenKey        TEXT               not null
        unique,
    passwordHash    TEXT               not null,
    lastResetSentAt TEXT    default "" not null,
    created         TEXT    default "" not null,
    updated         TEXT    default "" not null
);

create table main._collections
(
    id         TEXT                   not null
        primary key,
    system     BOOLEAN default FALSE  not null,
    type       TEXT    default "base" not null,
    name       TEXT                   not null
        unique,
    schema     JSON    default "[]"   not null,
    listRule   TEXT    default NULL,
    viewRule   TEXT    default NULL,
    createRule TEXT    default NULL,
    updateRule TEXT    default NULL,
    deleteRule TEXT    default NULL,
    options    JSON    default "{}"   not null,
    created    TEXT    default ""     not null,
    updated    TEXT    default ""     not null
);

create table main._externalAuths
(
    id           TEXT            not null
        primary key,
    collectionId TEXT            not null
        references main._collections
            on update cascade on delete cascade,
    recordId     TEXT            not null,
    provider     TEXT            not null,
    providerId   TEXT            not null,
    created      TEXT default "" not null,
    updated      TEXT default "" not null
);

create unique index main._externalAuths_provider_providerId_idx
    on main._externalAuths (provider, providerId);

create unique index main._externalAuths_record_provider_idx
    on main._externalAuths (collectionId, recordId, provider);

create table main._migrations
(
    file    VARCHAR(255) not null
        primary key,
    applied INTEGER      not null
);

create table main._params
(
    id      TEXT            not null
        primary key,
    key     TEXT            not null
        unique,
    value   JSON default NULL,
    created TEXT default "" not null,
    updated TEXT default "" not null
);

create table main.avatars
(
    avatar  TEXT default '',
    created TEXT default '' not null,
    id      TEXT            not null
        primary key,
    updated TEXT default '' not null
);

create index main._5jgeb8cyqwxydg1_created_idx
    on main.avatars (created);

create table main.library
(
    anime_id           TEXT default '',
    image              TEXT default '',
    created            TEXT default '' not null,
    total_episodes     REAL default 0,
    id                 TEXT            not null
        primary key,
    cover              TEXT default '',
    rating             REAL default 0,
    status             TEXT default '',
    title              TEXT default '',
    updated            TEXT default '' not null,
    user_id            TEXT default '',
    lasts_seen_episode REAL default 0,
    watch_status       TEXT default ''
);

create index main._biqhmpugafdxq8e_created_idx
    on main.library (created);

create unique index main.unq_user_anime_id_lib
    on main.library (user_id, anime_id);

create unique index main.unq_user_anime_lib
    on main.library (user_id, anime_id);

create unique index main.unq_user_anime_lib_lib
    on main.library (user_id, title);

create table main.sqlite_master
(
    type     TEXT,
    name     TEXT,
    tbl_name TEXT,
    rootpage INT,
    sql      TEXT
);

create table main.users
(
    avatar                 TEXT    default '',
    created                TEXT    default ''    not null,
    email                  TEXT    default ''    not null,
    emailVisibility        BOOLEAN default FALSE not null,
    id                     TEXT                  not null
        primary key,
    lastResetSentAt        TEXT    default ''    not null,
    lastVerificationSentAt TEXT    default ''    not null,
    passwordHash           TEXT                  not null,
    tokenKey               TEXT                  not null,
    updated                TEXT    default ''    not null,
    username               TEXT                  not null,
    verified               BOOLEAN default FALSE not null,
    last_login             TEXT    default '',
    locale                 TEXT    default '',
    given_name             TEXT    default '',
    family_name            TEXT    default ''
);

create index main.__pb_users_auth__created_idx
    on main.users (created);

create unique index main.__pb_users_auth__email_idx
    on main.users (email)
    where ` email ` != '';

create unique index main.__pb_users_auth__tokenKey_idx
    on main.users (tokenKey);

create unique index main.__pb_users_auth__username_idx
    on main.users (username);
