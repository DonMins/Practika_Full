# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table dictionary.reestr (
  id                            bigserial not null,
  code                          varchar(255),
  date                          date,
  description                   varchar(255),
  name                          varchar(255),
  owner                         varchar(255),
  region                        decimal(38),
  version                       decimal(38),
  constraint pk_reestr primary key (id)
);

create table dictionary.users (
  id                            integer not null,
  login                         varchar(255),
  password                      varchar(255),
  region                        integer,
  is_admin                      boolean default false not null,
  constraint pk_users primary key (id)
);
create sequence dictionary.user_id_seq;

create table local.usersnew (
  id                            bigserial not null,
  age                           integer,
  dob                           date,
  name                          varchar(255),
  temperature                   float,
  constraint pk_usersnew primary key (id)
);


# --- !Downs

drop table if exists dictionary.reestr cascade;

drop table if exists dictionary.users cascade;
drop sequence if exists dictionary.user_id_seq;

drop table if exists local.usersnew cascade;

