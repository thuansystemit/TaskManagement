```markdown
Angular Client
      |
      | POST /api/auth/login
      v
API Gateway (port 8080)
      |
      | route to /api/auth/** → user-service (port 8081)
      v
User Service (AuthController)
      |
      | validates user, returns JWT
      v
API Gateway forwards response
      |
      v
Angular receives JWT
      |
      | Subsequent requests with Authorization: Bearer <token>
      v
API Gateway → routes to user-service / task-service

```