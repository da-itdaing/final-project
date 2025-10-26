import { useEffect, useState } from 'react'
import { api } from '../../common/services/api'
import type { Report } from '../../types'

export default function Reports() {
  const [items, setItems] = useState<Report[]>([])
  useEffect(() => { api.listReports().then(setItems) }, [])
  return (
    <div>
      <h2 className="section-title">리포트 상세</h2>
      <div className="grid">
        {items.map(r => (
          <div key={r.id} className="card">
            <div className="pad">
              <div style={{fontWeight: 700}}>{r.title}</div>
              <div className="muted" style={{marginTop: 6}}>{r.metric}</div>
              <div style={{fontSize: 28, fontWeight: 800, marginTop: 6}}>{r.value.toLocaleString()}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
