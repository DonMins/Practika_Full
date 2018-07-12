# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table local.usersnew (
  id                            bigserial not null,
  age                           integer,
  dob                           date,
  name                          varchar(255),
  temperature                   float,
  constraint pk_usersnew primary key (id)
);


# --- !Downs

drop table if exists local.usersnew cascade;

