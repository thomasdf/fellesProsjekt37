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
(90909, 99999, 'Getting wasted', '2015-06-27', '2015-06-27', '07:30:00', '20:00:00', 'lahey', 'trailer park'),
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
