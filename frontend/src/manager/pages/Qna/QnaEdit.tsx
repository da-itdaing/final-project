import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { managerApi, type AdminQuestion } from '../../services/api'

export default function QnaEdit() {
  const { id } = useParams()
  const nav = useNavigate()
  const [item, setItem] = useState<AdminQuestion | undefined>()
  const [answer, setAnswer] = useState('')
  useEffect(()=>{ if(id) managerApi.getQna(id).then(q => { setItem(q); setAnswer(q?.answer || '') }) },[id])
  if (!item) return <div className="muted">불러오는 중...</div>
  async function del(){ if(!id) return; if(!confirm('삭제 하시겠습니까?')) return; await managerApi.deleteQna(id); nav('/manager/qna') }
  async function save(){ if(!id) return; await managerApi.updateQna(id, answer); alert('수정 되었습니다.'); nav('/manager/qna') }
  return (
    <div className="card"><div className="pad">
      <h2>문의사항 수정</h2>
      <div style={{fontWeight:700}}>{item.title}</div>
      <div className="field"><label>User</label><textarea rows={5} value={item.question} readOnly /></div>
      <div className="field"><label>Admin</label><textarea rows={5} value={answer} onChange={e=>setAnswer(e.target.value)} /></div>
      <div className="row" style={{gap:8}}>
        <button className="pill" onClick={del}>삭제</button>
        <button className="pill" onClick={save}>수정</button>
      </div>
    </div></div>
  )
}
