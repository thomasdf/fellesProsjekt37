Create table account
(
#Limit size userName 10 bytes (chars)
user_name varChar(10) unique primary key,
employee_nr int unique,
user_name varchar(10),
activity_id int,
room_name varchar(10),
group_id int,
foreign key(employee_nr) references person(employee_nr), #hasAccount
foreign key(user_name, activity_id) references attending(user_name, activity_id), #accountAttending
foreign key(room_name) references room(room_name), #isAdmin
foreign key(group_id) references calendarGroup(group_id)
);

Create table calendarGroup
(
group_id int unique primary key,
group_name varchar(10)
);

Create table room
(
room_name varchar(10) unique primary key
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
primary key(user_name, activity_id),
foreign key(user_name) references account(user_name),
foreign key(activity_id) references activity(activity_id)
);
