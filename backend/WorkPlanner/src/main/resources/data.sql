INSERT INTO
    BASIC_USER(id, username, first_name, last_name, password, account_creation_date, role)
VALUES
    (10001, 'jankow', 'Jan', 'Kowalski', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10002, 'adano', 'Adam', 'Nowak', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10003, 'adami', 'Adam', 'Mickiewicz', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10004, 'ewapia', 'Ewa', 'Piasecka', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10005, 'barjan', 'Barbara', 'Jankowska', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10006, 'marlis', 'Marcin', 'Lis', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10007, 'piowik', 'Piotr', 'Wiktor', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10008, 'kaszad', 'Kasia', 'Zadrożna', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10009, 'karwie', 'Karol', 'Wielki', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10010, 'wiozyg', 'Wioletta', 'Zygmunt', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10011, 'andrkr', 'Andrzej', 'Krawczyk', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10012, 'tommie', 'Tomasz', 'Mielnik', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10013, 'agnzal', 'Agnieszka', 'Zaleska', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10014, 'pawjas', 'Paweł', 'Jasiński', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10015, 'beajar', 'Beata', 'Jarecka', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10016, 'stazub', 'Stanisław', 'Zubek', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10017, 'maglew', 'Magdalena', 'Lewandowska', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10018, 'dormas', 'Dorota', 'Masecka', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10019, 'krzymo', 'Krzysztof', 'Morawski', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10020, 'annkow', 'Anna', 'Kowal', '$2a$10$R2HKWWfsnnFrD1sdJ5P8J.rokFeqsWtQKtyHaZnXgTpqAmvTFDMyi', CURRENT_TIMESTAMP, 'USER'),
    (10021, 'admin', 'Adam', 'Badyl', '$2a$10$Uw98GqUzgj2bs495KY0/MONN8e75mgpI4El5YM.X3uYHMjffbzdZO', CURRENT_TIMESTAMP, 'ADMIN');


INSERT INTO
    TEAM(id, name, description, team_creation_date, team_leader_id)
VALUES
    (20001, 'ASSEMBLY', 'Assembly Team', CURRENT_TIMESTAMP, 10003),
    (20002, 'PAINT_SHOP', 'Paint Shop Team', CURRENT_TIMESTAMP, 10007),
    (20003, 'ENGINEERING', 'Engineering Team', CURRENT_TIMESTAMP, 10011),
    (20004, 'HUMAN_RESOURCES', 'Human Resources Team', CURRENT_TIMESTAMP, 10013),
    (20005, 'MANAGEMENT', 'Management Team', CURRENT_TIMESTAMP, 10021),
    (20006, 'IT_SERVICES', 'IT Services Team', CURRENT_TIMESTAMP, 10018);

INSERT INTO
    PLAN_ENTRY(id, title, start_time, end_time, team_id, user_id, plan_entry_color)
