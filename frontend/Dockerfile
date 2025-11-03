# Multi-stage build for Vite React app with optional profiling build

# 1) Build stage
FROM node:20-alpine AS build
WORKDIR /app

# Optional: enable profiling build of React by passing --build-arg VITE_REACT_PROFILE=true
ARG VITE_REACT_PROFILE=false
ENV VITE_REACT_PROFILE=${VITE_REACT_PROFILE}

# Build-time API flags used by Vite build
ARG VITE_USE_REAL_API=false
ARG VITE_API_BASE_URL=
ENV VITE_USE_REAL_API=${VITE_USE_REAL_API}
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

# Install deps first (better caching)
COPY package*.json ./
# Use npm ci if lockfile exists; fallback to npm install
RUN if [ -f package-lock.json ]; then npm ci; else npm install --no-audit --no-fund; fi

# Copy source and build
COPY . .
# Use the env var to control profiling build (see vite.config.ts)
RUN npm run build

# 2) Runtime stage (static serving with Nginx)
FROM nginx:alpine AS runtime
# Replace default site with SPA-friendly config
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Copy built assets
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
