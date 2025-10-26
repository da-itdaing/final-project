import { useEffect, useState } from 'react'
import { api } from '../../../common/services/api'
import { useAuth } from '../../../common/services/authState'
import type { Profile } from '../../../types'

const ALL_CATEGORIES = ['패션','주방','가전','전시','체험']
const ALL_AREAS = ['동구','서구','남구','북구','광산구']
const ALL_CONVENIENCES = ['주차','유모차','화장실','엘리베이터']

export default function ProfilePage() {
  const { user } = useAuth()
  const [p, setP] = useState<Profile | null>(null)
  useEffect(()=>{ if(user) api.getProfile(user.id).then(setP) },[user])
  if (!user || !p) return <div>로그인이 필요합니다.</div>
  const toggle = (key: keyof Profile, value: string) => {
    setP(prev => {
      if (!prev) return prev
      const arr = (prev[key] as unknown as string[])
      const next = arr.includes(value) ? arr.filter((v: string) => v !== value) : [...arr, value]
      return { ...prev, [key]: next } as Profile
    })
  }
  const save = async () => {
    await api.updateProfile(user.id, p)
    alert('저장되었습니다')
  }
  return (
    <div>
      <h3 className="section-title">프로필</h3>
      <div className="field"><label>이름</label><div>{user.name}</div></div>
      <div className="field"><label>아이디</label><div>{user.username}</div></div>
      <div className="field"><label>이메일</label><div>{user.email}</div></div>
      <div className="field"><label>관심 카테고리</label>
        <div className="chips">{ALL_CATEGORIES.map(c => (<button key={c} className={`chip ${p.interests.includes(c)?'active':''}`} onClick={()=>toggle('interests', c)}>{c}</button>))}</div>
      </div>
      <div className="field"><label>관심 동네</label>
        <div className="chips">{ALL_AREAS.map(c => (<button key={c} className={`chip ${p.areas.includes(c)?'active':''}`} onClick={()=>toggle('areas', c)}>{c}</button>))}</div>
      </div>
      <div className="field"><label>원하는 편의</label>
        <div className="chips">{ALL_CONVENIENCES.map(c => (<button key={c} className={`chip ${p.conveniences.includes(c)?'active':''}`} onClick={()=>toggle('conveniences', c)}>{c}</button>))}</div>
      </div>
      <button className="cta" onClick={save}>저장하기</button>
    </div>
  )
}
