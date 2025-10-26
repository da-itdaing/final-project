import { Route, Routes, NavLink } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import Approvals from './pages/Approvals'
import Reports from './pages/Reports'

export default function ManagerRoutes() {
  return (
    <div>
      <div className="row" style={{margin: '8px 0 16px'}}>
        <NavLink to="." end className={({isActive}) => isActive ? 'active pill' : 'pill'}>대시보드</NavLink>
        <NavLink to="approvals" className={({isActive}) => isActive ? 'active pill' : 'pill'}>승인</NavLink>
        <NavLink to="reports" className={({isActive}) => isActive ? 'active pill' : 'pill'}>리포트</NavLink>
      </div>
      <Routes>
        <Route index element={<Dashboard />} />
        <Route path="approvals" element={<Approvals />} />
        <Route path="reports" element={<Reports />} />
      </Routes>
    </div>
  )
}
