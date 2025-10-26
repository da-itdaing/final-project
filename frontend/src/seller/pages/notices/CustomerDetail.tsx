import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { sellerApi } from '../../services/api'
import type { SellerNotice } from '../../../types'

export default function CustomerDetail() {
  const { id } = useParams()
  const nav = useNavigate()
  const [item, setItem] = useState<SellerNotice | undefined>()
  const [edit, setEdit] = useState(false)
  const [title, setTitle] = useState('')
  const [body, setBody] = useState('')
  useEffect(()=>{ if(id) sellerApi.getCustomerNotice(id).then(n => { setItem(n); if(n){ setTitle(n.title); setBody(n.body) } }) },[id])
  if (!item) return <div className="muted">불러오는 중...</div>
  async function save(){ if (!id) return; await sellerApi.updateCustomerNotice(id, { title, body }); setEdit(false) }
  return (
    <div className="card">
      <div className="pad">
        <div className="muted">고객 공지사항</div>
        {edit ? (
          <>
            <div className="field"><label>제목</label><input value={title} onChange={e=>setTitle(e.target.value)} /></div>
            <div className="field"><label>내용</label><textarea rows={8} value={body} onChange={e=>setBody(e.target.value)} /></div>
            <div className="row" style={{gap:8}}>
              <button className="pill" onClick={save}>저장</button>
              <button className="pill" onClick={()=>setEdit(false)}>취소</button>
            </div>
          </>
        ) : (
          <>
            <h2>{item.title}</h2>
            <div className="muted" style={{marginBottom:8}}>{item.date}</div>
            <div style={{whiteSpace:'pre-wrap'}}>{item.body}</div>
            <div className="row" style={{gap:8, marginTop:12}}>
              <button className="pill" onClick={()=>nav('/seller/notices/customers')}>목록보기</button>
              <button className="pill" onClick={()=>setEdit(true)}>수정하기</button>
            </div>
          </>
        )}
      </div>
    </div>
  )
}
