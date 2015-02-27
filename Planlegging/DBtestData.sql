
/*
rekkefølgen for innsetting er viktig her. Entitetene først. Følgende rekkefølge gjelder:
Følgende presedens for entiteter vil alltid virke. Andre rekkefølger kan virke, men trenger ikke virke. Derfor: det er ofte fornuftig å opprette ting i denne rekkefølgen, også i fremtiden.
-Person, Account, Calendar, Activity, CalendarGroup 
og følgende entiter kan opprettes helt uavhengig av andre entiteter:
- room, 

for relasjonene gjelder det at begge entitetene vi skal relatere må eksistere før relasjonen kan opprettes.

*/

#######################################################################
#entities

insert into person
values
(12345, 'Spooderman', 'Kent', '81549300'),
(67890, 'Batman', 'Wayne', '22225555'),
(24680, 'Steve', 'Stiffler', '99224488'),
(13579, 'McLovin\'', '', '23562467'),
(13806, 'John \'Hannlibal\'', 'Smith', '76543210'),
(76487, 'Templeton \'Face\'', 'Peck', '75630389'),
(75894, 'Bosco Albert', 'Baracus', '73311337'),
(87931, 'Howling mad', 'Murdoc', '13377331'),
(99883, 'Jim', 'Lahey', '13371337'),
(13371, 'J-', 'Roc', '77337733');

insert into account
values
('Spidey', 12345),
('manBats', 67890),
('S.Meister', 24680),
('McLovin', 13579),
('Hannibal', 13806),
('Faceman', 76487),
('Baracus', 75894),
('Murdoc', 87931),
('Lahey', 99883),
('J-Roc', 13371);

insert into calendar
values
(11111),
(22222),
(33333),
(44444),
(55555),
(66666),
(77777),
(88888),
(99999),
(00000),
(10000), #groupCalendar 1
(20000); #groupCalendar 2

insert into activity
values
(90909, 99999, 'Getting wasted', '2015-06-27', '2015-06-27', '07:30:00', '20:00:00', 'Lahey', 'trailer park'),
(91919, 22222, 'planning to save the people of gotham', '2015-04-23', '2015-04-26', '09:45:00', '15:44:00', 'manBats', 'batcave'),
(92929, 44444, 'buy alcohol for the great party', '2015-07-08', '2015-07-08', '14:50:00', '14:59:00', 'McLovin', 'penthouse');

insert into calendarGroup
values
(12121, 'A-team'),
(13131, 'Roc-pile'),
(14141, 'Superheroes');

insert into room
values
('batcave', 2),
('trailer park', 10),
('penthouse', 7),
('-83 GMC G-series', 6);

insert into hascalendar
values
('Spidey', 11111),
('manBats', 22222),
('S.Meister', 33333),
('McLovin', 44444),
('Hannibal', 55555),
('Faceman', 66666),
('Baracus', 77777),
('Murloc', 88888),
('Lahey', 99999),
('J-Rock', 00000);


################################################################################
#relations



