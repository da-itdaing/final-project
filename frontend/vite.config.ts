import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
// Optional: enable production profiling build of React by setting VITE_REACT_PROFILE=true
export default defineConfig(({ mode }) => {
  const enableProdProfiling = process.env.VITE_REACT_PROFILE === 'true' || process.env.REACT_PROFILE === 'true'
  return {
    plugins: [react()],
    resolve: {
      alias: enableProdProfiling
        ? [
            { find: 'react-dom/client', replacement: 'react-dom/profiling' },
            { find: 'react-dom', replacement: 'react-dom/profiling' },
          ]
        : [],
    },
  }
})
