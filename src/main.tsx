import { createRoot } from "react-dom/client";
import App from "./App";
import "./index.css";
import { UserProvider } from "./context/UserContext";

createRoot(document.getElementById("root")!).render(
	<UserProvider>
		<App />
	</UserProvider>
);

// Register service worker for PWA (only in production)
if ('serviceWorker' in navigator && import.meta.env.PROD) {
	window.addEventListener('load', () => {
		navigator.serviceWorker.register('/sw.js').catch((err) => {
			console.warn('SW registration failed:', err);
		});
	});
}
  