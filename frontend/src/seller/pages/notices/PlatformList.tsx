import { useEffect, useState } from 'react'
import { sellerApi } from '../../services/api'
import type { SellerNotice } from '../../../types'
import { NavLink } from 'react-router-dom'

export default function PlatformList() {
  const [list, setList] = useState<SellerNotice[]>([])
  const [q, setQ] = useState('')
  useEffect(()=>{ sellerApi.listPlatformNotices().then(setList) },[])
  const filtered = list.filter(n => n.title.includes(q) || n.body.includes(q))
  return (
    <div className="card">
      <div className="pad">
        <h2>플랫폼 공지사항</h2>
        <div className="row" style={{gap:8, marginTop:8}}>
          <input placeholder="검색어를 입력하세요" value={q} onChange={e=>setQ(e.target.value)} style={{flex:1}} />
        </div>
        <div className="table" style={{marginTop:8}}>
          {filtered.map(n => (
            <div className="table-row" key={n.id}>
              <div className="pill" style={{background:'#eee'}}>{n.importance||'일반'}</div>
              <div style={{flex:1}}><NavLink to={`/seller/notices/platform/${n.id}`}>{n.title}</NavLink></div>
              <div className="muted">{n.date}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
