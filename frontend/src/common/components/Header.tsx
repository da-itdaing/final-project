import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../services/authState'

export default function Header() {
  const { user, setUser } = useAuth()
  const navigate = useNavigate()
  return (
    <header className="header">
      <div className="container header-inner">
        <NavLink to="/" className="logo">DA · IT DAING</NavLink>
        <div className="search"><input placeholder="행사/팝업/장소 검색" /></div>
  {/* Role-specific navigation removed for production-like header */}
        {user ? (
          <button className="pill" onClick={() => { setUser(null); navigate('/auth/login') }}>
            로그아웃 · {user.role}
          </button>
        ) : (
          <NavLink to="/auth/login" className={({isActive}) => isActive ? 'active pill' : 'pill'}>로그인</NavLink>
        )}
      </div>
    </header>
  )
}
