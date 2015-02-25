
(#the following three lines are for problems with mySql and should be deleted before the final version
drop schema fellesprosjekt;
create schema fellesprosjekt;
use fellesprosjekt;
)

Create table account(
user_name varChar(10) unique primary key,
employee_nr int unique not null,
/*not null to assure that an account cannot exist without a relation to a person-entity.
unique to ensure that there is 1 and only 1 person corresponding to 1 and only 1 account.*/
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
#subgroup-relation. A group can have many "children" and "many parents". None of which needs to be unique, but the unique constraint checks that there are no duplicate relations.
subgroup_id int,
supergroup_id int,
constraint unique (subgroup_id, supergroup_id),
foreign key(subgroup_id) references calendarGroup(group_id),
foreign key(supergroup_id) references calendarGroup(group_id)
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
end_date date,
owner_user_name varchar(10) unique, # ensures 1 owner
foreign key(owner_user_name) references account(user_name) #owner is a reserved word, and owner_user_name is used instead.
);

create table invited(
activity_id int,
user_name varchar(10),
primary key (activity_id, user_name), #to ensure that no duplicated invitations to one activity exists.
foreign key(activity_id) references activity(activity_id) on delete cascade,
foreign key(user_name) references account(user_name) on delete cascade
);

create table attending(
user_name varchar(10) not null,
activity_id int not null,
role varchar(10) not null,
primary key(user_name, activity_id)
);
