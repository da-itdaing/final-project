import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { api } from '../../common/services/api'

export default function PopupRegister() {
  const nav = useNavigate()
  const [sp] = useSearchParams()
  const editId = sp.get('id') || undefined
  const [form, setForm] = useState({
    title: '',
    district: '동구',
    address: '',
    start: '',
    end: '',
    popupCats: [] as string[],
    consumerCats: [] as string[],
    notes: [] as string[],
    desc: '',
    files: [] as string[],
    sourceUrl: ''
  })

  useEffect(() => {
    if (!editId) return
    api.getPopup(editId).then(p => {
      if (!p) return
      setForm(prev => ({
        ...prev,
        title: p.title || '',
        district: p.district || '동구',
        address: p.address || '',
        // start/end parsing from dateRange is best-effort only for the pattern 'YYYY-MM-DD - YYYY-MM-DD'
        start: '',
        end: '',
        popupCats: p.categories || [],
        consumerCats: p.consumerCats || [],
        notes: p.notes || [],
        desc: p.description || '',
        files: p.images || (p.image ? [p.image] : []),
        sourceUrl: p.sourceUrl || ''
      }))
    })
  }, [editId])

  function onFileChange(e: React.ChangeEvent<HTMLInputElement>) {
    const f = e.target.files?.[0]
    if (!f) return
    const r = new FileReader()
    r.onload = () => setForm(prev => ({ ...prev, files: [...prev.files, String(r.result)] }))
    r.readAsDataURL(f)
  }

  function toggle(list: 'popupCats'|'consumerCats'|'notes', v: string) {
    setForm(prev => {
      const arr = new Set(prev[list]); arr.has(v) ? arr.delete(v) : arr.add(v)
      return { ...prev, [list]: Array.from(arr) as any }
    })
  }

  function submit(e: React.FormEvent) {
    e.preventDefault()
    const dateRange = form.start && form.end ? `${form.start} - ${form.end}` : (form.start || form.end || '')
    const payload = {
      title: form.title,
      district: form.district,
      address: form.address,
      dateRange,
      images: form.files,
      categories: form.popupCats,
      consumerCats: form.consumerCats,
      notes: form.notes,
      description: form.desc,
      sourceUrl: form.sourceUrl
    }
    if (editId) {
      api.updatePopup(editId, payload).then(p => {
        alert('팝업이 수정되었습니다.')
        if (p) nav(`/seller/popups/${p.id}`)
      })
    } else {
      api.createPopup(payload as any).then(p => {
        alert('팝업이 등록되었습니다.')
        nav(`/seller/popups/${p.id}`)
      })
    }
  }

  const CAT_POPUP = ['패션','가전','전시','체험','굿즈']
  const CAT_CONSUMER = ['10대','20대','30대','40대','50대+']
  const NOTES = ['주차','포토존','유모차','반려동물','휠체어']

  return (
    <form className="card" onSubmit={submit}>
      <div className="pad">
        <h2>팝업 등록</h2>
        <div className="field"><label>팝업명</label><input value={form.title} onChange={e=>setForm({...form,title:e.target.value})} /></div>
        <div className="row" style={{gap:8}}>
          <div className="field" style={{flex:1}}>
            <label>구 선택</label>
            <select value={form.district} onChange={e=>setForm({...form,district:e.target.value})}>
              {['동구','서구','남구','북구','광산구'].map(d => <option key={d} value={d}>{d}</option>)}
            </select>
          </div>
          <div className="field" style={{flex:3}}>
            <label>주소 검색</label>
            <input placeholder="도로명 또는 지번" value={form.address} onChange={e=>setForm({...form,address:e.target.value})} />
          </div>
        </div>
        <div className="row" style={{gap:8}}>
          <div className="field" style={{flex:1}}><label>운영 시작</label><input type="date" value={form.start} onChange={e=>setForm({...form,start:e.target.value})} /></div>
          <div className="field" style={{flex:1}}><label>운영 종료</label><input type="date" value={form.end} onChange={e=>setForm({...form,end:e.target.value})} /></div>
        </div>
        <div className="field">
          <label>팝업 카테고리</label>
          <div className="row" style={{flexWrap:'wrap', gap:6}}>
            {CAT_POPUP.map(c => (
              <button type="button" key={c} className={`pill ${form.popupCats.includes(c)?'active':''}`} onClick={()=>toggle('popupCats', c)}>{c}</button>
            ))}
          </div>
        </div>
        <div className="field">
          <label>소비자 카테고리</label>
          <div className="row" style={{flexWrap:'wrap', gap:6}}>
            {CAT_CONSUMER.map(c => (
              <button type="button" key={c} className={`pill ${form.consumerCats.includes(c)?'active':''}`} onClick={()=>toggle('consumerCats', c)}>{c}</button>
            ))}
          </div>
        </div>
        <div className="field">
          <label>특이사항</label>
          <div className="row" style={{flexWrap:'wrap', gap:6}}>
            {NOTES.map(c => (
              <button type="button" key={c} className={`pill ${form.notes.includes(c)?'active':''}`} onClick={()=>toggle('notes', c)}>{c}</button>
            ))}
          </div>
        </div>
        <div className="field"><label>팝업 소개</label><textarea rows={5} value={form.desc} onChange={e=>setForm({...form,desc:e.target.value})} /></div>
        <div className="field"><label>관련 링크(소스)</label><input placeholder="https://" value={form.sourceUrl} onChange={e=>setForm({...form, sourceUrl: e.target.value})} /></div>
        <div className="field">
          <label>첨부파일</label>
          <input type="file" accept="image/*" onChange={onFileChange} />
          <div className="row" style={{gap:8, marginTop:8}}>
            {form.files.map((src, i) => <img key={i} src={src} style={{width:80,height:80,objectFit:'cover',borderRadius:6}} />)}
          </div>
        </div>
        <button className="cta" type="submit">작성</button>
      </div>
    </form>
  )
}
