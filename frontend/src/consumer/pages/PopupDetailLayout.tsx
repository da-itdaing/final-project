import { useEffect, useState } from 'react'
import { NavLink, Outlet, useNavigate, useParams } from 'react-router-dom'
import { api } from '../../common/services/api'
import type { Popup } from '../../types'
import { useAuth } from '../../common/services/authState'
import Carousel from '../../common/components/Carousel'

export default function PopupDetailLayout() {
  const { id = '' } = useParams()
  const [popup, setPopup] = useState<Popup | null>(null)
  const navigate = useNavigate()
  const { user } = useAuth()
  const [fav, setFav] = useState<boolean>(false)
  useEffect(() => { api.getPopup(id).then(async p => {
    setPopup(p)
    if (user && p) setFav(await api.isFavorite(user.id, p.id))
    if (!location.pathname.endsWith('/map') && !location.pathname.endsWith('/reviews') && !location.pathname.endsWith('/write')) navigate('about', { replace: true })
  }) }, [id])
  if (!popup) return <div>로딩중…</div>
  return (
    <div>
      <div style={{display:'grid', gridTemplateColumns:'1fr', gap:12}}>
        {popup.images && popup.images.length > 0 ? (
          <Carousel items={popup.images.map((src, i) => ({ id: String(i), image: src }))} />
        ) : (
          <img src={popup.image} alt="" style={{width:'100%', height:220, objectFit:'cover', borderRadius:12}} />
        )}
        <div className="row" style={{justifyContent:'space-between'}}>
          <div>
            <div style={{fontSize:18, fontWeight:800}}>{popup.title}</div>
            <div className="muted" style={{marginTop:4}}>{popup.dateRange} · {popup.address}</div>
          </div>
          <div className="muted" style={{display:'flex', gap:12, alignItems:'center'}}>
            <button className={`pill ${fav?'active':''}`} onClick={async ()=>{
              if (!user) { navigate('/auth/login', { state: { from: location.pathname } }); return }
              const now = await api.toggleFavorite(user.id, popup.id)
              setFav(now)
            }}>♥ 관심</button>
            <span>👁 {popup.views||0}</span>
          </div>
        </div>
      </div>
      <div className="tabs" role="tablist">
        <NavLink to="about" className={({isActive})=>`tab ${isActive?'active':''}`}>설명</NavLink>
        <NavLink to="map" className={({isActive})=>`tab ${isActive?'active':''}`}>지도</NavLink>
        <NavLink to="reviews" className={({isActive})=>`tab ${isActive?'active':''}`}>후기</NavLink>
      </div>
      <Outlet context={{ popup }} />
    </div>
  )
}
