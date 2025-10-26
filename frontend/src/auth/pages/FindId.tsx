import { useState } from 'react'
import { authService } from '../../common/services/auth'

export default function FindId() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [result, setResult] = useState<string | null>(null)

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    const r = await authService.findId(name, email)
    setResult(r)
  }

  return (
    <div style={{display:'flex', justifyContent:'center', marginTop:24}}>
      <form className="card" onSubmit={onSubmit} style={{width: 420}}>
        <div className="pad">
          <h2 style={{textAlign:'center', color:'var(--brand)'}}>아이디 찾기</h2>
          <div className="tabs"><button className="tab active" type="button">아이디 찾기</button><button className="tab" type="button">비밀번호 찾기</button></div>
          <div className="field"><input placeholder="이름을 입력하세요." value={name} onChange={e=>setName(e.target.value)} /></div>
          <div className="field"><input placeholder="이메일을 입력하세요." value={email} onChange={e=>setEmail(e.target.value)} /></div>
          <button className="cta" type="submit">아이디 찾기</button>
          {result !== null && (
            <div className="muted" style={{marginTop:8}}>
              {result ? `등록된 아이디: ${result}` : '일치하는 계정을 찾지 못했습니다.'}
            </div>
          )}
        </div>
      </form>
    </div>
  )
}
