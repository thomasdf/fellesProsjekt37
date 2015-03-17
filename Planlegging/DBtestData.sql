/*
rekkefølgen for innsetting er viktig her. Entitetene først. Følgende rekkefølge gjelder:
Følgende presedens for entiteter vil alltid virke. Andre rekkefølger kan virke, men trenger ikke virke. Derfor: det er ofte fornuftig å opprette ting i denne rekkefølgen, også i fremtiden.
-Person, Account
-Calendar, Room, CalendarGroup i vilkårlig rekkefølge
-Activity (er avhengig av mye rart, så burde som regel opprettes til slutt).

for relasjonene gjelder det at begge entitetene vi skal relatere må eksistere før relasjonen kan opprettes.

*/

#######################################################################
#entities

insert into account
values
('spidey', 'Spoider','Spooderman', 'Kent', '81549300'),
('manbats', 'noParents','Batman', 'Wayne', '22225555'),
('smeister', 'password', 'Steve', 'Stiffler', '99224488'),
('mclovin', 'soda', 'McLovin\'', '', '23562467'),
('hannibal', 'Ateam1337', 'John \'Hannlibal\'', 'Smith', '76543210'),
('faceman', 'Ateam1337', 'Templeton \'Face\'', 'Peck', '75630389'),
('baracus', 'Ateam1337', 'Bosco Albert', 'Baracus', '73311337'),
('murdoc', 'Ateam1337', 'Howling mad', 'Murdoc', '13377331'),
('lahey', 'police', 'Jim', 'Lahey', '13371337'),
('j-roc', 'roc', 'J-', 'Roc', '77337733');

insert into calendar
values
(11111),#spiderman
(22222),#batman
(33333),#stiffmeister
(44444),#mclovin
(55555),#hannibal
(66666),#faceman
(77777),#baracus
(88888),#murdoc
(99999),#lahey
(01111),#j-roc
(10000), #groupCalendar 1
(20000), #groupCalendar 2
(30000), #groupCalendar 3
(40000); #groupCalendar 4

insert into room
values
('batcave', 2),
('trailer park', 10),
('penthouse', 7),
('-83 GMC G-series', 6);

