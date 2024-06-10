INSERT INTO admin_table (create_date, update_date, admin_account_id, name, password, phone_number)
values ('2024-04-01 00:00:00', '2024-04-01 00:00:00', 'admin', '관리자',
        '$2a$10$BQ94EduRIIKGioyoXv4dieZJSAaL10A0Bi1VIF24UqVGe.xMhpd4y', '01012341234');

INSERT INTO group_table (create_date, group_admin_id, update_date, address, business_number, description, email,
                         group_unique_code, image_url, name)
values ('2024-04-01 00:00:00', '1', '2024-04-01 00:00:00', '경기 성남시 분당구 판교역로 166 (백현동, 카카오 판교 아지트) 2층', '123-45-67890',
        '테스트 그룹입니다.', '123@gmail.com',
        '12A3B4', 'groupDefault.png', '테스트그룹');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('1', '2024-06-01 00:00:00', '1', '1', '2024-06-01 00:00:00', '2008-06-01', '테스트멤버1', '01012341234',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('1', '2024-06-01 00:00:00', '1', '2', '2024-06-01 00:00:00', '2008-06-01', '테스트멤버2', '01012341234',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('1', '2024-06-01 00:00:00', '1', '3', '2024-06-01 00:00:00', '2008-06-01', '테스트멤버3', '01043214321',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('1', '2024-06-01 00:00:00', '1', '4', '2024-06-01 00:00:00', '2008-06-01', '테스트멤버4', '01043214321',
        '테스트 멤버 입니다.');