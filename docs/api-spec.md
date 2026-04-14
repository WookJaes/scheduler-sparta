## 공통 사항
- Base URL: `http://localhost:8080`
- 모든 API는 JSON 형식으로 요청 및 응답한다.
- 요청 시 `Content-Type: application/json` 헤더를 사용한다.
- 모든 응답에는 비밀번호를 포함하지 않는다.

---

## 일정 생성

일정을 생성한다.

### Method / URL

```
POST /schedules
```

### Request Body 예시 1

```json
{
  "title":"회의 시작",
  "content":"API 명세서 작성",
  "author":"정욱재",
  "password":"0921"
}
```

### Request Body 예시 2

```json
{
  "title":"여행 계획",
  "content":"일본 여행",
  "author":"김민수",
  "password":"1234"
}
```

### Response Body 예시 1

```json
{
  "id": 1,
  "title": "회의 시작",
  "content": "API 명세서 작성",
  "author": "정욱재",
  "createdAt": "2026-04-13T20:52:12.6454828",
  "modifiedAt": "2026-04-13T20:52:12.6454828"
}
```

### Response Body 예시 2

```json
{
  "id": 2,
  "title": "여행 계획",
  "content": "일본 여행",
  "author": "김민수",
  "createdAt": "2026-04-13T20:52:57.3580742",
  "modifiedAt": "2026-04-13T20:52:57.3580742"
}
```

### 상태 코드

`201 Created`

### 예외

- 제목, 내용, 작성자명, 비밀번호 중 하나라도 누락되면 `400 Bad Request`
- 제목이 30자를 초과하면 `400 Bad Request`
- 내용이 200자를 초과하면 `400 Bad Request`

---

## 전체 일정 조회

전체 일정을 조회한다.

작성자명은 선택 조건으로 사용된다.

수정일(modifiedAt) 기준 내림차순으로 조회한다.

### Method / URL

```
GET /schedules
GET /schedules?author=정욱재
```

### Query Parameter

`author` (선택)
- 값이 존재하면 해당 작성자(author)와 일치하는 일정만 조회한다.
- 값이 없으면 전체 일정을 조회한다.

### Response Body 예시 (전체 조회)

```json
[
  {
    "id": 2,
    "title": "여행 계획",
    "content": "일본 여행",
    "author": "김민수",
    "createdAt": "2026-04-13T20:52:57.358074",
    "modifiedAt": "2026-04-13T20:52:57.358074"
  },
  {
    "id": 1,
    "title": "회의 시작",
    "content": "API 명세서 작성",
    "author": "정욱재",
    "createdAt": "2026-04-13T20:52:12.645483",
    "modifiedAt": "2026-04-13T20:52:12.645483"
  }
]
```

### Response Body 예시 (작성자 조회)

```json
[
  {
    "id": 1,
    "title": "회의 시작",
    "content": "API 명세서 작성",
    "author": "정욱재",
    "createdAt": "2026-04-13T20:52:12.645483",
    "modifiedAt": "2026-04-13T20:52:12.645483"
  }
]
```

### 상태 코드

`200 OK`

---

## 선택 일정 조회

ID를 기반으로 단건 일정을 조회하며, 해당 일정에 등록된 댓글 목록도 함께 반환한다.

### Method / URL

```
GET /schedules/{scheduleId}
```

### Path Variable

`scheduleId` : 일정 ID

### Response Body (댓글이 없는 경우)

```json
{
  "id": 1,
  "title": "회의 시작",
  "content": "API 명세서 작성",
  "author": "정욱재",
  "createdAt": "2026-04-13T20:52:12.645483",
  "modifiedAt": "2026-04-13T20:52:12.645483",
  "comments": []
}
```

### Response Body (댓글이 존재하는 경우)

```json
{
  "id": 1,
  "title": "API 명세서 마무리",
  "content": "API 명세서 작성",
  "author": "정욱재",
  "createdAt": "2026-04-13T20:52:12.645483",
  "modifiedAt": "2026-04-13T20:56:28.690015",
  "comments": [
    {
      "id": 1,
      "content": "좋아요",
      "author": "김민수",
      "createdAt": "2026-04-13T21:00:09.353742"
    }
  ]
}
```

### 상태 코드

`200 OK`

### 예외

- 해당 ID가 없으면 `404 Not Found`

---

## 일정 수정

선택한 일정의 `제목`, `작성자명`만 수정한다. (`내용은 수정 불가`)

수정 요청 시 비밀번호를 함께 받는다.

### Method / URL

```
PATCH /schedules/{scheduleId}
```

### 수정 가능 필드

- `title`
- `author`

### 수정 불가 필드

- `content`
- `createdAt`
- `modifiedAt`
- `password`

### Request Body

```json
{
  "title":"API 명세서 마무리",
  "author":"정욱재",
  "password":"0921"
}
```

### Response Body

```json
{
  "id": 1,
  "title": "API 명세서 마무리",
  "author": "정욱재",
  "createdAt": "2026-04-13T20:52:12.645483"
}
```

### 상태 코드

`200 OK`

### 예외

- 제목 또는 작성자명 또는 비밀번호를 누락하면 `400 Bad Request`
- 제목이 30자를 초과하면 `400 Bad Request`
- 비밀번호가 일치하지 않은 경우 `403 Forbidden`
- 등록된 일정이 없으면 `404 Not Found`

---

## 일정 삭제

선택한 일정을 삭제한다.

삭제 요청 시 비밀번호를 함께 받는다.

### Method / URL

```
DELETE /schedules/{scheduleId}
```

### Request Body

```json
{
  "password":"0921"
}
```

### Response Body

없음

### 상태 코드

`204 No Content`

### 예외

- 비밀번호를 누락한 경우 `400 Bad Request`
- 비밀번호가 일치하지 않은 경우 `403 Forbidden`
- 삭제할 일정이 없는 경우 `404 Not Found`

---

## 댓글 작성

선택한 일정에 댓글을 작성한다.

### Method / URL

```
POST /schedules/{scheduleId}/comments
```

### Path Variable

`scheduleId` : 일정 ID

### Request Body

```json
{
  "content":"좋아요",
  "author":"김민수",
  "password":"1234"
}
```

### Response Body

```json
{
  "id": 1,
  "scheduleId": 1,
  "content": "좋아요",
  "author": "김민수",
  "createdAt": "2026-04-13T21:00:09.353742",
  "modifiedAt": "2026-04-13T21:00:09.353742"
}
```

### 상태 코드

`201 Created`

### 예외

- 댓글 내용, 작성자명, 비밀번호 중 하나라도 누락되면 `400 Bad Request`
- 댓글 내용이 100자를 초과하면 `400 Bad Request`
- 해당 일정의 댓글이 10개 이상이면 `400 Bad Request`
- 해당 일정이 없으면 `404 Not Found`