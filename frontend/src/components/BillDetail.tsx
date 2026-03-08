"use client";

import { useState } from "react";
import { InquiryResponse, payBill, PaymentResponse } from "@/lib/api";

interface Props {
  bill: InquiryResponse;
  onPaymentSuccess: (payment: PaymentResponse) => void;
}

export default function BillDetail({ bill, onPaymentSuccess }: Props) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("id-ID", {
      style: "currency",
      currency: "IDR",
      minimumFractionDigits: 0,
    }).format(amount);
  };

  const handlePay = async () => {
    setError("");
    setLoading(true);

    try {
      // Extract billId from referenceId (stored in backend)
      const res = await payBill(1, bill.customerId);
      if (res.success) {
        onPaymentSuccess(res.data);
      } else {
        setError(res.message);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "Pembayaran gagal");
    } finally {
      setLoading(false);
    }
  };

  const isPaid = bill.status === "PAID";

  return (
    <div className="card bill-detail">
      <h2>Detail Tagihan</h2>
      {error && <div className="alert alert-error">{error}</div>}

      <div className="bill-row">
        <span className="label">No. Pelanggan</span>
        <span className="value">{bill.customerId}</span>
      </div>
      <div className="bill-row">
        <span className="label">Nama</span>
        <span className="value">{bill.customerName}</span>
      </div>
      <div className="bill-row">
        <span className="label">Tarif / Daya</span>
        <span className="value">
          {bill.tariff} / {bill.power}
        </span>
      </div>
      <div className="bill-row">
        <span className="label">Periode</span>
        <span className="value">{bill.period}</span>
      </div>
      <div className="bill-row">
        <span className="label">Tagihan</span>
        <span className="value">{formatCurrency(bill.amount)}</span>
      </div>
      <div className="bill-row">
        <span className="label">Biaya Admin</span>
        <span className="value">{formatCurrency(bill.adminFee)}</span>
      </div>
      <div className="bill-row total">
        <span className="label">Total Bayar</span>
        <span className="value">{formatCurrency(bill.totalAmount)}</span>
      </div>
      <div className="bill-row">
        <span className="label">Status</span>
        <span className={`status-badge status-${bill.status}`}>
          {bill.status}
        </span>
      </div>

      {!isPaid && (
        <button
          className="btn btn-success"
          onClick={handlePay}
          disabled={loading}
          style={{ marginTop: 16 }}
        >
          {loading ? (
            <>
              <span className="spinner" />
              Memproses...
            </>
          ) : (
            `Bayar ${formatCurrency(bill.totalAmount)}`
          )}
        </button>
      )}
    </div>
  );
}
