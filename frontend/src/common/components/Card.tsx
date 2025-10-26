type Props = {
  image?: string
  title: string
  subtitle?: string
  footnote?: string
  onLikeToggle?: (e: React.MouseEvent) => void
  liked?: boolean
}

export default function Card({ image, title, subtitle, footnote, onLikeToggle, liked }: Props) {
  return (
    <div className="card">
      {image && (
        <div style={{position: 'relative'}}>
          <img src={image} alt="" />
          {onLikeToggle && (
            <button
              aria-label="즐겨찾기"
              className={`heart ${liked ? 'on' : ''}`}
              onClick={(e) => { e.preventDefault(); e.stopPropagation(); onLikeToggle(e) }}
            >
              ♥
            </button>
          )}
        </div>
      )}
      <div className="pad">
        <div style={{fontWeight: 700}}>{title}</div>
        {subtitle && <div className="muted" style={{marginTop: 4}}>{subtitle}</div>}
        {footnote && <div className="muted" style={{marginTop: 6}}>{footnote}</div>}
      </div>
    </div>
  )
}
