
#the following three lines are for problems with mySql and should be deleted before the final version
drop schema fellesprosjekt;
create schema fellesprosjekt;
use fellesprosjekt;

create table person( #entity. This is an entity to be created before creating an account for the person
employee_nr int(5) unique not null primary key,
first_name varchar(20) not null,
last_name varchar(20) not null,
mobile_nr varchar(8) not null
);

Create table account( #entity including hasAccount
user_name varChar(10) unique primary key,
employee_nr int(5) unique not null,
/*not null to assure that an account cannot exist without a relation to a person-entity.
unique to ensure that there is 1 and only 1 person corresponding to 1 and only 1 account.*/
foreign key(employee_nr) references person(employee_nr) on delete cascade #hasAccount
);

create table calendar(
calendar_id int(5) not null unique primary key,
user_name varchar(10) unique not null,
activity_id int(5) unique
);

Create table room(
room_name varchar(20) unique primary key,
capacity int(3)
);

create table activity( #entity. includes isOwner-relation and activityRoom
activity_id int(5) unique primary key,
description varchar(256),
activity_date date,
start_time time,
end_time time,
repetition int,
end_date date,
owner_user_name varchar(10) not null, # ensures 1 owner
room_name varchar(20),
foreign key(owner_user_name) references account(user_name), #owner is a reserved word, and owner_user_name is used instead.
foreign key(room_name) references room(room_name)
);

create table hasCalendar(
user_name varchar(10) unique not null primary key,
calendar_id int(5) unique not null,
foreign key(user_name) references account(user_name),
foreign key(calendar_id) references calendar(calendar_id)
);

create table groupHasCalendar(
calendar_id int(5) not null,
group_id int (5) not null,
foreign key(calendar_id) references calendar(calendar_id),
foreign key(group_id) references calendarGroup(group_id)
);

Create table calendarGroup(
group_id int unique not null primary key,
group_name varchar(20) not null
);

create table subGroup( #subgroup-Relation
#subgroup-relation. A group can have many "children" and "many parents". None of which needs to be unique, but the unique constraint checks that there are no duplicate relations.
subgroup_id int,
supergroup_id int,
constraint unique (subgroup_id, supergroup_id),
foreign key(subgroup_id) references calendarGroup(group_id),
foreign key(supergroup_id) references calendarGroup(group_id)
);

create table isMember( #isMember-relation
#this is the relationship, isMember, between calendarGroup and Account. The relationship entity has one field; "role"
group_id int,
user_name varchar(10),
role varchar(10),
primary key(group_id, user_name),
foreign key(group_id) references calendarGroup(group_id) on delete cascade,
foreign key(user_name) references account(user_name) on delete cascade
#delete on cascade is used to assure that the relation is deleted if either the corresponding account or group is deleted
);

create table invited( #relation n-n between account and activity
activity_id int(5),
user_name varchar(10),
role varchar(10)
);

#altering tables for foreign keys

alter table calendar
add foreign key (calendarGroup_id) references calendarGroup(group_id),
add foreign key(user_name) references account(user_name),
add foreign key(activity_id) references activity(activity_id);

alter table invited#problems underneath
add constraint unique (activity_id, user_name),
add foreign key(activity_id) references activity(activity_id) on delete cascade,
add foreign key(user_name) references account(user_name) on delete cascade;