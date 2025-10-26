import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { api } from '../../common/services/api'
import type { Event } from '../../types'

export default function EventDetail() {
  const { id = '' } = useParams()
  const [ev, setEv] = useState<Event | null>(null)
  const nav = useNavigate()
  useEffect(() => { api.getEvent(id).then(setEv) }, [id])
  if (!ev) return <div className="muted">불러오는 중...</div>
  return (
    <div className="card">
      <div className="pad">
        <div style={{display:'grid', gridTemplateColumns:'1fr', gap:12}}>
          <img src={ev.image} alt="" style={{width:'100%', height:220, objectFit:'cover', borderRadius:12}} />
          <div className="row" style={{justifyContent:'space-between'}}>
            <div>
              <div style={{fontSize:18, fontWeight:800}}>{ev.title}</div>
              <div className="muted" style={{marginTop:4}}>{ev.dateRange} · {ev.location}</div>
            </div>
            <div className="row" style={{gap:8}}>
              <button className="pill" onClick={()=>navigator.clipboard.writeText(location.href)}>공유</button>
              <button className="pill" onClick={()=>nav(-1)}>뒤로</button>
            </div>
          </div>
        </div>
        <div className="muted" style={{marginTop:12}}>이 행사의 상세 정보는 데모 데이터입니다.</div>
      </div>
    </div>
  )
}
