import { useEffect, useState } from 'react'
import { api } from '../../common/services/api'
import Card from '../../common/components/Card'
import type { Event } from '../../types'

export default function Dashboard() {
  const [events, setEvents] = useState<Event[]>([])
  useEffect(() => { api.listEvents().then(setEvents) }, [])
  return (
    <div>
      <h2 className="section-title">추천 행사</h2>
      <div className="grid">
        {events.map(e => (
          <Card key={e.id} image={e.image} title={e.title} subtitle={e.dateRange} footnote={e.location} />
        ))}
      </div>
    </div>
  )
}
