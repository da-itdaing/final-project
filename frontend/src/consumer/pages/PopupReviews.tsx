import { useEffect, useMemo, useState } from 'react'
import { Link, useLocation, useOutletContext, useParams } from 'react-router-dom'
import { api } from '../../common/services/api'
import type { Popup, Review } from '../../types'
import Stars from '../../common/components/Stars'
import { useAuth } from '../../common/services/authState'

export default function PopupReviews() {
  const { id = '' } = useParams()
  const { popup } = useOutletContext<{ popup: Popup }>()
  const { user } = useAuth()
  const location = useLocation()
  const [list, setList] = useState<Review[]>([])
  const [sort, setSort] = useState<'latest'|'rating'>('rating')
  useEffect(()=>{ api.listReviews(id).then(setList) },[id])
  const avg = useMemo(()=> list.length? (list.reduce((a,b)=>a+b.rating,0)/list.length).toFixed(1):'0.0', [list])
  const sorted = useMemo(()=> {
    const arr = [...list]
    if (sort==='latest') return arr.sort((a,b)=> b.date.localeCompare(a.date))
    return arr.sort((a,b)=> b.rating - a.rating)
  }, [list, sort])
  return (
    <div>
      <div className="row" style={{justifyContent:'space-between', alignItems:'center'}}>
        <div style={{display:'flex', alignItems:'center', gap:8}}>
          <span className="muted">평점</span>
          <Stars value={Number(avg)} />
          <strong>{avg}</strong>
        </div>
        <div>
          <select value={sort} onChange={(e)=>setSort(e.target.value as any)}>
            <option value="rating">평점순</option>
            <option value="latest">최신순</option>
          </select>
        </div>
      </div>
      <div style={{marginTop:12, display:'grid', gap:12}}>
        {sorted.map(r => (
          <div key={r.id} className="card">
            <div className="pad">
              <div style={{display:'flex', justifyContent:'space-between'}}>
                <strong>{r.userName}</strong>
                <span className="muted">{r.date}</span>
              </div>
              <div style={{marginTop:6}}><Stars value={r.rating} /></div>
              <p style={{marginTop:8}}>{r.text}</p>
              {r.images && r.images.length>0 && (
                <div className="row" style={{gap:8, marginTop:8}}>
                  {r.images.map((src,i)=> (
                    <img key={i} src={src} alt="review" style={{width:80,height:80,objectFit:'cover',borderRadius:8}} />
                  ))}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
      {user ? (
        <Link to={`write`}><button className="cta">후기 작성하기</button></Link>
      ) : (
        <Link to={`/auth/login`} state={{ from: `/consumer/popup/${id}/reviews/write` }}>
          <button className="cta">로그인하고 후기 작성하기</button>
        </Link>
      )}
    </div>
  )
}
