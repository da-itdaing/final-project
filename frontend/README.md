# Frontend (React + Vite + TypeScript)

This app ships three role-specific sections besides the common Home page:
- Consumer: `/consumer/*`
- Seller: `/seller/*`
- Manager: `/manager/*`

All data is mocked in `src/common/data/dummy.ts` and exposed through `src/common/services/api.ts`.

## Scripts

- `npm run dev` — Start dev server (http://localhost:5173)
- `npm run build` — Type-check and build to `dist/`
- `npm run preview` — Preview the production build

## Quick start

```bash
cd frontend
npm install
npm run dev
```

Build and preview:

```bash
npm run build
npm run preview
```

## Folder structure

```
frontend/
├─ index.html
├─ tsconfig.json
├─ vite.config.ts
├─ src/
│  ├─ main.tsx
│  ├─ styles.css
│  ├─ routes/
│  │  └─ AppRoutes.tsx
│  ├─ screens/
│  │  └─ Home.tsx
│  ├─ types/
│  │  └─ index.ts
│  ├─ common/
│  │  ├─ components/
│  │  │  ├─ BottomTab.tsx
│  │  │  ├─ Card.tsx
│  │  │  ├─ Carousel.tsx
│  │  │  ├─ Chips.tsx
│  │  │  ├─ Header.tsx
│  │  │  └─ SectionHeader.tsx
│  │  ├─ layouts/
│  │  │  └─ BaseLayout.tsx
│  │  ├─ data/
│  │  │  └─ dummy.ts
│  │  └─ services/
│  │     └─ api.ts
│  ├─ consumer/
│  │  ├─ ConsumerRoutes.tsx
│  │  └─ pages/
│  │     ├─ Dashboard.tsx
│  │     ├─ Events.tsx
│  │     └─ Popups.tsx
│  ├─ seller/
│  │  ├─ SellerRoutes.tsx
│  │  └─ pages/
│  │     ├─ Dashboard.tsx
│  │     ├─ Orders.tsx
│  │     └─ Products.tsx
│  └─ manager/
│     ├─ ManagerRoutes.tsx
│     └─ pages/
│        ├─ Approvals.tsx
│        ├─ Dashboard.tsx
│        └─ Reports.tsx
```

## Notes

- The UI is intentionally minimal and styled via `styles.css`.
- Replace dummy data with real APIs later; the `api.ts` module is a good seam to swap implementations.
