import './globals.scss';
import './layout.module.scss';
import type { Metadata, Viewport } from 'next';

export const metadata: Metadata = {
  title: 'Knock',
  description: 'Generated by create next app',
};

export const viewport: Viewport = {
  width: 'device-width',
  initialScale: 1,
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <head>
        <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.min.css"
        />
      </head>
      <body>{children}</body>
    </html>
  );
}
