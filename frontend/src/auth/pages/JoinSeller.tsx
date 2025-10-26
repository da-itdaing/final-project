import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { authService } from '../../common/services/auth'
import { useAuth } from '../../common/services/authState'

export default function JoinSeller() {
  const [name, setName] = useState('')
  const [username, setUsername] = useState('')
  const [nickname, setNickname] = useState('')
  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
  const [email, setEmail] = useState('')
  const [err, setErr] = useState('')
  const { setUser } = useAuth()
  const nav = useNavigate()

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!name || !username || !password || !email) { setErr('필수 값을 입력하세요.'); return }
    if (password !== confirm) { setErr('비밀번호가 일치하지 않습니다.'); return }
    const u = await authService.registerSeller({ name, username, nickname, password, email })
    setUser(u)
    nav('/seller')
  }

  return (
    <div style={{display:'flex', justifyContent:'center', marginTop:24}}>
      <form className="card" onSubmit={onSubmit} style={{width: 520}}>
        <div className="pad">
          <h2 style={{textAlign:'center', color:'var(--brand)'}}>DA - IT DAING</h2>
          <div className="tabs">
            <button type="button" className="tab">소비자</button>
            <button type="button" className="tab active">판매자</button>
          </div>
          <div className="field"><label>이름</label><input value={name} onChange={e=>setName(e.target.value)} /></div>
          <div className="field"><label>아이디</label><input value={username} onChange={e=>setUsername(e.target.value)} /></div>
          <div className="field"><label>닉네임</label><input value={nickname} onChange={e=>setNickname(e.target.value)} /></div>
          <div className="field"><label>비밀번호</label><input type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
          <div className="field"><label>비밀번호 재입력</label><input type="password" value={confirm} onChange={e=>setConfirm(e.target.value)} /></div>
          <div className="field"><label>E-mail</label><input type="email" value={email} onChange={e=>setEmail(e.target.value)} /></div>
          {err && <div className="muted" style={{color:'#d32f2f'}}>{err}</div>}
          <button className="cta" type="submit">가입하기</button>
        </div>
      </form>
    </div>
  )
}
