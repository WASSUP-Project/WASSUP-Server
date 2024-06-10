-- admin 테이블
CREATE TABLE `admin`
(
    `id`               INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `admin_account_id` VARCHAR(255) NOT NULL COMMENT '관리자 ID',
    `password`         VARCHAR(255) NOT NULL,
    `name`             VARCHAR(255) NOT NULL,
    `phone_number`     VARCHAR(255) NOT NULL
);

-- group 테이블
CREATE TABLE `group`
(
    `id`              INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `name`            VARCHAR(255) NOT NULL,
    `description`     VARCHAR(255) NOT NULL,
    `address`         VARCHAR(255),
    `business_number` VARCHAR(255) COMMENT '사업자 번호는 선택',
    `email`           VARCHAR(255) NOT NULL,
    `image_url`       VARCHAR(255),
    `user_id`         INT          NOT NULL COMMENT '그룹에 해당하는 유저 ID'
);

-- announcement 테이블
CREATE TABLE `announcement`
(
    `id`       INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `title`    VARCHAR(255) NOT NULL,
    `content`  TEXT         NOT NULL,
    `group_id` INT          NOT NULL
);

-- member 테이블
CREATE TABLE `member`
(
    `id`              INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `group_id`        INT                          NOT NULL COMMENT '속한 그룹의 ID',
    `name`            VARCHAR(255)                 NOT NULL COMMENT '원생 이름',
    `phone_number`    VARCHAR(255)                 NOT NULL COMMENT '학부모 번호',
    `birth`           DATE                         NOT NULL COMMENT '생년월일',
    `field`           VARCHAR(255) COMMENT '특이사항',
    `status`          ENUM ('WAITING', 'ACCEPTED') NOT NULL DEFAULT 'WAITING' COMMENT '가입 상태 (대기중, 완료)',
    `announcement_id` INT                          NOT NULL COMMENT '공지사항 ID'
);

-- attendance 테이블
CREATE TABLE `attendance`
(
    `id`          INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `status`      ENUM ('ATTENDANCE', 'ABSENCE', 'SICK_LEAVE', 'OTHER') NOT NULL COMMENT '출석, 결석, 병결, 기타',
    `created_at`  DATETIME                                              NOT NULL COMMENT '생성 시간',
    `modified_at` DATETIME                                              NOT NULL COMMENT '수정 시간',
    `member_id`   INT                                                   NOT NULL,
    `group_id`    INT                                                   NOT NULL
);

-- open_days 테이블
CREATE TABLE `open_days`
(
    `id`          INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `create_date` DATE NOT NULL COMMENT '생성 날짜',
    `group_id`    INT  NOT NULL COMMENT '그룹 ID'
);
