import { NavLink, Outlet } from 'react-router-dom'

export default function SellerLayout() {
  return (
    <div className="row" style={{alignItems:'stretch', gap:16}}>
      <aside style={{minWidth:220}}>
        <div className="card" style={{padding:12}}>
          <div className="muted" style={{margin:'6px 0'}}>팝업관리</div>
          <nav className="col" style={{gap:6}}>
            <NavLink to="popups" className={({isActive}) => isActive ? 'active pill' : 'pill'}>팝업 리스트</NavLink>
            <NavLink to="popups/register" className={({isActive}) => isActive ? 'active pill' : 'pill'}>팝업 등록</NavLink>
            <NavLink to="popups/location" className={({isActive}) => isActive ? 'active pill' : 'pill'}>위치 설정</NavLink>
          </nav>
          <div className="muted" style={{margin:'12px 0 6px'}}>일정관리</div>
          <nav className="col" style={{gap:6}}>
            <NavLink to="schedule" className={({isActive}) => isActive ? 'active pill' : 'pill'}>캘린더</NavLink>
          </nav>
          <div className="muted" style={{margin:'12px 0 6px'}}>내 정보</div>
          <nav className="col" style={{gap:6}}>
            <NavLink to="profile" className={({isActive}) => isActive ? 'active pill' : 'pill'}>프로필</NavLink>
          </nav>
          <div className="muted" style={{margin:'12px 0 6px'}}>리뷰 관리</div>
          <nav className="col" style={{gap:6}}>
            <NavLink to="reviews" className={({isActive}) => isActive ? 'active pill' : 'pill'}>대시보드</NavLink>
          </nav>
          <div className="muted" style={{margin:'12px 0 6px'}}>메시지</div>
          <nav className="col" style={{gap:6}}>
            <NavLink to="messages" className={({isActive}) => isActive ? 'active pill' : 'pill'}>메시지함</NavLink>
            <NavLink to="messages/compose" className={({isActive}) => isActive ? 'active pill' : 'pill'}>메시지 작성</NavLink>
          </nav>
          <div className="muted" style={{margin:'12px 0 6px'}}>공지사항 관리</div>
          <nav className="col" style={{gap:6}}>
            <NavLink to="notices/platform" className={({isActive}) => isActive ? 'active pill' : 'pill'}>플랫폼 공지</NavLink>
            <NavLink to="notices/customers" className={({isActive}) => isActive ? 'active pill' : 'pill'}>고객 공지</NavLink>
          </nav>
        </div>
      </aside>
      <main style={{flex:1}}>
        <Outlet />
      </main>
    </div>
  )
}
