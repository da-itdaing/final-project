import { NavLink } from 'react-router-dom'

export default function BottomTab() {
  return (
    <nav className="bottom-tab">
      <NavLink to="/consumer/popups" className={({isActive}) => isActive ? 'active' : ''}>
        <span role="img" aria-label="search">🔎</span>
        <span>팝업탐색</span>
      </NavLink>
      <NavLink to="/" end className={({isActive}) => isActive ? 'active' : ''}>
        <span role="img" aria-label="home">🏠</span>
        <span>메인화면</span>
      </NavLink>
      <NavLink to="/consumer/mypage" className={({isActive}) => isActive ? 'active' : ''}>
        <span role="img" aria-label="mypage">👤</span>
        <span>마이페이지</span>
      </NavLink>
    </nav>
  )
}
