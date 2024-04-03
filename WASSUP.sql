-- admin 테이블
CREATE TABLE `admin`
(
    `id`           INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `admin_id`     VARCHAR(255) NOT NULL COMMENT '관리자 ID',
    `password`     VARCHAR(255) NOT NULL,
    `name`         VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL
);

-- group 테이블
CREATE TABLE `group`
(
    `id`                     INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `name`                   VARCHAR(255) NOT NULL,
    `representative_name`    VARCHAR(255) NOT NULL,
    `representative_email`   VARCHAR(255) NOT NULL,
    `business_number`        VARCHAR(255) NULL COMMENT '사업자 번호는 선택 (String!! -가 포함됨)',
    `representative_address` VARCHAR(255) NULL,
    `introduction`           VARCHAR(255) NOT NULL,
    `user_id`                INT          NOT NULL COMMENT '그룹에 해당하는 사용자 ID'
);

-- announcement 테이블
CREATE TABLE `announcement`
(
    `id`       INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `title`    VARCHAR(255) NOT NULL,
    `content`  VARCHAR(255) NOT NULL,
    `group_id` INT          NOT NULL COMMENT '그룹의 ID로 여러 공지사항을 관리 가능'
);

-- member 테이블
CREATE TABLE `member`
(
    `id`              INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `name`            VARCHAR(255) NOT NULL COMMENT '원생 이름',
    `phone_number`    VARCHAR(255) NOT NULL COMMENT '번호 뒤 4자리를 잘라서 대조하는 방식 사용',
    `is_matching`     BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '뒷자리 & 얼굴인식 일치 여부',
    `status`          ENUM('WAITING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'WAITING' COMMENT '가입 상태 (대기중, 완료, 취소)',
    `announcement_id` INT          NOT NULL COMMENT '공지사항 ID',
    `group_id`        INT          NOT NULL COMMENT '속한 그룹의 ID'
);

-- attendance 테이블
CREATE TABLE `attendance`
(
    `id`          INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `status`      ENUM('ATTENDANCE', 'ABSENCE', 'SICK_LEAVE', 'OTHER') NOT NULL COMMENT '출석, 결석, 병결, 기타',
    `created_at`  DATETIME NOT NULL COMMENT '생성 시간',
    `modified_at` DATETIME NOT NULL COMMENT '수정 시간',
    `member_id`   INT      NOT NULL
);

-- sent_history 테이블
CREATE TABLE `sent_history`
(
    `id`              INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto Increment',
    `announcement_id` INT NOT NULL,
    `member_id`       INT NOT NULL
);
