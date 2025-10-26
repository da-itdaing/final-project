import type { Event, Popup, Product, Order, Report, Review, Profile, CalendarItem } from '../../types'

export const events: Event[] = [
  { id: 'e1', title: 'COFFEE WALK', dateRange: '2025.11.08', location: '국립아시아문화전당', image: 'https://images.unsplash.com/photo-1512428559087-560fa5ceab42?q=80&w=1200&auto=format&fit=crop' },
  { id: 'e2', title: '광주시민연극제', dateRange: '2025.11.07 - 11.09', location: '광주여고', image: 'https://images.unsplash.com/photo-1515165562835-c3b8c1f0eec6?q=80&w=1200&auto=format&fit=crop' },
  { id: 'e3', title: 'ACC 어린이공연', dateRange: '2025.11.15 - 11.23', location: '국립아시아문화전당', image: 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?q=80&w=1200&auto=format&fit=crop' }
]

export const popups: Popup[] = [
  { id: 'p1', title: 'MOYNAT 팝업', district: '동구', dateRange: '2025.10.16 - 10.31', image: 'https://images.unsplash.com/photo-1544441893-675973e31985?q=80&w=1200&auto=format&fit=crop', address: '광주 동구 금남로 1', lat: 35.146, lng: 126.92, categories: ['패션','럭셔리'], views: 256, likes: 32 },
  { id: 'p2', title: '드롱기로 팝업', district: '동구', dateRange: '2025.09.23 - 10.31', image: 'https://images.unsplash.com/photo-1512436991641-6745cdb1723f?q=80&w=1200&auto=format&fit=crop', address: '광주 동구 충장로 2', lat: 35.149, lng: 126.916, categories: ['주방','가전'], views: 180, likes: 12 },
  { id: 'p3', title: 'LG전자 팝업', district: '북구', dateRange: '2025.10.17 - 10.30', image: 'https://images.unsplash.com/photo-1609582848145-d69a1efe0b4b?q=80&w=1200&auto=format&fit=crop', address: '광주 북구 일곡로 3', lat: 35.187, lng: 126.91, categories: ['가전','체험'], views: 320, likes: 40 }
]

export const products: Product[] = [
  { id: 'pr1', name: '에코백', price: 19000, image: 'https://images.unsplash.com/photo-1545987796-200677ee1011?q=80&w=1200&auto=format&fit=crop', stock: 42 },
  { id: 'pr2', name: '머그컵', price: 12000, image: 'https://images.unsplash.com/photo-1551053541-41a0ad0a4b2d?q=80&w=1200&auto=format&fit=crop', stock: 18 },
  { id: 'pr3', name: '포스터', price: 8000, image: 'https://images.unsplash.com/photo-1499041893263-22a59a50b395?q=80&w=1200&auto=format&fit=crop', stock: 77 }
]

export const orders: Order[] = [
  { id: 'o1', productId: 'pr1', qty: 2, status: 'paid' },
  { id: 'o2', productId: 'pr2', qty: 1, status: 'preparing' },
  { id: 'o3', productId: 'pr3', qty: 5, status: 'shipped' }
]

export const reports: Report[] = [
  { id: 'r1', title: '일간 방문자', metric: 'visitors', value: 1240 },
  { id: 'r2', title: '주간 주문', metric: 'orders', value: 86 },
  { id: 'r3', title: '환불 요청', metric: 'refunds', value: 3 }
]

// Reviews for popups (mock)
export const reviews: Review[] = [
  { id: 'rv1', popupId: 'p1', userName: '홍길동', rating: 5, date: '2025-10-20', text: '전시 구성과 굿즈가 좋았어요!', images: [] },
  { id: 'rv2', popupId: 'p1', userName: '김유민', rating: 4, date: '2025-10-21', text: '대기시간이 조금 길었지만 만족했습니다.', images: [] },
  { id: 'rv3', popupId: 'p2', userName: '이서준', rating: 3, date: '2025-10-10', text: '체험존은 좋았고 가격은 평범.', images: [] }
]

// Default profile and calendar items (mock)
export const profiles: Profile[] = [
  { userId: 'u1', interests: ['패션','전시'], areas: ['동구','서구'], conveniences: ['주차','유모차'] }
]

export const calendar: CalendarItem[] = [
  { id: 'c1', popupId: 'p1', title: 'MOYNAT 오픈', date: '2025-10-16', type: 'start' },
  { id: 'c2', popupId: 'p1', title: 'MOYNAT 종료', date: '2025-10-31', type: 'end' },
  { id: 'c3', popupId: 'p2', title: '드롱기로 종료', date: '2025-10-31', type: 'end' }
]
