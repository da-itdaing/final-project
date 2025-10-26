import { useEffect, useState } from 'react'
import { api } from '../../common/services/api'
import type { Order, Report } from '../../types'

export default function Dashboard() {
  const [orders, setOrders] = useState<Order[]>([])
  const [reports, setReports] = useState<Report[]>([])
  useEffect(() => { api.listOrders().then(setOrders); api.listReports().then(setReports) }, [])
  return (
    <div>
      <h2 className="section-title">빠른 통계</h2>
      <div className="row">
        {reports.map(r => (
          <div key={r.id} className="card" style={{flex: '1 1 200px'}}>
            <div className="pad">
              <div className="muted">{r.title}</div>
              <div style={{fontSize: 28, fontWeight: 800, marginTop: 6}}>{r.value.toLocaleString()}</div>
            </div>
          </div>
        ))}
      </div>

      <h2 className="section-title">최근 주문</h2>
      <div className="card">
        <div className="pad">
          {orders.map(o => (
            <div key={o.id} style={{display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f3f3f3'}}>
              <div>주문번호 {o.id}</div>
              <div className="muted">수량 {o.qty}</div>
              <div className="pill">{o.status}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
