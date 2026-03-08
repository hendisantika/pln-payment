"use client";

import { useState } from "react";
import { inquiryPln, InquiryResponse } from "@/lib/api";

interface Props {
  onResult: (data: InquiryResponse) => void;
}

export default function InquiryForm({ onResult }: Props) {
  const [customerId, setCustomerId] = useState("");
  const [productCode, setProductCode] = useState("PLNPOSTPAID");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const res = await inquiryPln({ customerId, productCode });
      if (res.success) {
        onResult(res.data);
      } else {
        setError(res.message);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "Terjadi kesalahan");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Cek Tagihan PLN</h2>
      {error && <div className="alert alert-error">{error}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="customerId">Nomor Pelanggan / ID Meter</label>
          <input
            id="customerId"
            type="text"
            placeholder="Contoh: 5301234567"
            value={customerId}
            onChange={(e) => setCustomerId(e.target.value.replace(/\D/g, ""))}
            maxLength={13}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="productCode">Jenis Layanan</label>
          <select
            id="productCode"
            value={productCode}
            onChange={(e) => setProductCode(e.target.value)}
          >
            <option value="PLNPOSTPAID">Pascabayar (Postpaid)</option>
            <option value="PLNPREPAID">Prabayar (Prepaid / Token)</option>
          </select>
        </div>
        <button type="submit" className="btn btn-primary" disabled={loading || customerId.length < 10}>
          {loading ? (
            <>
              <span className="spinner" />
              Mengecek...
            </>
          ) : (
            "Cek Tagihan"
          )}
        </button>
      </form>
    </div>
  );
}
