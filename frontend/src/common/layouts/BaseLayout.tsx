import Header from '../components/Header'

type Props = { children: React.ReactNode }

export default function BaseLayout({ children }: Props) {
  return (
    <div>
      <Header />
      <main className="container">{children}</main>
      <footer className="footer">© 2025 DA · IT DAING · 인공지능 사관학교 6기</footer>
    </div>
  )
}