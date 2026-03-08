import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "PLN Payment - Cek & Bayar Tagihan Listrik",
  description: "Aplikasi cek dan bayar tagihan listrik PLN",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="id">
      <body>{children}</body>
    </html>
  );
}
