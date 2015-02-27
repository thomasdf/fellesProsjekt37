
/*
rekkefølgen for innsetting er viktig her. Entitetene først. Følgende rekkefølge gjelder:
Følgende presedens for entiteter vil alltid virke. Andre rekkefølger kan virke, men trenger ikke virke. Derfor: det er ofte fornuftig å opprette ting i denne rekkefølgen, også i fremtiden.
-Person, Account, Calendar, CalendarGroup
og følgende entiter kan opprettes helt uavhengig av andre entiteter:
- room, 

for relasjonene gjelder det at begge entitetene vi skal relatere må eksistere før relasjonen kan opprettes.

*/

insert into person
values 
(12345, 'Spooderman', 'Kent', '81549300'),
(67890, 'Batman', 'Wayne', '22225555'),
(24680, 'Steve', 'Stiffler', '99224488'),
(13579, 'McLovin\'', '', '23562467'),
(13806, 'John \'Hannlibal\'', 'Smith', '76543210'),
(76487, 'Templeton \'Face\'', 'Peck', '75630389'),
(75894, 'Bosco Albert', 'Baracus', '73311337'),
(87931, 'Howling mad', 'Murloc', '13377331'),
(99883, 'Jim', 'Lahey', '13371337'),
(13371, 'J-', 'Rock', '77337733');

insert into account
values
('Spidey', 12345, 11111),
('manBats', 67890, 22222),
('S.Meister', 24680, 33333),
('McLovin', 13579, 44444),
('Hannibal', 13806, 55555),
('Faceman', 76487, 66666),
('Baracus', 75894, 77777),
('Murloc', 87931, 88888),
('Lahey', 99883, 99999),
('J-Rock', 13371, 00000);

insert into calendar
values
(11111, 'Spidey', null, null),
(22222, 'manBats', null, null),
(33333, 'S.Meister', null, null),
(44444, 'McLovin', null, null),
(55555, 'Hannibal', null, null),
(66666, 'Faceman', null, null),
(77777, 'Baracus', null, null),
(88888, 'Murloc', null, null),
(99999, 'Lahey', null, null),
(00000, 'J-Rock', null, null);


