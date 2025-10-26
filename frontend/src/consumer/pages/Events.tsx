import { useEffect, useState } from 'react'
import { api } from '../../common/services/api'
import Card from '../../common/components/Card'
import type { Event } from '../../types'
import { Link } from 'react-router-dom'

export default function Events() {
  const [items, setItems] = useState<Event[]>([])
  useEffect(() => { api.listEvents().then(setItems) }, [])
  return (
    <div>
      <h2 className="section-title">행사 목록</h2>
      <div className="grid">
        {items.map(it => (
          <Link key={it.id} to={`/consumer/events/${it.id}`} style={{ display: 'block' }}>
            <Card image={it.image} title={it.title} subtitle={it.dateRange} footnote={it.location} />
          </Link>
        ))}
      </div>
    </div>
  )
}
