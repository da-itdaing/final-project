type Props = { title: string; to?: string; style?: React.CSSProperties }

import { Link } from 'react-router-dom'

export default function SectionHeader({ title, to, style }: Props) {
  return (
    <div className="section-header" style={style}>
      <h2 className="section-title" style={{margin: 0}}>{title}</h2>
      {to && <Link className="muted" to={to}>전체보기 ›</Link>}
    </div>
  )
}
