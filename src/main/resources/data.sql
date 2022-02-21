insert into guest(id, name) values(null, 'Roger Federer');
insert into guest(id, name) values(null, 'Rafael Nadal');
insert into guest(id, name) values(null, 'Novak Djokovic');

insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Chatrier');
insert into tennis_court(id, name) values(null, 'Wimbledon');

insert
    into
        schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2023-12-20T20:00:00.0', '2023-02-20T21:00:00.0', 1);

insert
    into
        schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2023-10-20T20:00:00.0', '2023-10-20T21:00:00.0', 1);