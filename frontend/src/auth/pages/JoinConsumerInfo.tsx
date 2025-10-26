import { useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { authService } from '../../common/services/auth'

export default function JoinConsumerInfo() {
  const [name, setName] = useState('')
  const [age, setAge] = useState('')
  const [username, setUsername] = useState('')
  const [nickname, setNickname] = useState('')
  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
  const [email, setEmail] = useState('')
  const [err, setErr] = useState('')
  const nav = useNavigate()

  async function onSave(e: React.FormEvent) {
    e.preventDefault()
    if (!name || !username || !password || !email) { setErr('필수 값을 입력하세요.'); return }
    if (password !== confirm) { setErr('비밀번호가 일치하지 않습니다.'); return }
    await authService.saveConsumerDraft({ name, age, username, nickname, password, email })
    nav('/auth/join/consumer/categories')
  }

  return (
    <div style={{display:'flex', justifyContent:'center', marginTop:24}}>
      <form className="card" onSubmit={onSave} style={{width: 520}}>
        <div className="pad">
          <h2 style={{textAlign:'center', color:'var(--brand)'}}>DA - IT DAING</h2>
          <div className="tabs">
            <button type="button" className="tab active">소비자</button>
            <NavLink to="/auth/join/seller" className="tab" style={{textDecoration:'none'}}>판매자</NavLink>
          </div>
          <div className="row" style={{gap:12}}>
            <div className="field" style={{flex:1}}><label>이름</label><input value={name} onChange={e=>setName(e.target.value)} /></div>
            <div className="field" style={{width:180}}>
              <label>나이</label>
              <select value={age} onChange={e=>setAge(e.target.value)}>
                <option value="">나이대 선택</option>
                <option>10대</option>
                <option>20대</option>
                <option>30대</option>
                <option>40대</option>
                <option>50대+</option>
              </select>
            </div>
          </div>
          <div className="field"><label>아이디</label><input value={username} onChange={e=>setUsername(e.target.value)} /></div>
          <div className="field"><label>닉네임</label><input value={nickname} onChange={e=>setNickname(e.target.value)} /></div>
          <div className="field"><label>비밀번호</label><input type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
          <div className="field"><label>비밀번호 재입력</label><input type="password" value={confirm} onChange={e=>setConfirm(e.target.value)} /></div>
          <div className="field"><label>E-mail</label><input type="email" value={email} onChange={e=>setEmail(e.target.value)} /></div>
          {err && <div className="muted" style={{color:'#d32f2f'}}>{err}</div>}
          <div className="row" style={{justifyContent:'space-between', marginTop: 8}}>
            <button className="cta" type="submit">저장하기</button>
            <NavLink to="/auth/join/consumer/categories" className="muted">다음 페이지 ›</NavLink>
          </div>
        </div>
      </form>
    </div>
  )
}
