import { useEffect, useState } from 'react'
import { api } from '../../common/services/api'
import type { Report } from '../../types'

export default function Dashboard() {
  const [reports, setReports] = useState<Report[]>([])
  useEffect(() => { api.listReports().then(setReports) }, [])
  return (
    <div>
      <h2 className="section-title">운영 리포트</h2>
      <div className="row">
        {reports.map(r => (
          <div key={r.id} className="card" style={{flex: '1 1 240px'}}>
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
