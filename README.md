# Scheduler APP

Spring Boot와 JPA를 활용하여 일정 생성, 조회, 수정, 삭제 기능을 구현한 일정 관리 API 프로젝트입니다.


## 기술 스택

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_4.0.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)

## 주요 기능

- 일정 생성, 조회, 수정, 삭제
- 작성자 기준 일정 조회 기능
- 비밀번호 검증 기반 수정/삭제
- JPA Auditing을 통한 생성일/수정일 자동 관리

## API 요약

기본 실행 주소: `http://localhost:8080`

모든 API는 JSON 형식으로 요청 및 응답합니다.

| Method   | URL                          | 설명         |
|----------|------------------------------|------------|
| `POST`   | `/schedules`                 | 일정 생성      |
| `GET`    | `/schedules`                 | 전체 일정 조회   |
| `GET`    | `/schedules?author={author}` | 작성자별 일정 조회 |
| `GET`    | `/schedules/{scheduleId}`    | 선택 일정 조회   |
| `PATCH`  | `/schedules/{scheduleId}`    | 일정 수정      |
| `DELETE` | `/schedules/{scheduleId}`    | 일정 삭제      |

상세한 내용은 [API 명세서](./docs/api-spec.md)에서 확인할 수 있습니다.

## ERD

![ERD](./docs/ERD.png)

## 프로젝트 구조

```text
 main
 ├─ java/com/wookjae/scheduler
 │  ├─ controller
 │  ├─ dto
 │  ├─ entity
 │  ├─ repository
 │  ├─ service
 │  └─ SchedulerApplication.java
 └─ resources
    └─ application.properties
```
- `controller`: HTTP 요청/응답 처리
- `dto`: 요청 및 응답 데이터 전달
- `entity`: 일정 도메인과 공통 시간 필드 정의
- `repository`: 데이터 접근
- `service`: 일정 생성, 조회, 수정, 삭제 비즈니스 로직 처리