VALUES
    --Assembly Team
    (30001, 'Assembly Line Supervision', '2024-10-07 08:00:00', '2024-10-07 16:00:00', 20001, 10001, 'RED'),
    (30002, 'Parts Quality Inspection', '2024-10-07 10:00:00', '2024-10-07 16:00:00', 20001, 10002, 'BLUE'),
    (30003, 'Machine Calibration', '2024-10-07 10:00:00', '2024-10-07 18:00:00', 20001, 10003, 'GREEN'),
    (30004, 'Inventory Check', '2024-10-07 06:00:00', '2024-10-07 16:00:00', 20001, 10004, 'GRAY'),
    (30005, 'Safety Compliance Review', '2024-10-08 08:00:00', '2024-10-08 16:00:00', 20001, 10001, 'RED'),
    (30006, 'Production Line Setup', '2024-10-08 08:00:00', '2024-10-08 16:00:00', 20001, 10004, 'BLUE'),
    (30007, 'Daily Production Report', '2024-10-09 08:00:00', '2024-10-09 12:00:00', 20001, 10004, 'GREEN'),
    (30008, 'Shift Handover Meeting', '2024-10-08 06:00:00', '2024-10-08 14:00:00', 20001, 10003, 'GRAY'),

    -- Paint Shop Team
    (30009, 'Paint Booth Maintenance', '2024-10-07 08:00:00', '2024-10-07 12:00:00', 20002, 10005, 'GREEN'),
    (30010, 'Color Mix Preparation', '2024-10-07 13:00:00', '2024-10-07 17:00:00', 20002, 10006, 'BLUE'),
    (30011, 'Paint Quality Inspection', '2024-10-08 08:00:00', '2024-10-08 12:00:00', 20002, 10006, 'RED'),
    (30012, 'Sprayer Calibration', '2024-10-08 13:00:00', '2024-10-08 17:00:00', 20002, 10006, 'GRAY'),
    (30013, 'Booth Ventilation Check', '2024-10-09 08:00:00', '2024-10-09 12:00:00', 20002, 10007, 'RED'),
    (30014, 'Paint Thickness Measurement', '2024-10-09 13:00:00', '2024-10-09 17:00:00', 20002, 10008, 'BLUE'),
    (30015, 'Color Matching Process', '2024-10-10 08:00:00', '2024-10-10 12:00:00', 20002, 10005, 'GREEN'),
    (30016, 'Final Inspection of Paint Jobs', '2024-10-10 13:00:00', '2024-10-10 17:00:00', 20002, 10008, 'GRAY'),

    -- Engineering Team
    (30017, 'Prototype Testing', '2024-10-07 08:00:00', '2024-10-07 16:00:00', 20003, 10009, 'RED'),
    (30018, 'Design Review Meeting', '2024-10-08 08:00:00', '2024-10-08 16:00:00', 20003, 10009, 'BLUE'),
    (30019, 'CAD Model Update', '2024-10-09 08:00:00', '2024-10-09 16:00:00', 20003, 10011, 'GREEN'),
    (30020, 'Performance Simulation', '2024-10-10 08:00:00', '2024-10-10 16:00:00', 20003, 10010, 'GRAY'),
    (30021, 'Materials Research', '2024-10-11 08:00:00', '2024-10-11 16:00:00', 20003, 10012, 'RED'),
    (30022, 'Technical Documentation Review', '2024-10-12 08:00:00', '2024-10-12 16:00:00', 20003, 10011, 'BLUE'),
    (30023, 'Engineering Change Order Review', '2024-10-13 08:00:00', '2024-10-13 16:00:00', 20003, 10011, 'GREEN'),
    (30024, 'Client Project Meeting', '2024-10-14 08:00:00', '2024-10-14 16:00:00', 20003, 10012, 'GRAY'),

    -- Human Resources Team
    (30025, 'Employee Onboarding Session', '2024-10-07 08:00:00', '2024-10-07 16:00:00', 20004, 10013, 'GREEN'),
    (30026, 'Monthly Payroll Processing', '2024-10-08 08:00:00', '2024-10-08 16:00:00', 20004, 10013, 'BLUE'),
    (30027, 'Employee Engagement Survey', '2024-10-09 08:00:00', '2024-10-09 16:00:00', 20004, 10014, 'RED'),
    (30028, 'Performance Review Setup', '2024-10-10 08:00:00', '2024-10-10 16:00:00', 20004, 10014, 'GRAY'),
    (30029, 'New Benefits Program Launch', '2024-10-11 08:00:00', '2024-10-11 16:00:00', 20004, 10015, 'RED'),
    (30030, 'Training Needs Analysis', '2024-10-12 08:00:00', '2024-10-12 16:00:00', 20004, 10015, 'BLUE'),
    (30031, 'Conflict Resolution Workshop', '2024-10-13 08:00:00', '2024-10-13 16:00:00', 20004, 10016, 'GREEN'),
    (30032, 'Employee Retention Strategies', '2024-10-14 08:00:00', '2024-10-14 16:00:00', 20004, 10016, 'GRAY'),

    -- Management Team
    (30033, 'Strategic Planning Meeting', '2024-10-07 08:00:00', '2024-10-07 16:00:00', 20005, 10021, 'RED'),
    (30034, 'Budget Review', '2024-10-08 08:00:00', '2024-10-08 16:00:00', 20005, 10014, 'BLUE'),
    (30035, 'Quarterly Performance Review', '2024-10-09 08:00:00', '2024-10-09 16:00:00', 20005, 10021, 'GREEN'),
    (30036, 'Board of Directors Meeting', '2024-10-10 08:00:00', '2024-10-10 16:00:00', 20005, 10021, 'BLUE'),
    (30037, 'Client Presentation', '2024-10-11 08:00:00', '2024-10-11 16:00:00', 20005, 10003, 'GRAY'),
    (30038, 'Project Kickoff Meeting', '2024-10-12 08:00:00', '2024-10-12 16:00:00', 20005, 10013, 'RED'),
    (30039, 'Annual General Meeting', '2024-10-13 08:00:00', '2024-10-13 16:00:00', 20005, 10013, 'BLUE'),
    (30040, 'Sales Strategy Review', '2024-10-14 08:00:00', '2024-10-14 16:00:00', 20005, 10011, 'GREEN'),

    -- IT Services Team
    (30041, 'Hotline Duty', '2024-10-07 08:00:00', '2024-10-07 16:00:00', 20006, 10017, 'BLUE'),
    (30042, 'Server Maintenance', '2024-10-08 08:00:00', '2024-10-08 16:00:00', 20006, 10018, 'GREEN'),
    (30043, 'Network Security Audit', '2024-10-09 08:00:00', '2024-10-09 16:00:00', 20006, 10018, 'RED'),
    (30044, 'Software Patch Update', '2024-10-10 08:00:00', '2024-10-10 16:00:00', 20006, 10019, 'RED'),
    (30045, 'Database Backup', '2024-10-11 08:00:00', '2024-10-11 16:00:00', 20006, 10017, 'GREEN'),
    (30046, 'User Account Management', '2024-10-12 08:00:00', '2024-10-12 16:00:00', 20006, 10020, 'BLUE'),
    (30047, 'Help Desk Support', '2024-10-13 08:00:00', '2024-10-13 16:00:00', 20006, 10020, 'GRAY'),
    (30048, 'Software Testing', '2024-10-14 08:00:00', '2024-10-14 16:00:00', 20006, 10019, 'GREEN');



INSERT INTO
    TEAM_SET_OF_USERS (SET_OF_TEAMS_ID, SET_OF_USERS_ID)
VALUES
    (20001, 10001),
    (20001, 10002),
    (20001, 10003),
    (20001, 10004),
    (20002, 10005),
    (20002, 10006),
    (20002, 10007),
    (20002, 10008),
    (20003, 10009),
    (20003, 10010),
    (20003, 10011),
    (20003, 10012),
    (20004, 10013),
    (20004, 10014),
    (20004, 10015),
    (20004, 10016),
    (20005, 10021),
    (20005, 10003),
    (20005, 10013),
    (20005, 10011),
    (20006, 10017),
    (20006, 10018),
    (20006, 10019),
    (20006, 10020);
