import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { api } from '../../common/services/api'
import type { Popup } from '../../types'
import Carousel from '../../common/components/Carousel'

export default function PopupDetail() {
  const { id = '' } = useParams()
  const [popup, setPopup] = useState<Popup | null>(null)
  const nav = useNavigate()
  useEffect(()=>{ api.getPopup(id).then(setPopup) },[id])
  if (!popup) return <div className="muted">불러오는 중...</div>
  return (
    <div className="card">
      <div className="pad">
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
              <div className="muted" style={{marginTop:4}}>{popup.categories?.join(', ')}</div>
            </div>
            <div className="row" style={{gap:8}}>
              <button className="pill" onClick={()=>nav(`/seller/popups/register?id=${popup.id}`)}>편집</button>
              <button className="pill" onClick={()=>navigator.clipboard.writeText(location.href)}>공유</button>
            </div>
          </div>
        </div>
        <div className="muted" style={{marginTop:12}}>이 페이지는 판매자 관점의 상세 페이지입니다. (데모)</div>
      </div>
    </div>
  )
}
