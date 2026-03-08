"use client";

import { PaymentResponse } from "@/lib/api";

interface Props {
  payment: PaymentResponse;
  onReset: () => void;
}

export default function PaymentResult({ payment, onReset }: Props) {
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("id-ID", {
      style: "currency",
      currency: "IDR",
      minimumFractionDigits: 0,
    }).format(amount);
  };

  return (
    <div className="card">
      <div className="alert alert-success" style={{ textAlign: "center" }}>
        Pembayaran Berhasil!
      </div>

      <div className="bill-row">
        <span className="label">No. Pelanggan</span>
        <span className="value">{payment.customerId}</span>
      </div>
      <div className="bill-row">
        <span className="label">Nama</span>
        <span className="value">{payment.customerName}</span>
      </div>
      <div className="bill-row">
        <span className="label">Total Dibayar</span>
        <span className="value">{formatCurrency(payment.totalAmount)}</span>
      </div>
      <div className="bill-row">
        <span className="label">Status</span>
        <span className="status-badge status-PAID">{payment.status}</span>
      </div>
      <div className="bill-row">
        <span className="label">Referensi</span>
        <span className="value" style={{ fontSize: 12 }}>
          {payment.referenceId}
        </span>
      </div>
      <div className="bill-row">
        <span className="label">Waktu Bayar</span>
        <span className="value">
          {new Date(payment.paidAt).toLocaleString("id-ID")}
        </span>
      </div>

      <button
        className="btn btn-primary"
        onClick={onReset}
        style={{ marginTop: 16 }}
      >
        Cek Tagihan Lain
      </button>
    </div>
  );
}
