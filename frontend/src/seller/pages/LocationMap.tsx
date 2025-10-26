export default function LocationMap() {
  return (
    <div className="card">
      <div className="pad">
        <h2>위치 추가 (지도)</h2>
        <div style={{border:'1px solid #ddd', borderRadius:8, overflow:'hidden'}}>
          <iframe title="map" src="https://www.openstreetmap.org/export/embed.html?bbox=126.84%2C35.09%2C126.99%2C35.22&layer=mapnik" style={{width:'100%', height:420, border:0}} />
        </div>
        <div className="row" style={{gap:8, marginTop:12}}>
          <input placeholder="상세주소" style={{flex:1}} />
          <button className="pill">확인</button>
          <button className="pill">취소</button>
        </div>
        <div className="muted" style={{marginTop:8}}>지도를 통해 정확한 위치를 지정하세요. (데모)</div>
      </div>
    </div>
  )
}
