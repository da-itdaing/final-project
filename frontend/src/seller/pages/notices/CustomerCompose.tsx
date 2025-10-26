import { useState } from 'react'
import { sellerApi } from '../../services/api'
import { useNavigate } from 'react-router-dom'

export default function CustomerCompose() {
  const [title, setTitle] = useState('')
  const [body, setBody] = useState('')
  const [file, setFile] = useState<File | null>(null)
  const nav = useNavigate()

  async function submit(e: React.FormEvent) {
    e.preventDefault()
    await sellerApi.createCustomerNotice({ title, body })
    nav('/seller/notices/customers')
  }

  return (
    <form className="card" onSubmit={submit}>
      <div className="pad">
        <h2>고객 공지사항 작성</h2>
        <div className="field"><label>제목</label><input value={title} onChange={e=>setTitle(e.target.value)} /></div>
        <div className="field"><label>내용</label><textarea rows={8} value={body} onChange={e=>setBody(e.target.value)} /></div>
        <div className="field"><label>첨부파일</label><input type="file" onChange={e=>setFile(e.target.files?.[0]||null)} /></div>
        <button className="cta" type="submit">작성</button>
      </div>
    </form>
  )
}
