# Branching & CI Guide

This repo uses a split branching model to isolate frontend and backend while keeping `dev/integration` for full-stack verification.

## Branches

- `main` — production-ready (protected)
- `dev/integration` — integration/staging (protected)
- `fe` — frontend-only branch (root = `itdaing-web/` contents)
- `be` (aka `dev/be`) — backend-only branch
- `feature/<name>` and `fix/<name>` underneath `fe` or `be`
- `hotfix/<name>` — fix prod first into `main`, then back-merge to `dev/integration`

## First-time Split (done once)

```bash
# from dev/integration
git checkout dev/integration && git pull --ff-only
# create fe from itdaing-web subtree history
git subtree split -P itdaing-web -b fe
# push
git push origin fe
```

If `itdaing-web/` still exists in `be`/`dev/be`, remove it once there:

```bash
git checkout be
git rm -r --cached itdaing-web
git commit -m "chore: remove frontend folder from backend branch"
git push origin be
```

## Daily Sync

- Pull integration changes into `fe`:

```bash
git checkout fe
git pull --ff-only
# bring itdaing-web/ from integration into fe
git subtree pull -P itdaing-web origin dev/integration -m "sync: from integration" --squash
```

- Push frontend changes into integration:

```bash
git checkout dev/integration
git pull --ff-only
# first time
git subtree add  -P itdaing-web origin fe --squash
# afterwards
git subtree pull -P itdaing-web origin fe -m "merge fe into integration" --squash
```

Rules:
- Make frontend changes only on `fe`, not directly in `dev/integration` (except emergencies; then sync back to `fe`).
- Keep PRs single-responsibility: FE-only or BE-only for `dev/integration`.

## CI Overview

- FE CI (`.github/workflows/fe-ci.yml`): eslint, tsc, vitest, vite build under `itdaing-web/`
- BE CI (`.github/workflows/be-ci.yml`): Gradle tests on JDK 21
- Contract Check (`.github/workflows/contract-check.yml`): Generate OpenAPI via SpringDoc plugin and diff vs base branch using Redocly

## OpenAPI → TS Client (Frontend)

After backend generates spec (`./gradlew openApi`), run:

```bash
pnpm gen:api  # generates client into itdaing-web/src/api
```

It reads `../build/openapi/openapi.yaml` and outputs a typed client using axios.

## Subtree Edge Cases

- Conflict when pulling from integration:

```bash
git checkout fe
git subtree pull -P itdaing-web origin dev/integration -m "pre-sync from integration"
# resolve conflicts, commit

git checkout dev/integration
git subtree pull -P itdaing-web origin fe -m "sync fe → integration"
```

- Accidentally removed `itdaing-web/` in integration:

```bash
git checkout dev/integration
git subtree add -P itdaing-web origin fe --squash
```

- History too heavy: use `--squash` for subtree operations to reduce noise.

## PR Hygiene

- Title prefixes: `[FE]`, `[BE]`, `[CONTRACT]`, `[HOTFIX]`
- Keep changes ~±300 lines
- Checkboxes for OpenAPI/DB migrations in PR template
