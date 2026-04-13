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
    "createdAt": "2026-04-13T14:32:33.7505313",
    "modifiedAt": "2026-04-13T14:32:33.7505313"
}
```

### Response Body 예시 2

```json
{
    "id": 2,
    "title": "여행 계획",
    "content": "일본 여행",
    "author": "김민수",
    "createdAt": "2026-04-13T14:33:18.0285366",
    "modifiedAt": "2026-04-13T14:33:18.0285366"
}
```

### 상태 코드

`201 Created`

### 예외

- 제목, 내용, 작성자명, 비밀번호 중 하나라도 누락 → `400 Bad Request`

---

## 전체 일정 조회

전체 일정을 조회한다.

작성자명은 선택 조건이다.

수정일(modifiedAt) 기준 내림차순으로 조회한다.

### Method / URL

```
GET /schedules
GET /schedules?author=정욱재
```

### Query Parameter

`author` (선택)
값이 존재하면 author와 일치하는 일정 조회
값이 없으면 전체 일정 조회

### Response Body 예시 (전체 조회)

```json
[
    {
        "id": 2,
        "title": "여행 계획",
        "content": "일본 여행",
        "author": "김민수",
        "createdAt": "2026-04-13T14:33:18.028537",
        "modifiedAt": "2026-04-13T14:33:18.028537"
    },
    {
        "id": 1,
        "title": "회의 시작",
        "content": "API 명세서 작성",
        "author": "정욱재",
        "createdAt": "2026-04-13T14:32:33.750531",
        "modifiedAt": "2026-04-13T14:32:33.750531"
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
        "createdAt": "2026-04-13T14:32:33.750531",
        "modifiedAt": "2026-04-13T14:32:33.750531"
    }
]
```

### 상태 코드

`200 OK`

---

## 선택 일정 조회

ID로 단건 일정 조회

### Method / URL

```
GET /schedules/{scheduleId}
```

### Path Variable

`scheduleId` : 일정 ID

### Response Body

```json
{
    "id": 1,
    "title": "회의 시작",
    "content": "API 명세서 작성",
    "author": "정욱재",
    "createdAt": "2026-04-13T14:32:33.750531",
    "modifiedAt": "2026-04-13T14:32:33.750531"
}
```

### 상태 코드

`200 OK`

### 예외

- 해당 ID가 없으면 `404 Not Found`

---

## 일정 수정

선택한 일정의 `제목`, `작성자명`만 수정한다.

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
    "createdAt": "2026-04-13T14:32:33.750531"
}
```

### 상태 코드

`200 OK`

### 예외

- 제목 또는 작성자명 또는 비밀번호 누락 → `400 Bad Request`
- 비밀번호 불일치 → `403 Forbidden`
- 일정 없음 → `404 Not Found`

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

- 비밀번호 누락 → `400 Bad Request`
- 비밀번호 불일치 → `403 Forbidden`
- 일정 없음 → `404 Not Found`