import { useEffect, useState } from 'react'
import { sellerApi } from '../../services/api'
import type { SellerMessage } from '../../../types'

export default function Inbox() {
  const [tab, setTab] = useState<'received'|'sent'>('received')
  const [list, setList] = useState<SellerMessage[]>([])
  useEffect(() => { sellerApi.listMessages(tab).then(setList) }, [tab])
  return (
    <div className="card">
      <div className="pad">
        <h2>메시지함</h2>
        <div className="tabs" role="tablist">
          <button className={`tab ${tab==='received'?'active':''}`} onClick={()=>setTab('received')}>받은 메시지</button>
          <button className={`tab ${tab==='sent'?'active':''}`} onClick={()=>setTab('sent')}>보낸 메시지</button>
        </div>
        <div className="row" style={{marginTop:12}}>
          <div style={{flex:1}}>
            {list.map(m => (
              <div key={m.id} className="card" style={{marginBottom:8}}>
                <div className="pad">
                  <div className="row" style={{justifyContent:'space-between'}}>
                    <strong>{m.subject || '(제목 없음)'}</strong>
                    <span className="muted">{m.date}</span>
                  </div>
                  <div className="muted" style={{marginTop:6, whiteSpace:'pre-wrap'}}>{m.body}</div>
                </div>
              </div>
            ))}
            {list.length===0 && <div className="muted">메시지가 없습니다.</div>}
          </div>
        </div>
      </div>
    </div>
  )
}
