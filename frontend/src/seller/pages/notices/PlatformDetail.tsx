import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { sellerApi } from '../../services/api'
import type { SellerNotice } from '../../../types'

export default function PlatformDetail() {
  const { id } = useParams()
  const nav = useNavigate()
  const [item, setItem] = useState<SellerNotice | undefined>()
  useEffect(()=>{ if(id) sellerApi.getPlatformNotice(id).then(setItem) },[id])
  if (!item) return <div className="muted">불러오는 중...</div>
  return (
    <div className="card">
      <div className="pad">
        <div className="muted">플랫폼 공지사항</div>
        <h2>{item.title}</h2>
        <div className="muted" style={{marginBottom:8}}>{item.date} · {item.importance||'일반'}</div>
        <div style={{whiteSpace:'pre-wrap'}}>{item.body}</div>
        <div style={{marginTop:12}}>
          <button className="pill" onClick={()=>nav('/seller/notices/platform')}>목록보기</button>
        </div>
      </div>
    </div>
  )
}
