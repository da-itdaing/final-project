import { useEffect, useState } from 'react'
import { api } from '../../../common/services/api'
import { useAuth } from '../../../common/services/authState'
import type { Popup } from '../../../types'
import Card from '../../../common/components/Card'
import { Link } from 'react-router-dom'

export default function FavoritesPage() {
  const { user } = useAuth()
  const [list, setList] = useState<Popup[]>([])
  const refresh = () => { if (user) api.listFavorites(user.id).then(setList) }
  useEffect(()=>{ refresh() },[user])
  if (!user) return <div>로그인이 필요합니다.</div>
  return (
    <div>
      <h3 className="section-title">관심 팝업</h3>
      <div className="grid">
        {list.map(it => (
          <Link key={it.id} to={`../../popup/${it.id}`} style={{display:'block'}}>
            <Card image={it.image} title={it.title} subtitle={it.dateRange} footnote={it.district}
              liked={true} onLikeToggle={async (e?: any)=>{ e?.preventDefault?.(); await api.toggleFavorite(user.id, it.id); refresh() }}
            />
          </Link>
        ))}
      </div>
    </div>
  )
}
