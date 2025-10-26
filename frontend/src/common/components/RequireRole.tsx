import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../services/authState'

type Props = { role: 'consumer' | 'seller' | 'manager'; children?: React.ReactNode }

export default function RequireRole({ role, children }: Props) {
  const { user } = useAuth()
  const loc = useLocation()
  if (!user) return <Navigate to="/auth/login" state={{ from: loc.pathname }} replace />
  if (user.role !== role) return <Navigate to="/" replace />
  return children ? <>{children}</> : <Outlet />
}
