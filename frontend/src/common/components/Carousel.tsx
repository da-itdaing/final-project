type Item = { id: string; image: string; title?: string }

type Props = { items: Item[] }

export default function Carousel({ items }: Props) {
  if (!items || items.length === 0) return null
  return (
    <div className="carousel">
      <div className="carousel-track" style={{gridTemplateColumns: `repeat(${items.length}, 100%)`}}>
        {items.map(it => (
          <div key={it.id} className="carousel-slide">
            <img src={it.image} alt={it.title || ''} />
          </div>
        ))}
      </div>
      <div className="carousel-dots">
        {items.map((_, idx) => (<span key={idx} />))}
      </div>
    </div>
  )
}
