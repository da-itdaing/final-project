import { useState } from 'react'
import { authService } from '../../common/services/auth'

export default function FindPw() {
  const [name, setName] = useState('')
  const [username, setUsername] = useState('')
  const [done, setDone] = useState<boolean | null>(null)

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    const ok = await authService.resetPassword(name, username)
    setDone(ok)
  }

  return (
    <div style={{display:'flex', justifyContent:'center', marginTop:24}}>
      <form className="card" onSubmit={onSubmit} style={{width: 420}}>
        <div className="pad">
          <h2 style={{textAlign:'center', color:'var(--brand)'}}>비밀번호 찾기</h2>
          <div className="tabs"><button className="tab" type="button">아이디 찾기</button><button className="tab active" type="button">비밀번호 찾기</button></div>
          <div className="field"><input placeholder="이름을 입력하세요." value={name} onChange={e=>setName(e.target.value)} /></div>
          <div className="field"><input placeholder="아이디를 입력하세요." value={username} onChange={e=>setUsername(e.target.value)} /></div>
          <button className="cta" type="submit">비밀번호 찾기</button>
          {done !== null && (
            <div className="muted" style={{marginTop:8}}>
              {done ? '임시 비밀번호(1234)로 재설정했습니다.' : '일치하는 계정을 찾지 못했습니다.'}
            </div>
          )}
        </div>
      </form>
    </div>
  )
}
