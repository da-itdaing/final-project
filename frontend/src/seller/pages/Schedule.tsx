import { useMemo, useState } from 'react'
import { sellerApi } from '../services/api'
import type { CalendarItem } from '../../types'

function daysInMonth(year: number, month: number) { return new Date(year, month+1, 0).getDate() }

export default function Schedule() {
  const today = new Date()
  const [year, setYear] = useState(today.getFullYear())
  const [month, setMonth] = useState(today.getMonth()) // 0-based
  const [items] = useState<CalendarItem[]>([])
  const firstDow = useMemo(() => new Date(year, month, 1).getDay(), [year, month])
  const dim = useMemo(() => daysInMonth(year, month), [year, month])

  function prevMonth(){ const d = new Date(year, month, 1); d.setMonth(d.getMonth()-1); setYear(d.getFullYear()); setMonth(d.getMonth()) }
  function nextMonth(){ const d = new Date(year, month, 1); d.setMonth(d.getMonth()+1); setYear(d.getFullYear()); setMonth(d.getMonth()) }

  return (
    <div className="card">
      <div className="pad">
        <div className="row" style={{justifyContent:'space-between', alignItems:'center'}}>
          <h2>&lt; {year}년 {month+1}월 &gt;</h2>
          <div className="row" style={{gap:8}}>
            <button className="pill" onClick={prevMonth}>이전</button>
            <button className="pill" onClick={nextMonth}>다음</button>
          </div>
        </div>
        <div className="grid" style={{display:'grid', gridTemplateColumns:'repeat(7,1fr)', gap:6, marginTop:12}}>
          {Array.from({length:firstDow}).map((_,i)=>(<div key={'e'+i} />))}
          {Array.from({length:dim}).map((_,i)=>{
            const d = i+1
            const date = `${year}-${String(month+1).padStart(2,'0')}-${String(d).padStart(2,'0')}`
            const dayItems = items.filter(it=>it.date===date)
            return (
              <div key={d} className="card" style={{padding:8, minHeight:80}}>
                <div style={{fontWeight:600}}>{d}</div>
                {dayItems.map(it => (
                  <div key={it.id} className="muted" style={{fontSize:12}}>{it.title}</div>
                ))}
              </div>
            )
          })}
        </div>
        <div className="muted" style={{marginTop:12}}>이벤트 일정은 데모 데이터로 표시됩니다.</div>
      </div>
    </div>
  )
}