insert into activity
values
(90909, 99999, 'Cup of Joe', '2015-06-27', '2015-06-27', '07:30:00', '08:00:00', 'lahey', 'trailer park'),
(93939, 99999, 'second cup of coffee', '2015-06-27', '2015-06-27', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(94949, 99999, 'yet another cup of coffee', '2015-06-27', '2015-06-27', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(95959, 99999, 'meeting with j-roc', '2015-06-27', '2015-06-27', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(96969, 99999, 'conference about the secret batcave', '2015-06-27', '2015-06-27', '11:30:00', '12:00:00', 'lahey', 'trailer park'),

(80808, 99999, 'Breakfast with lots of bacon', '2015-06-28', '2015-06-28', '07:30:00', '08:00:00', 'lahey', 'trailer park'),
(83838, 99999, 'meeting with murdoc', '2015-06-28', '2015-06-28', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(84848, 99999, 'go to the supermarket', '2015-06-28', '2015-06-28', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(85858, 99999, 'make coffee', '2015-06-28', '2015-06-28', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(86868, 99999, 'coffee with all the employees (+ Cake)', '2015-06-28', '2015-06-28', '11:30:00', '12:00:00', 'lahey', 'trailer park'),

(70707, 99999, 'Eat the remaining amount of cake, and also drink all the coffee that\'s left from the meeting yesterday. This is a very long acitivity-description. Very very long indeed.', '2015-06-29', '2015-06-30', '07:30:00', '08:00:00', 'lahey', 'trailer park'),
(73737, 99999, 'make even more coffee', '2015-06-29', '2015-06-30', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(74747, 99999, 'drink even more coffee', '2015-06-29', '2015-06-30', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(75757, 99999, 'yell at some of the other employees for not doing your job', '2015-06-29', '2015-06-30', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(76767, 99999, 'sell your one share of the trailer park', '2015-06-29', '2015-06-30', '11:30:00', '12:00:00', 'lahey', 'trailer park'),

(60606, 99999, 'say hello to Randy', '2015-06-01', '2015-06-01', '07:30:00', '08:00:00', 'lahey', 'trailer park'),
(63636, 99999, 'say goodbye to Randy', '2015-06-01', '2015-06-01', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(64646, 99999, 'go to Work', '2015-06-01', '2015-06-01', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(65656, 99999, 'meeting with faceman about the secret A-team mission about to go down', '2015-06-01', '2015-06-01', '10:30:00', '11:00:00', 'lahey', '-83 GMC G-series'),
(66666, 99999, 'go on a mission with the A-team', '2015-06-01', '2015-06-01', '11:30:00', '12:00:00', 'lahey', '-83 GMC G-series'),

(53535, 99999, 'say hello to Randy', '2015-06-02', '2015-06-02', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(54545, 99999, 'make cheeseburgers', '2015-06-02', '2015-06-02', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(45454, 99999, 'sell cheseburgers', '2015-06-02', '2015-06-02', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(46464, 99999, 'make doughnuts', '2015-06-02', '2015-06-02', '11:30:00', '12:00:00', 'lahey', 'trailer park'),
(40404, 99999, 'sell doughtnuts', '2015-06-02', '2015-06-02', '07:30:00', '08:00:00', 'lahey', 'trailer park'),

(30303, 99999, 'job interview at the mall', '2015-06-03', '2015-06-03', '07:30:00', '14:00:00', 'lahey', null),

(21212, 99999, 'buy news-paper from Ricky', '2015-06-04', '2015-06-04', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(22222, 20000, 'get some doughnuts for the job', '2015-06-06', '2015-06-06', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(23232, 99999, 'make cheeseburgers for Randy', '2015-06-07', '2015-06-07', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(24242, 99999, 'Talk about the upcomming shitstorm', '2015-06-08', '2015-06-08', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(25252, 99999, 'do absolutely nothing', '2015-06-10', '2015-06-10', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(91919, 22222, 'planning to save the people of gotham', '2015-04-23', '2015-04-26', '09:45:00', '15:44:00', 'manbats', 'batcave'),
(92929, 44444, 'buy soda for the party', '2015-07-08', '2015-07-08', '14:50:00', '14:59:00', 'mclovin', null);

insert into calendarGroup
values
(12121, 'A-team'),
(13131, 'Roc-pile'),
(14141, 'Superheroes'),
(15151, 'Spidermangroup');


################################################################################
#relations

insert into hasCalendar
values
('spidey', 11111),
('manbats', 22222),
('smeister', 33333),
('mclovin', 44444),
('hannibal', 55555),
('faceman', 66666),
('baracus', 77777),
('murdoc', 88888),
('lahey', 99999),
('j-roc', 01111);

insert into groupHasCalendar
values
(10000, 12121),
(20000, 13131),
(30000, 14141),
(40000, 14141);

insert into isMember
values
(12121, 'hannibal'),
(12121, 'faceman'),
(12121, 'baracus'),
(12121, 'murdoc'),
(13131, 'lahey'),
(13131, 'j-roc'),
(14141, 'manbats'),
(14141, 'spidey'),
(15151, 'spidey');

insert into alarm
values
('manbats', 91919, '12:00:00', 'alert, alert, gotham needs me'),
('lahey', 90909, '13:12:00', 'remember this activity'),

#all employees has a reminder for the coffee-activity
('spidey',86868, '11:00:00','time for coffee'),
('manbats',86868, '11:00:00','time for coffee'),
('mclovin',86868, '11:00:00','time for coffee'),
('hannibal',86868, '11:00:00','time for coffee'),
('smeister',86868, '11:00:00','time for coffee'),
('faceman',86868, '11:00:00','time for coffee'),
('baracus',86868, '11:00:00','time for coffee'),
('murdoc',86868, '11:00:00','time for coffee'),
('j-roc',86868, '11:00:00','time for coffee');

insert into invited
values
#coffee with all employees
(86868, 'spidey', 'true'),
(86868, 'manbats', 'true'),
(86868, 'smeister', 'true'),
(86868, 'mclovin', 'true'),
(86868, 'hannibal', 'false'),
(86868, 'faceman', 'true'),
(86868, 'baracus', 'true'),
(86868, 'murdoc', 'true'),
(86868, 'j-roc', 'true'),

#conference about batcave
(96969, 'manbats', 'true'),
(96969, 'spidey', 'false'),

#meeting murdoc
(83838, 'murdoc', 'true'),

#yell at employees
(75757, 'hannibal', 'false'),
(75757, 'j-roc', 'false'),
(75757, 'faceman', 'false'),

#meeting about secret a-team mission
(65656, 'faceman', 'true'),

#secret mission with the a-team
(66666, 'hannibal', 'true'),
(66666, 'faceman', 'true'),
(66666, 'baracus', 'true'),
(66666, 'murdoc', 'true'),

(90909, 'j-roc', 'false'),
(92929, 'lahey', 'false');
