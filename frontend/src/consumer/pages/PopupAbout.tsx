import { useOutletContext } from 'react-router-dom'
import type { Popup } from '../../types'

export default function PopupAbout() {
  const { popup } = useOutletContext<{ popup: Popup }>()
  return (
    <div>
      <div className="row" style={{gap:16}}>
        <div style={{flex:1}}>
          <div className="field"><label>팝업명</label><div>{popup.title}</div></div>
          <div className="field"><label>기간</label><div>{popup.dateRange}</div></div>
          <div className="field"><label>주소</label><div>{popup.address}</div></div>
          <div className="field"><label>카테고리</label><div>{popup.categories?.join(', ')}</div></div>
          {popup.description && (
            <div className="field"><label>소개</label><div>{popup.description}</div></div>
          )}
          {popup.sourceUrl && (
            <div className="field"><label>관련 링크</label><div><a href={popup.sourceUrl} target="_blank" rel="noreferrer">{popup.sourceUrl}</a></div></div>
          )}
        </div>
      </div>
      <div style={{marginTop:16}} className="muted">판매자 소개가 여기에 표시됩니다. (더미)</div>
    </div>
  )
}
