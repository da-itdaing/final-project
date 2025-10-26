import { useState } from 'react'
import { useNavigate, NavLink, useLocation } from 'react-router-dom'
import { authService, type Role } from '../../common/services/auth'
import { useAuth } from '../../common/services/authState'

const ROLES: Array<{key: Role | 'auto'; label: string}> = [
  { key: 'consumer', label: '소비자' },
  { key: 'seller', label: '판매자' },
]

export default function Login() {
  const [role, setRole] = useState<Role | 'auto'>('consumer')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [err, setErr] = useState('')
  const { setUser } = useAuth()
  const nav = useNavigate()
  const loc = useLocation()

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    const u = await authService.login(role, username, password)
    if (!u) { setErr('아이디/비밀번호를 확인하세요.'); return }
  setUser(u)
  // If redirected from a protected page, go back there first
  const from = (loc.state as { from?: string } | null)?.from
  if (from) { nav(from, { replace: true }); return }
  // Otherwise redirect by role
  if (u.role === 'consumer') nav('/consumer')
  else if (u.role === 'seller') nav('/seller')
  else nav('/manager')
  }

  return (
    <div style={{display:'flex', justifyContent:'center', marginTop: 24}}>
      <form className="card" onSubmit={onSubmit} style={{width: 420}}>
        <div className="pad">
          <h2 style={{textAlign:'center', color:'var(--brand)'}}>DA - IT DAING</h2>
          <div className="tabs" role="tablist">
            {ROLES.map(r => (
              <button type="button" key={r.key} className={`tab ${role===r.key?'active':''}`} onClick={() => setRole(r.key)}>{r.label}</button>
            ))}
          </div>
          <div className="field"><input placeholder="아이디를 입력하세요" value={username} onChange={e=>setUsername(e.target.value)} /></div>
          <div className="field"><input placeholder="비밀번호를 입력하세요" type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
          {err && <div className="muted" style={{color:'#d32f2f', marginTop: 6}}>{err}</div>}

          <div className="row" style={{alignItems:'center', gap:12, marginTop: 10}}>
            <button type="button" className="pill" title="카카오 로그인">카카오</button>
          </div>

          <button type="submit" className="cta">Login</button>

          <div className="row" style={{justifyContent:'space-between', marginTop: 10}}>
            <NavLink to="/auth/join/consumer/info" className="muted">회원가입</NavLink>
            <div style={{display:'flex', gap:12}}>
              <NavLink to="/auth/find-id" className="muted">ID 찾기</NavLink>
              <NavLink to="/auth/find-pw" className="muted">PW 찾기</NavLink>
            </div>
          </div>
        </div>
      </form>
    </div>
  )
}
