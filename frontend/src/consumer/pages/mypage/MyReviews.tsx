import { useEffect, useState } from 'react'
import { api } from '../../../common/services/api'
import { useAuth } from '../../../common/services/authState'
import type { Review } from '../../../types'
import Stars from '../../../common/components/Stars'

export default function MyReviewsPage() {
  const { user } = useAuth()
  const [list, setList] = useState<Review[]>([])
  const refresh = () => { if(user) api.listMyReviews(user.name).then(setList) }
  useEffect(()=>{ refresh() }, [user])
  if (!user) return <div>로그인이 필요합니다.</div>
  const remove = async (id: string) => { await api.deleteReview(id); refresh() }
  return (
    <div>
      <h3 className="section-title">내 후기</h3>
      <div style={{display:'grid', gap:12}}>
        {list.map(r => (
          <div key={r.id} className="card">
            <div className="pad">
              <div style={{display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                <div><strong>{r.popupId}</strong> <span className="muted">{r.date}</span></div>
                <button onClick={()=>remove(r.id)} style={{border:'1px solid #eee', background:'#fff', borderRadius:6, padding:'4px 8px'}}>삭제</button>
              </div>
              <div style={{marginTop:6}}><Stars value={r.rating} /></div>
              <p style={{marginTop:8}}>{r.text}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
