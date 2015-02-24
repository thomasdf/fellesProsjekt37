drop schema fellesprosjekt;
create schema fellesprosjekt;
use fellesprosjekt;

Create table account
(
#Limit size userName 10 bytes (chars)
user_name varChar(10) unique primary key,
employee_nr int unique,
activity_id int,
room_name varchar(10),
group_id int
);

Create table calendarGroup
(
group_id int unique primary key,
group_name varchar(10)
);

create table isMember
(
group_id int,
user_name varchar(10),
role varchar(10),
primary key(group_id, user_name),
foreign key(group_id) references calendarGroup(group_id) on delete cascade,
foreign key(user_name) references account(user_name) on delete cascade
);

Create table room
(
room_name varchar(10) unique primary key,
activity_id int unique
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
