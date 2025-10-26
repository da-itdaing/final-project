import { useEffect, useMemo, useState } from 'react'
import { sellerApi } from '../services/api'
import type { Review } from '../../types'
import Stars from '../../common/components/Stars'

export default function ReviewsDashboard() {
  const [list, setList] = useState<Review[]>([])
  useEffect(()=>{ sellerApi.listReviews().then(setList) },[])
  const avg = useMemo(()=> list.length ? (list.reduce((a,b)=>a+b.rating,0)/list.length) : 0, [list])
  const counts = useMemo(()=> [5,4,3,2,1].map(s => list.filter(r=>r.rating===s).length), [list])
  return (
    <div className="card">
      <div className="pad">
        <h2>리뷰 관리 대시보드</h2>
        <div className="row" style={{gap:12, marginTop:12}}>
          <div className="card" style={{padding:12}}>
            <div className="muted">평점</div>
            <div style={{fontSize:24, fontWeight:700}}>{avg.toFixed(1)}</div>
            <Stars value={avg} />
          </div>
          <div className="card" style={{padding:12}}>
            <div className="muted">총 리뷰수</div>
            <div style={{fontSize:24, fontWeight:700}}>{list.length}</div>
          </div>
          <div className="card" style={{padding:12, flex:1}}>
            <div className="muted">분포</div>
            {[5,4,3,2,1].map((s,i)=> (
              <div key={s} className="row" style={{alignItems:'center', gap:6}}>
                <div style={{width:28}}>{s}점</div>
                <div style={{background:'#eee', height:8, borderRadius:4, flex:1}}>
                  <div style={{width: `${(counts[i]/Math.max(1, Math.max(...counts)))*100}%`, height: '100%', background:'#e53935', borderRadius:4}} />
                </div>
                <div style={{width:24, textAlign:'right'}}>{counts[i]}</div>
              </div>
            ))}
          </div>
        </div>
        <div style={{marginTop:16}}>
          {list.map(r => (
            <div key={r.id} className="card" style={{marginBottom:8}}>
              <div className="pad">
                <div className="row" style={{justifyContent:'space-between'}}>
                  <strong>{r.userName}</strong>
                  <span className="muted">{r.date}</span>
                </div>
                <Stars value={r.rating} />
                <div style={{marginTop:6}}>{r.text}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
