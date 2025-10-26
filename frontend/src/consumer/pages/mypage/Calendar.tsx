import { useEffect, useMemo, useState } from 'react'
import { api } from '../../../common/services/api'
import { useAuth } from '../../../common/services/authState'
import type { CalendarItem } from '../../../types'

export default function CalendarPage() {
  const { user } = useAuth()
  const [items, setItems] = useState<CalendarItem[]>([])
  useEffect(()=>{ if(user) api.listCalendar(user.id).then(setItems) }, [user])
  const now = new Date()
  const year = now.getFullYear(); const month = now.getMonth() // 0-based
  const first = new Date(year, month, 1)
  const last = new Date(year, month+1, 0)
  const days = Array.from({length: last.getDate()}, (_,i)=>i+1)
  const map = useMemo(()=>{
    const m: Record<number, CalendarItem[]> = {}
    items.forEach(it => {
      const d = new Date(it.date)
      if (d.getFullYear()===year && d.getMonth()===month) {
        const day = d.getDate(); (m[day] ||= []).push(it)
      }
    })
    return m
  }, [items])
  const color = (t: CalendarItem['type']) => t==='start' ? '#42a5f5' : t==='end' ? '#ef5350' : '#66bb6a'

  if (!user) return <div>로그인이 필요합니다.</div>
  return (
    <div>
      <h3 className="section-title">캘린더</h3>
      <div className="muted">{year}년 {month+1}월</div>
      <div style={{display:'grid', gridTemplateColumns:'repeat(7,1fr)', gap:6, marginTop:8}}>
        {['월','화','수','목','금','토','일'].map(w => <div key={w} className="muted" style={{textAlign:'center'}}>{w}</div>)}
        {Array(first.getDay()===0?6:first.getDay()-1).fill(0).map((_,i)=>(<div key={'x'+i}></div>))}
        {days.map(d => (
          <div key={d} style={{border:'1px solid #eee', borderRadius:8, padding:8, minHeight:72}}>
            <div className="muted" style={{fontSize:12}}>{d}</div>
            <div style={{display:'grid', gap:4, marginTop:4}}>
              {(map[d]||[]).map(it => (
                <div key={it.id} style={{background: color(it.type), color:'#fff', borderRadius:6, padding:'2px 6px', fontSize:12}}>{it.title}</div>
              ))}
            </div>
          </div>
        ))}
      </div>
      <div className="row" style={{gap:8, marginTop:12}}>
        <span style={{background:'#42a5f5', color:'#fff', borderRadius:6, padding:'2px 6px', fontSize:12}}>시작</span>
        <span style={{background:'#66bb6a', color:'#fff', borderRadius:6, padding:'2px 6px', fontSize:12}}>운영</span>
        <span style={{background:'#ef5350', color:'#fff', borderRadius:6, padding:'2px 6px', fontSize:12}}>종료</span>
      </div>
    </div>
  )
}
