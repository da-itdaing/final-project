import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { authService } from '../../common/services/auth'
import { useAuth } from '../../common/services/authState'

const INTERESTS = ['공연/전시', '푸드', '패션/잡화', '라이프스타일', '테크']
const AREAS = ['남구', '동구', '서구', '북구', '광산구']
const STYLES = ['팝업 스토어', '마켓', '페스티벌', '플리마켓', '체험형']

export default function JoinConsumerCategory() {
  const [interests, setInterests] = useState<string[]>([])
  const [areas, setAreas] = useState<string[]>([])
  const [styles, setStyles] = useState<string[]>([])
  const [err, setErr] = useState('')
  const { setUser } = useAuth()
  const nav = useNavigate()

  function toggle(list: string[], set: (v: string[]) => void, v: string) {
    set(list.includes(v) ? list.filter(x=>x!==v) : [...list, v])
  }

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (interests.length<1 || areas.length<1 || styles.length<1) { setErr('각 항목을 최소 1개 이상 선택하세요.'); return }
    const u = await authService.registerConsumer({ interests, areas, styles })
    setUser(u)
    nav('/consumer')
  }

  return (
    <div style={{display:'flex', justifyContent:'center', marginTop:24}}>
      <form className="card" onSubmit={onSubmit} style={{width: 720}}>
        <div className="pad">
          <h2 style={{textAlign:'center', color:'var(--brand)'}}>사용자님의 취향을 알려주세요!</h2>
          <p className="muted">선택하신 내용을 바탕으로 맞춤 추천을 드릴게요.</p>

          <section style={{marginTop: 8}}>
            <h3 style={{margin:'8px 0'}}>어떤 분야에 관심이 있나요? <small className="muted">(최소 1개)</small></h3>
            <div className="chips">
              {INTERESTS.map(v => (
                <button type="button" key={v} className={`chip ${interests.includes(v)?'active':''}`} onClick={()=>toggle(interests,setInterests,v)}>{v}</button>
              ))}
            </div>
          </section>

          <section style={{marginTop: 8}}>
            <h3 style={{margin:'8px 0'}}>어떤 지역의 팝업을 방문하고 싶나요? <small className="muted">(최소 1개)</small></h3>
            <div className="chips">
              {AREAS.map(v => (
                <button type="button" key={v} className={`chip ${areas.includes(v)?'active':''}`} onClick={()=>toggle(areas,setAreas,v)}>{v}</button>
              ))}
            </div>
          </section>

          <section style={{marginTop: 8}}>
            <h3 style={{margin:'8px 0'}}>원하는 팝업 스타일은? <small className="muted">(최소 1개)</small></h3>
            <div className="chips">
              {STYLES.map(v => (
                <button type="button" key={v} className={`chip ${styles.includes(v)?'active':''}`} onClick={()=>toggle(styles,setStyles,v)}>{v}</button>
              ))}
            </div>
          </section>

          {err && <div className="muted" style={{color:'#d32f2f', marginTop: 8}}>{err}</div>}

          <button className="cta" type="submit" style={{marginTop: 12}}>가입하기</button>
        </div>
      </form>
    </div>
  )
}
