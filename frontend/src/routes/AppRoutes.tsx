import { NavLink, Route, Routes } from 'react-router-dom'
import Home from '../screens/Home'
import ConsumerRoutes from '../consumer/ConsumerRoutes'
import SellerRoutes from '../seller/SellerRoutes'
import ManagerRoutes from '../manager/ManagerRoutes'
import AuthRoutes from '../auth/AuthRoutes'
import BaseLayout from '../common/layouts/BaseLayout'

export default function AppRoutes() {
  return (
    <BaseLayout>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/auth/*" element={<AuthRoutes />} />
        <Route path="/consumer/*" element={<ConsumerRoutes />} />
        <Route path="/seller/*" element={<SellerRoutes />} />
        <Route path="/manager/*" element={<ManagerRoutes />} />
      </Routes>
    </BaseLayout>
  )
}
