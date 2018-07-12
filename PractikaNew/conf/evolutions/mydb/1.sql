# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table dictionary.doctors (
  id_pk_ora                     bigserial not null,
  datebeg                       date,
  dateend                       date,
  dbsource                      varchar(255),
  drcode                        varchar(255),
  fio                           varchar(255),
  lpuwork                       decimal(38),
  name                          varchar(255),
  pension                       varchar(255),
  secname                       varchar(255),
  surname                       varchar(255),
  constraint pk_doctors primary key (id_pk_ora)
);

create table local.usersnew (
  id                            bigserial not null,
  age                           integer,
  dob                           date,
  name                          varchar(255),
  temperature                   float,
  constraint pk_usersnew primary key (id)
);


# --- !Downs

drop table if exists dictionary.doctors cascade;

drop table if exists local.usersnew cascade;

