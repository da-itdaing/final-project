import { reviews as allReviews } from '../../common/data/dummy'
import { api } from '../../common/services/api'
import type { SellerMessage, SellerNotice, Popup, Review, CalendarItem } from '../../types'

function load<T>(key: string, fallback: T): T {
  const raw = localStorage.getItem(key)
  if (!raw) return fallback
  try { return JSON.parse(raw) as T } catch { return fallback }
}
function save<T>(key: string, value: T) { localStorage.setItem(key, JSON.stringify(value)) }

const MSG_KEY = 'seller:messages'
const NOTICE_PLATFORM_KEY = 'seller:notices:platform'
const NOTICE_CUSTOMER_KEY = 'seller:notices:customer'
const SCHEDULE_KEY = 'seller:schedule'

// seed defaults if empty
function seedMessages(): SellerMessage[] {
  const seed: SellerMessage[] = [
    { id: 'm1', box: 'received', subject: '승인 완료 안내', body: '팝업 승인 완료되었습니다.', date: '2025-10-20', read: false },
    { id: 'm2', box: 'sent', subject: '문의: 등록 절차', body: '등록 절차 문의드립니다.', date: '2025-10-18' }
  ]
  save(MSG_KEY, seed)
  return seed
}

export const sellerApi = {
  async listPopups(): Promise<Popup[]> { return api.listPopups() },

  async listMessages(box: 'received'|'sent'): Promise<SellerMessage[]> {
    let arr = load<SellerMessage[]>(MSG_KEY, [])
    if (!arr.length) arr = seedMessages()
    return arr.filter(m => m.box === box)
  },
  async sendMessage(msg: Omit<SellerMessage,'id'|'date'|'read'> & { attachments?: File[] }): Promise<SellerMessage> {
    const arr = load<SellerMessage[]>(MSG_KEY, [])
    const newMsg: SellerMessage = { id: 'm' + (arr.length + 1), box: 'sent', subject: msg.subject, body: msg.body, date: new Date().toISOString().slice(0,10) }
    arr.push(newMsg)
    save(MSG_KEY, arr)
    return newMsg
  },

  async listPlatformNotices(): Promise<SellerNotice[]> {
    return load<SellerNotice[]>(NOTICE_PLATFORM_KEY, [
      { id: 'n1', kind: 'platform', title: '필수 공지사항', body: '플랫폼 이용약관 변경 안내', date: '2025-10-17', importance: '필수' },
      { id: 'n2', kind: 'platform', title: '일반 공지사항', body: '점검 안내', date: '2025-10-10', importance: '일반' },
    ])
  },
  async getPlatformNotice(id: string): Promise<SellerNotice | undefined> {
    const arr = await this.listPlatformNotices()
    return arr.find(n => n.id === id)
  },

  async listCustomerNotices(): Promise<SellerNotice[]> {
    return load<SellerNotice[]>(NOTICE_CUSTOMER_KEY, [])
  },
  async getCustomerNotice(id: string): Promise<SellerNotice | undefined> {
    const arr = await this.listCustomerNotices()
    return arr.find(n => n.id === id)
  },
  async createCustomerNotice(input: { title: string; body: string }): Promise<SellerNotice> {
    const arr = load<SellerNotice[]>(NOTICE_CUSTOMER_KEY, [])
    const item: SellerNotice = { id: 'c' + (arr.length + 1), kind: 'customer', title: input.title, body: input.body, date: new Date().toISOString().slice(0,10) }
    arr.push(item)
    save(NOTICE_CUSTOMER_KEY, arr)
    return item
  },
  async updateCustomerNotice(id: string, patch: Partial<SellerNotice>): Promise<SellerNotice | undefined> {
    const arr = load<SellerNotice[]>(NOTICE_CUSTOMER_KEY, [])
    const idx = arr.findIndex(v => v.id === id)
    if (idx < 0) return undefined
    arr[idx] = { ...arr[idx], ...patch }
    save(NOTICE_CUSTOMER_KEY, arr)
    return arr[idx]
  },

  async listReviews(): Promise<Review[]> { return allReviews },

  async listSchedule(): Promise<CalendarItem[]> { return load<CalendarItem[]>(SCHEDULE_KEY, []) },
  async addSchedule(item: CalendarItem): Promise<void> {
    const arr = load<CalendarItem[]>(SCHEDULE_KEY, [])
    arr.push(item)
    save(SCHEDULE_KEY, arr)
  }
}
