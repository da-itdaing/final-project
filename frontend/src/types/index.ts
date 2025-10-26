export type Event = {
  id: string
  title: string
  dateRange: string
  location: string
  image: string
}

export type Popup = {
  id: string
  title: string
  district: string
  dateRange: string
  image: string
  images?: string[]
  address?: string
  lat?: number
  lng?: number
  categories?: string[]
  description?: string
  sourceUrl?: string
  consumerCats?: string[]
  notes?: string[]
  views?: number
  likes?: number
}

export type Product = {
  id: string
  name: string
  price: number
  image: string
  stock: number
}

export type Order = {
  id: string
  productId: string
  qty: number
  status: 'paid' | 'preparing' | 'shipped'
}

export type Report = {
  id: string
  title: string
  metric: string
  value: number
}

export type Review = {
  id: string
  popupId: string
  userName: string
  rating: number // 1-5
  date: string // YYYY-MM-DD
  text: string
  images?: string[]
}

export type Profile = {
  userId: string
  interests: string[]
  areas: string[]
  conveniences: string[]
}

export type CalendarItem = {
  id: string
  popupId: string
  title: string
  date: string // YYYY-MM-DD
  type: 'start' | 'end' | 'ongoing'
}

// Seller-specific types (mock)
export type SellerMessage = {
  id: string
  box: 'received' | 'sent'
  subject?: string
  body: string
  date: string // YYYY-MM-DD
  read?: boolean
}

export type SellerNotice = {
  id: string
  kind: 'platform' | 'customer'
  title: string
  body: string
  date: string // YYYY-MM-DD
  importance?: '일반' | '필수'
}
