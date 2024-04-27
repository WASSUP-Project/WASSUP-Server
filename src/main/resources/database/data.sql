INSERT INTO admin_table (create_date, update_date, admin_account_id, name, password, phone_number)
values ('2024-04-01 00:00:00', '2024-04-01 00:00:00', 'admin', '관리자', '12341234', '01012341234');

INSERT INTO group_table (create_date, group_admin_id, update_date, address, business_number, description, email,
                         group_unique_code, image_url, name)
values ('2024-04-01 00:00:00', '1', '2024-04-01 00:00:00', '경기 성남시 분당구 판교역로 166 (백현동, 카카오 판교 아지트) 2층', '123-45-67890',
        '테스트 그룹입니다.', '123@gmail.com',
        '12A3B4', 'groupDefault.png', '테스트그룹');
