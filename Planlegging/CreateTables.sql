
(#the following three lines are for problems with mySql and should be deleted before the final version
drop schema fellesprosjekt;
create schema fellesprosjekt;
use fellesprosjekt;
)

Create table account(
user_name varChar(10) unique primary key,
employee_nr int unique,
activity_id int,
room_name varchar(10),
group_id int
foreign key(employee_nr) references person(employee_nr) on delete cascade #hasAccount
);

Create table calendarGroup(
group_id int unique primary key,
group_name varchar(10)
);

create table subGroup(
#subgroup relation. One group is connected to another group. None of the IDs are unique to ensure an n-n-relation.
#i'm not so sure that this kind of subgroup will work, because this will in fact create a "supergroup" consisting of two groups. I think this will have to be reworked.
group_id1 int,
group_id2 int,
primary key(group_id1, group_id2),
foreign key(group_id1) references calendarGroup(group_id),
foreign key(group_id2) references calendarGroup(group_id)
);

create table isMember(
#this is the relationship, isMember, between calendarGroup and Account. The relationship entity has one field; "role"
group_id int,
user_name varchar(10),
role varchar(10),
primary key(group_id, user_name),
foreign key(group_id) references calendarGroup(group_id) on delete cascade,
foreign key(user_name) references account(user_name) on delete cascade
#delete on cascade is used to assure that the relation is deleted if either the corresponding account or group is deleted
);

Create table room(
room_name varchar(10) unique primary key,
user_name varchar(10),
foreign key(user_name) references account(user_name) on delete cascade
#the relationship isAdmin between accound and room. Delete on cascade to assure that the relation is deletes if its corresponding account is deleted.
);

create table person(
employee_nr int unique primary key,
first_name varchar(10),
last_name varchar(10),
mobile_nr char(8)
);

create table acitivity(
activity_id int unique primary key,
description varchar(256),
activity_date date,
start_time time,
end_time time,
repetition int,
end_date date
);

create table attending(
user_name varchar(10) not null,
activity_id int not null,
role varchar(10) not null,
primary key(user_name, activity_id)
);
