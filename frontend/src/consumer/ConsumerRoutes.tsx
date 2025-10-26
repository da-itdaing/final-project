import { Route, Routes, NavLink } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import Events from './pages/Events'
import EventDetail from './pages/EventDetail'
import Popups from './pages/Popups'
import DiscoverMap from './pages/DiscoverMap'
import PopupDetailLayout from './pages/PopupDetailLayout'
import PopupAbout from './pages/PopupAbout'
import PopupMap from './pages/PopupMap'
import PopupReviews from './pages/PopupReviews'
import PopupReviewWrite from './pages/PopupReviewWrite'
import RequireAuth from '../common/components/RequireAuth'
import ProfilePage from './pages/mypage/Profile'
import FavoritesPage from './pages/mypage/Favorites'
import CalendarPage from './pages/mypage/Calendar'
import MyReviewsPage from './pages/mypage/MyReviews'

export default function ConsumerRoutes() {
  return (
    <div>
      <div className="row" style={{margin: '8px 0 16px'}}>
        <NavLink to="." end className={({isActive}) => isActive ? 'active pill' : 'pill'}>대시보드</NavLink>
        <NavLink to="events" className={({isActive}) => isActive ? 'active pill' : 'pill'}>행사</NavLink>
        <NavLink to="popups" className={({isActive}) => isActive ? 'active pill' : 'pill'}>팝업</NavLink>
        <NavLink to="discover" className={({isActive}) => isActive ? 'active pill' : 'pill'}>탐색(지도)</NavLink>
        <NavLink to="mypage" className={({isActive}) => isActive ? 'active pill' : 'pill'}>마이페이지</NavLink>
      </div>
      <Routes>
        <Route index element={<Dashboard />} />
  <Route path="events" element={<Events />} />
  <Route path="events/:id" element={<EventDetail />} />
        <Route path="popups" element={<Popups />} />
        <Route path="discover" element={<DiscoverMap />} />
        <Route path="popup/:id" element={<PopupDetailLayout />}>
          <Route path="about" element={<PopupAbout />} />
          <Route path="map" element={<PopupMap />} />
          <Route path="reviews" element={<PopupReviews />} />
          <Route element={<RequireAuth />}>
            <Route path="reviews/write" element={<PopupReviewWrite />} />
          </Route>
          <Route index element={<PopupAbout />} />
        </Route>
        <Route element={<RequireAuth />}>
          <Route path="mypage">
            <Route index element={<ProfilePage />} />
            <Route path="favorites" element={<FavoritesPage />} />
            <Route path="calendar" element={<CalendarPage />} />
            <Route path="reviews" element={<MyReviewsPage />} />
          </Route>
        </Route>
      </Routes>
    </div>
  )
}
