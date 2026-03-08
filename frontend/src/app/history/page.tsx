"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { getHistory, PlnBill } from "@/lib/api";

export default function HistoryPage() {
  const [bills, setBills] = useState<PlnBill[]>([]);
  const [customerId, setCustomerId] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchHistory = async (id?: string) => {
    setLoading(true);
    setError("");
    try {
      const res = await getHistory(id);
      if (res.success) {
        setBills(res.data);
      } else {
        setError(res.message);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "Gagal memuat riwayat");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchHistory();
  }, []);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    fetchHistory(customerId || undefined);
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("id-ID", {
      style: "currency",
      currency: "IDR",
      minimumFractionDigits: 0,
    }).format(amount);
  };

  return (
    <div className="container">
      <h1>PLN Payment</h1>
      <p className="subtitle">Riwayat pembayaran tagihan listrik</p>

      <nav className="nav">
        <Link href="/">Cek Tagihan</Link>
        <Link href="/history" className="active">
          Riwayat
        </Link>
      </nav>

      <div className="card">
        <h2>Riwayat Pembayaran</h2>

        <form onSubmit={handleSearch} style={{ display: "flex", gap: 8, marginBottom: 16 }}>
          <input
            type="text"
            placeholder="Filter nomor pelanggan..."
            value={customerId}
            onChange={(e) => setCustomerId(e.target.value.replace(/\D/g, ""))}
            style={{
              flex: 1,
              padding: "8px 12px",
              border: "1px solid var(--border)",
              borderRadius: 8,
              fontSize: 14,
            }}
          />
          <button type="submit" className="btn btn-primary" style={{ width: "auto" }}>
            Cari
          </button>
        </form>

        {error && <div className="alert alert-error">{error}</div>}

        {loading ? (
          <div className="loading">
            <span className="spinner" />
            Memuat...
          </div>
        ) : bills.length === 0 ? (
          <div className="loading">Belum ada riwayat pembayaran</div>
        ) : (
          <div style={{ overflowX: "auto" }}>
            <table className="history-table">
              <thead>
                <tr>
                  <th>No. Pelanggan</th>
                  <th>Nama</th>
                  <th>Periode</th>
                  <th>Total</th>
                  <th>Status</th>
                  <th>Tanggal</th>
                </tr>
              </thead>
              <tbody>
                {bills.map((bill) => (
                  <tr key={bill.id}>
                    <td>{bill.customerId}</td>
                    <td>{bill.customerName}</td>
                    <td>{bill.period}</td>
                    <td>{formatCurrency(bill.totalAmount)}</td>
                    <td>
                      <span className={`status-badge status-${bill.status}`}>
                        {bill.status}
                      </span>
                    </td>
                    <td>
                      {bill.paidAt
                        ? new Date(bill.paidAt).toLocaleDateString("id-ID")
                        : "-"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
