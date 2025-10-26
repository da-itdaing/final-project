import { useAuth } from '../../common/services/authState'

export default function Profile() {
  const { user } = useAuth()
  return (
    <div className="card">
      <div className="pad">
        <h2>판매자 프로필</h2>
        <div className="row" style={{gap:16, alignItems:'center', marginTop:12}}>
          <div style={{width:72,height:72,borderRadius:'50%',background:'#eee'}} />
          <div style={{flex:1}}>
            <div style={{fontSize:18, fontWeight:700}}>{user?.name} 님</div>
            <div className="muted">계정: {user?.username} · 이메일: {user?.email}</div>
          </div>
          <button className="pill">수정하기</button>
        </div>
      </div>
    </div>
  )
}
