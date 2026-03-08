"use client";

import { useState } from "react";
import Link from "next/link";
import InquiryForm from "@/components/InquiryForm";
import BillDetail from "@/components/BillDetail";
import PaymentResult from "@/components/PaymentResult";
import { InquiryResponse, PaymentResponse } from "@/lib/api";

export default function Home() {
  const [bill, setBill] = useState<InquiryResponse | null>(null);
  const [payment, setPayment] = useState<PaymentResponse | null>(null);

  const handleReset = () => {
    setBill(null);
    setPayment(null);
  };

  return (
    <div className="container">
      <h1>PLN Payment</h1>
      <p className="subtitle">Cek dan bayar tagihan listrik PLN Anda</p>

      <nav className="nav">
        <Link href="/" className="active">
          Cek Tagihan
        </Link>
        <Link href="/history">Riwayat</Link>
      </nav>

      {!bill && !payment && <InquiryForm onResult={setBill} />}

      {bill && !payment && (
        <BillDetail bill={bill} onPaymentSuccess={setPayment} />
      )}

      {payment && <PaymentResult payment={payment} onReset={handleReset} />}
    </div>
  );
}
