#use thomasdf_fellesprosjekt;

drop schema fellesprosjekt;
create schema fellesprosjekt;
use fellesprosjekt;

Create table account( #entity including hasAccount
user_name varChar(10) unique primary key,
user_password varChar(10) not null, #should be scrambled
first_name varchar(20) not null,
last_name varchar(20) not null,
mobile_nr varchar(8)
);

create table calendar(
calendar_id int(5) auto_increment not null unique primary key
);

Create table room(
room_name varchar(20) unique primary key,
capacity int(3)
);

create table activity( #entity. includes isOwner-relation and activityRoom
activity_id int(5) auto_increment unique primary key,
calendar_id int(5) not null,
description varchar(256),
activity_date date,
end_date date,
start_time time,
end_time time,
owner_user_name varchar(10) not null, # ensures 1 owner per activity
room_name varchar(20),
foreign key(owner_user_name) references account(user_name), #en bruker som er admin for en aktivitet skal ikke kunne slettes.
foreign key(room_name) references room(room_name) on delete set null, #dersom et rom slettes vil room_name-feltet settes til null.
foreign key(calendar_id) references calendar(calendar_id) #activityCalendar
);

Create table calendarGroup(
group_id int(5) auto_increment unique not null primary key,
group_name varchar(20) not null
);

create table subGroup( #subgroup-Relation
#subgroup-relation. A group can have many "children" and "many parents". None of which needs to be unique, but the composite primary key ensures that there are no duplicates. To find all children, the supergroup-ID can be usedd.
subGroup_id int,
superGroup_id int,
primary key(supergroup_id, subgroup_id),
foreign key(subgroup_id) references calendarGroup(group_id) on delete cascade,
foreign key(supergroup_id) references calendarGroup(group_id) on delete cascade
);

create table hasCalendar(
user_name varchar(10) unique not null,
calendar_id int(5) unique not null,
primary key(user_name, calendar_id),
foreign key(user_name) references account(user_name) on delete cascade,
foreign key(calendar_id) references calendar(calendar_id) on delete cascade
);

create table groupHasCalendar(
calendar_id int(5) not null,
group_id int (5) not null,
primary key(calendar_id, group_id),
foreign key(calendar_id) references calendar(calendar_id) on delete cascade,
foreign key(group_id) references calendarGroup(group_id) on delete cascade
);

create table isMember( #isMember-relation
#this is the relationship, isMember, between calendarGroup and Account.
group_id int(5),
user_name varchar(10),
primary key(group_id, user_name),
foreign key(group_id) references calendarGroup(group_id) on delete cascade, #hvis en calendardGroup slettes, slettes alle isMember-entiteter tilknyttet denne gruppen
foreign key(user_name) references account(user_name) on delete cascade #hvis en bruker slettes hans medlemsskap fra alle grupper.
#delete on cascade is used to assure that the relation is deleted if either the corresponding account or group is deleted
);

create table invited( #relation n-n between account and activity
activity_id int(5) not null,
user_name varchar(10) not null,
invitation_status varchar(5),
primary key(activity_id, user_name),
foreign key(activity_id) references activity(activity_id) on delete cascade, #hvis en aktivitet slettes, slettes alle invitasjoner til aktiviteten
foreign key(user_name) references account(user_name) on delete cascade #hvis en account slettes, slettes alle invitasjoner tilknyttet denne accounten
);

create table alarm(
user_name varchar(10) not null,
activity_id int(5) not null,
alarm_time time,
description varchar(255),
primary key(user_name, activity_id),
foreign key(user_name) references account(user_name) on delete cascade, #hvis en account slettes, slettes alle alarmer knyttet til accounten, siden alle accounts kan velge sine egne alarmer.
foreign key(activity_id) references activity(activity_id) on delete cascade #hvis en aktivitet slettes slettes alle alarmer tilknyttet aktiviteten.
);

