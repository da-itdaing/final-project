import { useEffect, useState } from 'react'
import { api } from '../../common/services/api'
import type { Order } from '../../types'

export default function Orders() {
  const [items, setItems] = useState<Order[]>([])
  useEffect(() => { api.listOrders().then(setItems) }, [])
  return (
    <div>
      <h2 className="section-title">주문 목록</h2>
      <div className="card">
        <div className="pad">
          {items.map(o => (
            <div key={o.id} style={{display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f3f3f3'}}>
              <div>#{o.id}</div>
              <div className="muted">수량 {o.qty}</div>
              <div className="pill">{o.status}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
