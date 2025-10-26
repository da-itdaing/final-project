import { useEffect, useState } from 'react'
import { sellerApi } from '../services/api'
import type { Popup } from '../../types'
import { NavLink, useNavigate } from 'react-router-dom'

export default function PopupList() {
  const [list, setList] = useState<Popup[]>([])
  const [q, setQ] = useState('')
  const nav = useNavigate()
  useEffect(() => { sellerApi.listPopups().then(setList) }, [])
  const filtered = list.filter(p => p.title.includes(q))
  return (
    <div className="card">
      <div className="pad">
        <div className="row" style={{justifyContent:'space-between', alignItems:'center'}}>
          <h2>팝업 관리</h2>
          <button className="pill" onClick={() => nav('/seller/popups/register')}>팝업등록</button>
        </div>
        <div className="row" style={{gap:8, margin:'8px 0'}}>
          <input placeholder="팝업명을 검색하세요" value={q} onChange={e=>setQ(e.target.value)} style={{flex:1}} />
        </div>
        <div className="table">
          <div className="table-row table-head">
            <div>썸네일</div><div>팝업명</div><div>운영 상태</div><div>운영기간</div><div>승인 상태</div><div>변경사유</div><div>액션</div>
          </div>
          {filtered.map(p => (
            <div className="table-row" key={p.id}>
              <div><img src={p.image} alt="" style={{width:36,height:36,objectFit:'cover',borderRadius:4}} /></div>
              <div><NavLink to={`/seller/popups/${p.id}`}>{p.title}</NavLink></div>
              <div>진행중</div>
              <div>{p.dateRange}</div>
              <div>승인 완료</div>
              <div>-</div>
              <div className="row" style={{gap:8}}>
                <NavLink to={`/seller/notices/customers/new`} className="pill">공지</NavLink>
                <button className="pill" onClick={() => nav(`/seller/popups/register?id=${p.id}`)}>편집</button>
                <button className="pill" onClick={() => nav(`/seller/popups/${p.id}`)}>보기</button>
              </div>
            </div>
          ))}
        </div>
        <div className="muted" style={{marginTop:8}}>&lt;&lt; 1 | 2 | 3 | 4 | 5 &gt;&gt;</div>
      </div>
    </div>
  )
}
