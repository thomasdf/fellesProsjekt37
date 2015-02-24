#for å slippe problemer i sql workbench. De tre påfølgende linjene skal slettes
drop schema fellesprosjekt;
create schema fellesprosjekt;
use fellesprosjekt;

#lager account-entiteten, 10 bytes username
Create table account
(
#Limit size userName 10 bytes (chars)
user_name varChar(10) unique primary key,
employee_nr int unique,
activity_id int,
room_name varchar(10),
group_id int
);

#lager gruppeentiteten.
Create table calendarGroup
(
group_id int unique primary key,
group_name varchar(10)
);


#lager relasjonsklassen isMember mellom calendarGroup og account. relasjonsklassen har en felt for rolle
create table isMember
(
group_id int,
user_name varchar(10),
role varchar(10),
primary key(group_id, user_name),
foreign key(group_id) references calendarGroup(group_id) on delete cascade,
foreign key(user_name) references account(user_name) on delete cascade
#bruker on delete cascade for å være sikker på at hvis gruppe eller account slettes skal tilhørende relasjoner også slettes
);


#lager romtabell
Create table room
(
room_name varchar(10) unique primary key,
user_name varchar(10),
foreign key(user_name) references account(user_name) on delete cascade
#relasjon for isAdmin mellom account og room. On delete cascade for at relasjonen skal slettes om en bestemt account slettes.
);

create table person
(
employee_nr int unique primary key,
first_name varchar(10),
last_name varchar(10),
internal_nr char(8), #interntelefonnummer
mobile_nr char(8)
);

create table acitivity
(
activity_id int unique primary key,
description varchar(256),
activity_date date,
start_time time,
end_time time,
repetition int,
end_date date
);

create table attending
(
user_name varchar(10) not null,
activity_id int not null,
role varchar(10) not null,
primary key(user_name, activity_id)
);
