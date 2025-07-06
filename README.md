# Backend-team-B 서비스

Spring Boot 기반의 게시글 API 서버입니다. Redis 캐싱을 활용한 인기 게시글 조회 기능과, Docker + GitHub Actions + EC2 배포를 구성했습니다.

---

## 🛠 기술 스택

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL (Docker)
- Redis (Docker)
- Lombok
- Gradle

### DevOps
- Docker
- Amazon EC2
- GitHub Actions (CI/CD)

---

## ERD (Entity Relationship Diagram)
![image](https://github.com/user-attachments/assets/2ae52206-a234-470b-bc4f-13cbbec662b0)


---

## 선택 기술 설명: Redis 캐시 적용 방식

- `/api/popular` 등 인기 게시글 API는 조회 시 Redis 캐시 확인
- 캐시에 없을 경우 → DB 조회 후 Redis 저장
- 조회수 자체는 DB에 저장 (정확도 유지), 캐시는 빠른 응답을 위한 요약 정보만 저장
- Spring `@Cacheable`, 혹은 `RedisTemplate`을 통해 캐시 로직 구현

---

## Postman 테스트

[### 1. 게시글 등록 (POST)](https://backend-team-b.postman.co/workspace/backend-team-B-Workspace~9849a43a-ce76-428e-897a-761e6f56309d/collection/46095284-34354838-b9bd-4114-8731-c98300802b57?action=share&source=copy-link&creator=46095284)
