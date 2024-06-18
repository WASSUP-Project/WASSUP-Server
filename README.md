## WASSUP-Server

성공회대학교 소프트웨어 캡스톤 디자인 프로젝트입니다.

## 📌 와썹 소개

- **WASSUP**은 학원에서 사용할 수 있는 원생 출결 관리 서비스입니다.
- 데스크에서 쉽게 출석할 수 있습니다.
- 안심 등하원 메시지를 제공합니다.
- 관리자는 출석 현황을 확인할 수 있고, 간단하게 출석 상태를 변경할 수 있습니다.
- 관리자는 여러 개의 그룹을 생성하고, 그룹별로 출석 현황을 확인할 수 있습니다.
- 관리자는 개인 또는 그룹별로 공지사항(가정통신문)을 작성할 수 있습니다.
- 관리자는 그룹에 속한 인원들의 정보를 관리할 수 있습니다.

## 📌 와썹 미리보기

<img src="https://github.com/WASSUP-Project/WASSUP-Web/blob/main/images/wassup_main.png?raw=true" width="830" height="670">

<img src="https://github.com/WASSUP-Project/WASSUP-Web/blob/main/images/wassup_group.png?raw=true" width="830" height="610">

<img src="https://github.com/WASSUP-Project/WASSUP-Web/blob/main/images/wassup_pad.png?raw=true" width="830" height="780">

<img src="https://github.com/WASSUP-Project/WASSUP-Web/raw/main/public/groupAttendance.png" width="830" height="600">

<img src="https://github.com/WASSUP-Project/WASSUP-Web/blob/main/images/wassup_attendance.png?raw=true" width="830" height="610">

<img src="https://github.com/WASSUP-Project/WASSUP-Web/raw/main/public/groupNotice.png" width="830" height="610">

## 📌 프로젝트 기간

- `2024.03` ~ `2024.6`

## 📌 팀원

|                                                        Yehyeok Bang                                                        |                                                       Jiyun Lee                                                        |
|:--------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------:|
| <a href="https://github.com/YehyeokBang"><img src="https://avatars.githubusercontent.com/u/107793780?v=4" width="170"></a> | <a href="https://github.com/dd-jiyun"><img src="https://avatars.githubusercontent.com/u/84395062?v=4" width="170"></a> |
|                                                            Web                                                             |                                                         Server                                                         |

## 📌 프로젝트 구조

![Architecture](https://github.com/WASSUP-Project/WASSUP-Web/blob/main/images/wassup_architecture.png?raw=true)

| Tech            | Version |
|:----------------|:--------|
| Spring Boot     | 3.2.3   | 
| Java            | 17      | 
| Spring Data JPA | 3.3.0   | 
| Spring Security | 3.3.0   | 
| JWT             | 0.11.5  | 
| MySQL           | 8.4.0   | 
| Nurigo          | 4.3.0   | 
| Mail            | 1.4.0   | 
| Caffeine Cache  | 3.1.8   |
| Swagger         | 2.5.0   |

## 📌 개발 환경

- `Java 17`
- **Framework** : Spring Boot 3.2.3
- **IDE** : IntelliJ IDEA
- **Database** : MySQL
 
## 📌 실행 방법

```
# MySQL 설치
- ddl-auto 옵션이 create로 설정되어 있어, 서버 실행 시 테이블이 자동 생성됩니다.

# 서버 실행
$ git clone
$ cd WASSUP-Server
$ ./gradlew build
$ {환경변수키=값} java -jar WASSUP-0.0.1-SNAPSHOT.jar

로컬에서 접속 : http://localhost:8080
Swagger 접속 : http://localhost:8080/swagger-ui.html
```
