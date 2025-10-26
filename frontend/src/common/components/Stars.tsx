type Props = {
  value: number
  onChange?: (v: number) => void
}

export default function Stars({ value, onChange }: Props) {
  const stars = [1,2,3,4,5]
  return (
    <div style={{display:'flex', gap:6, alignItems:'center'}}>
      {stars.map(s => (
        <button key={s} aria-label={`별 ${s}`} onClick={() => onChange?.(s)}
          style={{
            width: 20, height: 20, borderRadius:4, border:'1px solid #eee',
            background: s <= value ? '#ffd54f' : '#f1f1f1', cursor: onChange ? 'pointer' : 'default'
          }}
        />
      ))}
    </div>
  )
}
