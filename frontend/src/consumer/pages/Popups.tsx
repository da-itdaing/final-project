import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../../common/services/api'
import Card from '../../common/components/Card'
import type { Popup } from '../../types'
import { useAuth } from '../../common/services/authState'

export default function Popups() {
  const [items, setItems] = useState<Popup[]>([])
  const { user } = useAuth()
  const [liked, setLiked] = useState<Record<string, boolean>>({})
  useEffect(() => { api.listPopups().then(setItems) }, [])
  useEffect(() => {
    if (user) {
      // preload liked status
      Promise.all(items.map(it => api.isFavorite(user.id, it.id))).then(arr => {
        const map: Record<string, boolean> = {}
        arr.forEach((v, i) => map[items[i].id] = v)
        setLiked(map)
      })
    }
  }, [user, items])
  return (
    <div>
      <h2 className="section-title">팝업 스토어</h2>
      <div className="grid">
        {items.map(it => (
          <Link key={it.id} to={`../popup/${it.id}`} style={{display:'block'}}>
            <Card image={it.image} title={it.title} subtitle={it.dateRange} footnote={it.district}
              liked={liked[it.id]} onLikeToggle={user ? async (e?: any) => {
                e?.preventDefault?.()
                const now = await api.toggleFavorite(user.id, it.id)
                setLiked(s => ({ ...s, [it.id]: now }))
              } : undefined}
            />
          </Link>
        ))}
      </div>
    </div>
  )
}
