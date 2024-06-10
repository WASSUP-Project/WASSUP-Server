INSERT INTO admin_table (create_date, update_date, admin_account_id, name, password, phone_number)
values ('2024-04-01 00:00:00', '2024-04-01 00:00:00', 'admin', '관리자',
        '$2a$10$BQ94EduRIIKGioyoXv4dieZJSAaL10A0Bi1VIF24UqVGe.xMhpd4y', '01012341234');

INSERT INTO group_table (create_date, group_admin_id, update_date, address, business_number, description, email,
                         group_unique_code, image_url, name)
values ('2024-04-01 00:00:00', '1', '2024-04-01 00:00:00', '경기 성남시 분당구 판교역로 166 (백현동, 카카오 판교 아지트) 2층', '123-45-67890',
        '아이들을 사랑으로 가르치는 수학 학원입니다.', '123@gmail.com',
        '12A3B4', 'groupDefault.png', '아이사랑수학학원');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('1', '2024-06-01 00:00:00', '1', '1', '2024-06-01 00:00:00', '2008-06-01', '홍길동', '01012341234',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('1', '2024-06-01 00:00:00', '1', '2', '2024-06-01 00:00:00', '2008-08-01', '이몽룡', '01012341234',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('1', '2024-06-01 00:00:00', '1', '3', '2024-06-01 00:00:00', '2008-09-01', '심청이', '01043214321',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('1', '2024-06-01 00:00:00', '1', '4', '2024-06-01 00:00:00', '2008-06-10', '김철수', '01043214321',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('0', '2024-06-01 00:00:00', '1', '5', '2024-06-01 00:00:00', '2008-07-01', '신짱구', '01012121212',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('0', '2024-06-01 00:00:00', '1', '6', '2024-06-01 00:00:00', '2008-04-15', '고길동', '01034343434',
        '테스트 멤버 입니다.');

INSERT INTO member_table (join_status, create_date, group_id, member_id, update_date, birth, name, phone_number,
                          specifics)
values ('0', '2024-06-01 00:00:00', '1', '7', '2024-06-01 00:00:00', '2008-05-15', '노진구', '01012345678',
        '테스트 멤버 입니다.');