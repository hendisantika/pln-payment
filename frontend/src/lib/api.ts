const BASE_URL = "/api/pln";

export interface InquiryRequest {
  customerId: string;
  productCode: string;
}

export interface InquiryResponse {
  customerId: string;
  customerName: string;
  tariff: string;
  power: string;
  period: string;
  amount: number;
  adminFee: number;
  totalAmount: number;
  status: string;
  referenceId: string;
  message: string;
}

export interface PaymentResponse {
  billId: number;
  customerId: string;
  customerName: string;
  totalAmount: number;
  status: string;
  referenceId: string;
  paidAt: string;
  message: string;
}

export interface PlnBill {
  id: number;
  customerId: string;
  customerName: string;
  tariff: string;
  power: string;
  period: string;
  amount: number;
  adminFee: number;
  totalAmount: number;
  status: string;
  referenceId: string;
  paidAt: string | null;
  createdAt: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export async function inquiryPln(
  request: InquiryRequest
): Promise<ApiResponse<InquiryResponse>> {
  const res = await fetch(`${BASE_URL}/inquiry`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(request),
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.message || "Gagal melakukan inquiry");
  }
  return res.json();
}

export async function payBill(
  billId: number,
  customerId: string
): Promise<ApiResponse<PaymentResponse>> {
  const res = await fetch(`${BASE_URL}/pay`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ billId, customerId }),
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.message || "Gagal melakukan pembayaran");
  }
  return res.json();
}

export async function getHistory(
  customerId?: string
): Promise<ApiResponse<PlnBill[]>> {
  const url = customerId
    ? `${BASE_URL}/history?customerId=${customerId}`
    : `${BASE_URL}/history`;
  const res = await fetch(url);
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.message || "Gagal mengambil riwayat");
  }
  return res.json();
}
