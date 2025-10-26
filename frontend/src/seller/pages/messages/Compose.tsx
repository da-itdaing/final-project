import { useState } from 'react'
import { sellerApi } from '../../services/api'
import { useNavigate } from 'react-router-dom'

export default function Compose() {
  const [subject, setSubject] = useState('')
  const [body, setBody] = useState('')
  const [file, setFile] = useState<File | null>(null)
  const nav = useNavigate()

  async function send(e: React.FormEvent) {
    e.preventDefault()
    await sellerApi.sendMessage({ box: 'sent', subject, body, })
    nav('/seller/messages')
  }

  return (
    <form className="card" onSubmit={send}>
      <div className="pad">
        <h2>메시지 작성</h2>
        <div className="field"><label>받는 사람</label><input value="관리자" readOnly /></div>
        <div className="field"><label>제목</label><input value={subject} onChange={e=>setSubject(e.target.value)} /></div>
        <div className="field"><label>메시지</label><textarea rows={6} value={body} onChange={e=>setBody(e.target.value)} /></div>
        <div className="field"><label>첨부파일</label><input type="file" onChange={e=>setFile(e.target.files?.[0] || null)} /></div>
        <button className="cta" type="submit">보내기</button>
      </div>
    </form>
  )
}
