import { useEffect, useState } from 'react'
import { sellerApi } from '../../services/api'
import type { SellerNotice } from '../../../types'
import { NavLink } from 'react-router-dom'

export default function CustomerList() {
  const [list, setList] = useState<SellerNotice[]>([])
  const [q, setQ] = useState('')
  useEffect(()=>{ sellerApi.listCustomerNotices().then(setList) },[])
  const filtered = list.filter(n => n.title.includes(q) || n.body.includes(q))
  return (
    <div className="card">
      <div className="pad">
        <div className="row" style={{justifyContent:'space-between', alignItems:'center'}}>
          <h2>고객 공지사항</h2>
          <NavLink to={`/seller/notices/customers/new`} className="pill">+ 새 글 올리기</NavLink>
        </div>
        <div className="row" style={{gap:8, marginTop:8}}>
          <input placeholder="검색어" value={q} onChange={e=>setQ(e.target.value)} style={{flex:1}} />
        </div>
        <div className="table" style={{marginTop:8}}>
          {filtered.map(n => (
            <div className="table-row" key={n.id}>
              <div style={{flex:1}}><NavLink to={`/seller/notices/customers/${n.id}`}>{n.title}</NavLink></div>
              <div className="muted">{n.date}</div>
            </div>
          ))}
          {filtered.length===0 && <div className="muted">등록된 고객 공지사항이 없습니다.</div>}
        </div>
      </div>
    </div>
  )
}
