# Backend & Frontend with Docker Compose

This project has two top-level folders only: `backend/` and `frontend/`.

Use this Compose file (in the `backend/` folder) to run both services:

```
docker compose up --build -d
```

- Backend: http://localhost:8080
- Frontend: http://localhost:8081

Stop:

```
docker compose down
```

Notes:
- Backend local configs live in `src/main/resources`. An example `application-local.example.yml` is provided.
- `.env` and `.env.example` are placed in `backend/` for Compose usage.
- `.dockerignore` files are added for smaller contexts and faster builds.