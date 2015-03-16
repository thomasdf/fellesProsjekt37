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
('smeister', 'shag', 'Steve', 'Stiffler', '99224488'),
('mclovin', 'alcohol', 'McLovin\'', '', '23562467'),
('hannibal', 'Ateam1337', 'John \'Hannlibal\'', 'Smith', '76543210'),
('faceman', 'Ateam1337', 'Templeton \'Face\'', 'Peck', '75630389'),
('baracus', 'Ateam1337', 'Bosco Albert', 'Baracus', '73311337'),
('murdoc', 'Ateam1337', 'Howling mad', 'Murdoc', '13377331'),
('lahey', 'liquor', 'Jim', 'Lahey', '13371337'),
('j-roc', 'mafk', 'J-', 'Roc', '77337733');

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
(90909, 99999, 'Getting wasted', '2015-06-27', '2015-06-27', '07:30:00', '08:00:00', 'lahey', 'trailer park'),
(93939, 99999, 'Getting wasted2', '2015-06-27', '2015-06-27', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(94949, 99999, 'Getting wasted3', '2015-06-27', '2015-06-27', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(95959, 99999, 'Getting wasted4', '2015-06-27', '2015-06-27', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(96969, 99999, 'Getting wasted5', '2015-06-27', '2015-06-27', '11:30:00', '12:00:00', 'lahey', 'trailer park'),

(80808, 99999, 'Getting wasted6', '2015-06-28', '2015-06-28', '07:30:00', '08:00:00', 'lahey', 'trailer park'),
(83838, 99999, 'Getting wasted7', '2015-06-28', '2015-06-28', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(84848, 99999, 'Getting wasted8', '2015-06-28', '2015-06-28', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(85858, 99999, 'Getting wasted9', '2015-06-28', '2015-06-28', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(86868, 99999, 'Getting wasted10', '2015-06-28', '2015-06-28', '11:30:00', '12:00:00', 'lahey', 'trailer park'),

(70707, 99999, 'Getting wasted11', '2015-06-29', '2015-06-30', '07:30:00', '08:00:00', 'lahey', 'trailer park'),
(73737, 99999, 'Getting wasted12', '2015-06-29', '2015-06-30', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(74747, 99999, 'Getting wasted13', '2015-06-29', '2015-06-30', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(75757, 99999, 'Getting wasted14', '2015-06-29', '2015-06-30', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(76767, 99999, 'Getting wasted15', '2015-06-29', '2015-06-30', '11:30:00', '12:00:00', 'lahey', 'trailer park'),

(60606, 99999, 'say hello to Randy', '2015-06-01', '2015-06-01', '07:30:00', '08:00:00', 'lahey', 'trailer park'),
(63636, 99999, 'say hello to Randy2', '2015-06-01', '2015-06-01', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(64646, 99999, 'say hello to Randy3', '2015-06-01', '2015-06-01', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(65656, 99999, 'say hello to Randy4', '2015-06-01', '2015-06-01', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(66666, 99999, 'say hello to Randy5', '2015-06-01', '2015-06-01', '11:30:00', '12:00:00', 'lahey', 'trailer park'),

(53535, 99999, 'say hello to Randy6', '2015-06-02', '2015-06-02', '08:30:00', '09:00:00', 'lahey', 'trailer park'),
(54545, 99999, 'say hello to Randy7', '2015-06-02', '2015-06-02', '09:30:00', '10:00:00', 'lahey', 'trailer park'),
(45454, 99999, 'say hello to Randy8', '2015-06-02', '2015-06-02', '10:30:00', '11:00:00', 'lahey', 'trailer park'),
(46464, 99999, 'say hello to Randy9', '2015-06-02', '2015-06-02', '11:30:00', '12:00:00', 'lahey', 'trailer park'),
(40404, 99999, 'say hello to Randy10', '2015-06-02', '2015-06-02', '07:30:00', '08:00:00', 'lahey', 'trailer park'),

(30303, 99999, 'Call in the heavies on Ricky, Julian and Bubbles', '2015-06-03', '2015-06-03', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(21212, 99999, 'buy weed from Ricky', '2015-06-04', '2015-06-04', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(22222, 99999, 'get some roc vodka', '2015-06-06', '2015-06-06', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(23232, 99999, 'make cheeseburgers for Randy', '2015-06-07', '2015-06-07', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(24242, 99999, 'Talk about the upcomming shitstorm', '2015-06-08', '2015-06-08', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(25252, 99999, 'sober upactivityactivity_id`PRIMARY`', '2015-06-10', '2015-06-10', '07:30:00', '14:00:00', 'lahey', 'trailer park'),

(91919, 22222, 'planning to save the people of gotham', '2015-04-23', '2015-04-26', '09:45:00', '15:44:00', 'manbats', 'batcave'),
(92929, 44444, 'buy alcohol for the great party', '2015-07-08', '2015-07-08', '14:50:00', '14:59:00', 'mclovin', null);

insert into calendarGroup
values
(12121, 'A-team'),
(13131, 'Roc-pile'),
(14141, 'Superheroes'),
(15151, 'Spidermangroup');


################################################################################
#relations

insert into hascalendar
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

insert into grouphascalendar
values
(10000, 12121),
(20000, 13131),
(30000, 14141),
(40000, 14141);

insert into ismember
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
('manbats', 91919, '12:00:00', 'fuk yeah'),
('lahey', 90909, '13:12:00', 'soon');

insert into invited
values
(90909, 'j-roc', 'false'),
(92929, 'lahey', 'false');
