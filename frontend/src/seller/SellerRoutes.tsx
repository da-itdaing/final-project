import { Route, Routes } from 'react-router-dom'
import RequireRole from '../common/components/RequireRole'
import SellerLayout from './SellerLayout'
import PopupList from './pages/PopupList'
import PopupRegister from './pages/PopupRegister'
import LocationMap from './pages/LocationMap'
import Schedule from './pages/Schedule'
import Profile from './pages/Profile'
import ReviewsDashboard from './pages/ReviewsDashboard'
import PopupDetail from './pages/PopupDetail'
import Inbox from './pages/messages/Inbox'
import Compose from './pages/messages/Compose'
import PlatformList from './pages/notices/PlatformList'
import PlatformDetail from './pages/notices/PlatformDetail'
import CustomerList from './pages/notices/CustomerList'
import CustomerDetail from './pages/notices/CustomerDetail'
import CustomerCompose from './pages/notices/CustomerCompose'

export default function SellerRoutes() {
  return (
    <Routes>
      <Route element={<RequireRole role="seller" />}>
        <Route element={<SellerLayout />}>
          <Route index element={<PopupList />} />
          <Route path="popups" element={<PopupList />} />
          <Route path="popups/:id" element={<PopupDetail />} />
          <Route path="popups/register" element={<PopupRegister />} />
          <Route path="popups/location" element={<LocationMap />} />
          <Route path="schedule" element={<Schedule />} />
          <Route path="profile" element={<Profile />} />
          <Route path="reviews" element={<ReviewsDashboard />} />
          <Route path="messages" element={<Inbox />} />
          <Route path="messages/compose" element={<Compose />} />
          <Route path="notices/platform" element={<PlatformList />} />
          <Route path="notices/platform/:id" element={<PlatformDetail />} />
          <Route path="notices/customers" element={<CustomerList />} />
          <Route path="notices/customers/new" element={<CustomerCompose />} />
          <Route path="notices/customers/:id" element={<CustomerDetail />} />
        </Route>
      </Route>
    </Routes>
  )
}
