import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import Stars from '../../common/components/Stars'
import { api } from '../../common/services/api'
import { useAuth } from '../../common/services/authState'

export default function PopupReviewWrite() {
  const { id = '' } = useParams()
  const nav = useNavigate()
  const { user } = useAuth()
  const [rating, setRating] = useState(4)
  const [text, setText] = useState('')
  const [images, setImages] = useState<string[]>([])
  const remain = 150 - text.length

  const onFiles = (files: FileList | null) => {
    if (!files) return
    const arr = Array.from(files).slice(0, 5-images.length).map(f => URL.createObjectURL(f))
    setImages(prev => [...prev, ...arr])
  }

  const save = async () => {
    await api.addReview(id, user?.name || '익명', rating, text, images)
    nav('../reviews', { replace: true })
  }

  return (
    <div>
      <div className="row" style={{alignItems:'center', justifyContent:'space-between'}}>
        <div className="muted">팝업스토어에 후기를 남겨주세요</div>
        <div><Stars value={rating} onChange={setRating} /></div>
      </div>
      <div className="field">
        <textarea value={text} onChange={e=>setText(e.target.value.slice(0,150))} rows={6} placeholder="후기를 입력해주세요" style={{padding:12, border:'1px solid #ddd', borderRadius:8}} />
        <div className="muted" style={{alignSelf:'flex-end'}}>{text.length}/150</div>
      </div>
      <div className="field">
        <label>사진을 선택해주세요 (최대 5장)</label>
        <input type="file" accept="image/*" multiple onChange={(e)=>onFiles(e.target.files)} />
        <div className="row" style={{gap:8}}>
          {images.map((src,i)=>(<img key={i} src={src} alt="preview" style={{width:80,height:80,objectFit:'cover',borderRadius:8}} />))}
        </div>
      </div>
      <button className="cta" onClick={save}>저장하기</button>
    </div>
  )
}
