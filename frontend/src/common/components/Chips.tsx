type Props = {
  items: string[]
  value: string
  onChange: (v: string) => void
}

export default function Chips({ items, value, onChange }: Props) {
  return (
    <div className="chips">
      {items.map(it => (
        <button
          key={it}
          className={`chip ${value === it ? 'active' : ''}`}
          onClick={() => onChange(it)}
        >
          {it}
        </button>
      ))}
    </div>
  )
}
