import { useOutletContext } from 'react-router-dom'
import type { Popup } from '../../types'

export default function PopupMap() {
  const { popup } = useOutletContext<{ popup: Popup }>()
  return (
    <div>
      <div style={{border:'1px solid #eee', borderRadius:12, overflow:'hidden'}}> 
        {/* OpenStreetMap embed (no API key) */}
        <iframe
          title="map"
          width="100%"
          height="300"
          src={`https://www.openstreetmap.org/export/embed.html?bbox=${(popup.lng||126.92)-0.01}%2C${(popup.lat||35.16)-0.01}%2C${(popup.lng||126.92)+0.01}%2C${(popup.lat||35.16)+0.01}&layer=mapnik&marker=${popup.lat||35.16}%2C${popup.lng||126.92}`}
          style={{border:0}}
        />
      </div>
      <div className="field" style={{marginTop:12}}>
        <label>주소</label>
        <div className="row" style={{gap:8, alignItems:'center'}}>
          <div style={{flex:1}}>{popup.address}</div>
          <button className="pill" onClick={() => navigator.clipboard.writeText(popup.address || '')}>주소 복사</button>
        </div>
      </div>
    </div>
  )
}
