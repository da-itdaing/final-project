import { NavLink, Route, Routes } from 'react-router-dom'
import Login from './pages/Login'
import JoinSeller from './pages/JoinSeller'
import JoinConsumerInfo from './pages/JoinConsumerInfo'
import JoinConsumerCategory from './pages/JoinConsumerCategory'
import FindId from './pages/FindId'
import FindPw from './pages/FindPw'

export default function AuthRoutes() {
  return (
    <div>
      <Routes>
        <Route index element={<Login />} />
        <Route path="login" element={<Login />} />
        <Route path="join">
          <Route path="seller" element={<JoinSeller />} />
          <Route path="consumer/info" element={<JoinConsumerInfo />} />
          <Route path="consumer/categories" element={<JoinConsumerCategory />} />
        </Route>
        <Route path="find-id" element={<FindId />} />
        <Route path="find-pw" element={<FindPw />} />
      </Routes>
    </div>
  )
}
