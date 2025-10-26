import { useEffect, useState } from 'react'
import { api } from '../../common/services/api'
import type { Product } from '../../types'
import Card from '../../common/components/Card'

export default function Products() {
  const [items, setItems] = useState<Product[]>([])
  useEffect(() => { api.listProducts().then(setItems) }, [])
  return (
    <div>
      <h2 className="section-title">상품 관리</h2>
      <div className="grid">
        {items.map(p => (
          <Card key={p.id} image={p.image} title={`${p.name} · ${p.price.toLocaleString()}원`} subtitle={`재고 ${p.stock}`} />
        ))}
      </div>
    </div>
  )
}
