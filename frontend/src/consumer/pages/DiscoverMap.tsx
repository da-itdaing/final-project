import { useEffect, useMemo, useState } from 'react'
import { api } from '../../common/services/api'
import type { Popup } from '../../types'
import Card from '../../common/components/Card'
import { Link } from 'react-router-dom'

const DISTRICTS = ['동구','서구','남구','북구','광산구']

export default function DiscoverMap() {
  const [popups, setPopups] = useState<Popup[]>([])
  const [selected, setSelected] = useState<string>('동구')
  useEffect(() => { api.listPopups().then(setPopups) }, [])
  const counts = useMemo(() => {
    const map: Record<string, number> = {}
    DISTRICTS.forEach(d => map[d] = 0)
    popups.forEach(p => { map[p.district] = (map[p.district]||0)+1 })
    return map
  }, [popups])
  const list = popups.filter(p => p.district === selected)
  return (
    <div>
      <div className="tabs" role="tablist">
        <button className="tab active">지도뷰</button>
        <button className="tab">단일뷰</button>
      </div>
      <div style={{border:'1px solid #eee', borderRadius:12, padding:16, background:'#fafafa'}}>
        <div style={{display:'flex', justifyContent:'space-between', gap:8, flexWrap:'wrap'}}>
          {DISTRICTS.map(d => (
            <button key={d} className={`chip ${selected===d?'active':''}`} onClick={()=>setSelected(d)}>
              {d} <span className="muted" style={{marginLeft:6}}>({counts[d]||0})</span>
            </button>
          ))}
        </div>
        <div className="muted" style={{marginTop:8}}>광주광역시 지도 (모형)</div>
      </div>
      <h3 className="section-title">{selected} 팝업</h3>
      <div className="grid">
        {list.map(it => (
          <Link key={it.id} to={`../popup/${it.id}`} style={{display:'block'}}>
            <Card image={it.image} title={it.title} subtitle={it.dateRange} footnote={it.address||it.district} />
          </Link>
        ))}
      </div>
    </div>
  )
}
