export type Role = 'consumer' | 'seller' | 'manager'

export type User = {
  id: string
  username: string
  name: string
  email: string
  role: Role
}

// Simple in-memory users for demo
const users: User[] = [
  { id: 'u1', username: 'cons1', name: '홍길동', email: 'cons1@example.com', role: 'consumer' },
  { id: 'u2', username: 'sell1', name: '이상철', email: 'sell1@example.com', role: 'seller' },
  { id: 'u3', username: 'admin', name: '관리자', email: 'admin@example.com', role: 'manager' },
]

const passwords: Record<string, string> = {
  cons1: '1234',
  sell1: '1234',
  admin: 'admin',
}

let tempConsumerJoin: any = null

export const authService = {
  async login(role: Role | 'auto', username: string, password: string): Promise<User | null> {
    // 'auto' means role-agnostic (e.g., 관리자)
    const found = users.find(u => u.username === username && (role === 'auto' || u.role === role))
    if (found && passwords[username] === password) return found
    return null
  },
  async registerSeller(data: { name: string; username: string; nickname?: string; password: string; email: string }): Promise<User> {
    const u: User = { id: 'u' + (users.length + 1), username: data.username, name: data.name, email: data.email, role: 'seller' }
    users.push(u)
    passwords[data.username] = data.password
    return u
  },
  async saveConsumerDraft(data: any) { tempConsumerJoin = { ...(tempConsumerJoin||{}), ...data } },
  async registerConsumer(categories: { interests: string[]; areas: string[]; styles: string[] }): Promise<User> {
    const base = tempConsumerJoin || { name: '사용자', username: 'user' + (users.length + 1), email: 'user@example.com', password: '1234' }
    const u: User = { id: 'u' + (users.length + 1), username: base.username, name: base.name, email: base.email, role: 'consumer' }
    users.push(u)
    passwords[base.username] = base.password
    tempConsumerJoin = null
    return u
  },
  async findId(name: string, email: string): Promise<string | null> {
    const u = users.find(v => v.name === name && v.email === email)
    return u?.username || null
  },
  async resetPassword(name: string, username: string): Promise<boolean> {
    const u = users.find(v => v.name === name && v.username === username)
    if (!u) return false
    // simulate reset
    passwords[username] = '1234'
    return true
  }
}
