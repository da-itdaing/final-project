import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import type { User } from './auth'

type Ctx = {
  user: User | null
  setUser: (u: User | null) => void
}

const AuthCtx = createContext<Ctx | undefined>(undefined)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  useEffect(() => {
    const raw = localStorage.getItem('auth:user')
    if (raw) setUser(JSON.parse(raw))
  }, [])
  useEffect(() => {
    if (user) localStorage.setItem('auth:user', JSON.stringify(user))
    else localStorage.removeItem('auth:user')
  }, [user])
  const value = useMemo(() => ({ user, setUser }), [user])
  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>
}

export function useAuth() {
  const v = useContext(AuthCtx)
  if (!v) throw new Error('useAuth must be used within AuthProvider')
  return v
}
