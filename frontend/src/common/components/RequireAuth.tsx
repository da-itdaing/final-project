import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../services/authState'

export default function RequireAuth({ children }: { children?: React.ReactNode }) {
  const { user } = useAuth()
  const loc = useLocation()
  if (!user) return <Navigate to={`/auth/login`} state={{ from: loc.pathname }} replace />
  return children ? <>{children}</> : <Outlet />
}
