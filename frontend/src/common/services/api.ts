import { events, popups, products, orders, reports, reviews as seedReviews, profiles as seedProfiles, calendar as seedCalendar } from '../data/dummy'
import type { Popup, Review, Profile, CalendarItem } from '../../types'

// Simulate async APIs with small delays
const delay = (ms = 200) => new Promise((res) => setTimeout(res, ms))

// In-memory mutable stores derived from seeds
let reviewStore: Review[] = [...seedReviews]
let profileStore: Profile[] = [...seedProfiles]
// favorites persistence
const loadFav = (): Record<string, Set<string>> => {
  try {
    const raw = typeof localStorage !== 'undefined' ? localStorage.getItem('mock:favorites') : null
    if (!raw) return { u1: new Set(['p1']) }
    const obj = JSON.parse(raw) as Record<string, string[]>
    const map: Record<string, Set<string>> = {}
    Object.keys(obj).forEach(k => map[k] = new Set(obj[k]))
    return map
  } catch { return { u1: new Set(['p1']) } }
}
const saveFav = (store: Record<string, Set<string>>) => {
  try {
    const obj: Record<string, string[]> = {}
    Object.keys(store).forEach(k => obj[k] = Array.from(store[k]))
    if (typeof localStorage !== 'undefined') localStorage.setItem('mock:favorites', JSON.stringify(obj))
  } catch {}
}
let favoriteStore: Record<string, Set<string>> = loadFav()
// popup persistence
const POPUP_KEY = 'mock:popups'
const loadPopups = (): Popup[] => {
  try {
    const raw = typeof localStorage !== 'undefined' ? localStorage.getItem(POPUP_KEY) : null
    if (!raw) return [...popups]
    const arr = JSON.parse(raw) as Popup[]
    if (!Array.isArray(arr) || !arr.length) return [...popups]
    return arr
  } catch { return [...popups] }
}
const savePopups = (arr: Popup[]) => {
  try { if (typeof localStorage !== 'undefined') localStorage.setItem(POPUP_KEY, JSON.stringify(arr)) } catch {}
}
let popupStore: Popup[] = loadPopups()
let calendarStore: CalendarItem[] = [...seedCalendar]

export const api = {
  async listEvents() {
    await delay();
    return events
  },
  async getEvent(id: string) {
    await delay();
    return events.find(e => e.id === id) || null
  },
  async listPopups() {
    await delay();
    return popupStore
  },
  async listPopupsByDistrict(district: string) {
    await delay();
    return popupStore.filter(p => p.district === district)
  },
  async getPopup(id: string) {
    await delay();
    return popupStore.find(p => p.id === id) || null
  },
  async createPopup(input: Omit<Popup, 'id'|'image'> & { image?: string }): Promise<Popup> {
    await delay();
    const id = 'p' + (popupStore.length + 1)
    // choose primary image: input.image or first from images
    const primary = input.image || (input.images && input.images[0]) || ''
    const item: Popup = { id, image: primary, views: 0, likes: 0, ...input }
    popupStore.unshift(item)
    savePopups(popupStore)
    return item
  },
  async updatePopup(id: string, patch: Partial<Popup>): Promise<Popup | null> {
    await delay();
    const idx = popupStore.findIndex(p => p.id === id)
    if (idx < 0) return null
    const next = { ...popupStore[idx], ...patch } as Popup
    // keep primary image in sync with images[0] if provided
    if (patch.images && patch.images.length > 0) {
      next.image = patch.images[0]
    }
    popupStore[idx] = next
    savePopups(popupStore)
    return next
  },
  async listProducts() {
    await delay();
    return products
  },
  async listOrders() {
    await delay();
    return orders
  },
  async listReports() {
    await delay();
    return reports
  },
  // Favorites
  async listFavorites(userId: string) {
    await delay();
    const favs = Array.from(favoriteStore[userId] || new Set())
    return popupStore.filter(p => favs.includes(p.id))
  },
  async toggleFavorite(userId: string, popupId: string) {
    await delay();
    if (!favoriteStore[userId]) favoriteStore[userId] = new Set()
    const set = favoriteStore[userId]
    if (set.has(popupId)) set.delete(popupId)
    else set.add(popupId)
    saveFav(favoriteStore)
    return set.has(popupId)
  },
  async isFavorite(userId: string, popupId: string) {
    await delay();
    return favoriteStore[userId]?.has(popupId) ?? false
  },
  // Reviews
  async listReviews(popupId: string) {
    await delay();
    return reviewStore.filter(r => r.popupId === popupId)
  },
  async addReview(popupId: string, userName: string, rating: number, text: string, images: string[] = []) {
    await delay();
    const rv: Review = { id: 'rv' + (reviewStore.length + 1), popupId, userName, rating, date: new Date().toISOString().slice(0,10), text, images }
    reviewStore.unshift(rv)
    return rv
  },
  async deleteReview(reviewId: string) {
    await delay();
    reviewStore = reviewStore.filter(r => r.id !== reviewId)
    return true
  },
  async averageRating(popupId: string) {
    const list = reviewStore.filter(r => r.popupId === popupId)
    if (!list.length) return 0
    return +(list.reduce((a,b)=>a+b.rating,0)/list.length).toFixed(1)
  },
  async listMyReviews(userName: string) {
    await delay();
    return reviewStore.filter(r => r.userName === userName)
  },
  // Profile
  async getProfile(userId: string) {
    await delay();
    let p = profileStore.find(p => p.userId === userId)
    if (!p) { p = { userId, interests: [], areas: [], conveniences: [] }; profileStore.push(p) }
    return p
  },
  async updateProfile(userId: string, data: Partial<Profile>) {
    await delay();
    const idx = profileStore.findIndex(p => p.userId === userId)
    if (idx === -1) profileStore.push({ userId, interests: [], areas: [], conveniences: [], ...data } as Profile)
    else profileStore[idx] = { ...profileStore[idx], ...data }
    try { if (typeof localStorage !== 'undefined') localStorage.setItem('mock:profiles', JSON.stringify(profileStore)) } catch {}
    return profileStore.find(p => p.userId === userId)!
  },
  // Calendar
  async listCalendar(userId: string) {
    await delay();
    return calendarStore
  }
}
