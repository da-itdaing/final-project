export default function Approvals() {
  const items = [
    { id: 'a1', title: '행사 등록 요청 – XX 페스티벌', requester: 'seller_01' },
    { id: 'a2', title: '팝업 스토어 신청 – 브랜드A', requester: 'seller_07' },
  ]
  return (
    <div>
      <h2 className="section-title">승인/검토 요청</h2>
      <div className="card">
        <div className="pad">
          {items.map(it => (
            <div key={it.id} style={{display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f3f3f3'}}>
              <div>{it.title}</div>
              <div className="muted">요청자 {it.requester}</div>
              <div>
                <button className="pill" style={{marginRight: 8}}>승인</button>
                <button className="pill">반려</button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
