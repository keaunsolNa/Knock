import { Providers } from '@/redux/provider';
import './globals.scss';
import type { Metadata, Viewport } from 'next';
import { Noto_Sans_KR, Anton, Roboto } from 'next/font/google';
import styles from './layout.module.scss';
import { UnauthorizedModal } from '@/components/UnauthorizedModal';

export const metadata: Metadata = {
  title: 'Knock',
  description: 'Generated by create next app',
};

export const viewport: Viewport = {
  width: 'device-width',
  initialScale: 1,
};

const notoSansKR = Noto_Sans_KR({
  subsets: ['latin'],
  weight: ['400', '700'],
  variable: '--font-noto-sans-kr',
});

const anton = Anton({
  subsets: ['latin'],
  weight: ['400'],
  variable: '--font-anton',
});
const roboto = Roboto({
  subsets: ['latin'],
  weight: ['400'],
  variable: '--font-roboto',
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={`${notoSansKR.variable} ${anton.variable} ${roboto.variable}`}>
        <Providers>
          <div className={styles.container}>
            {children}
            <UnauthorizedModal />
          </div>
        </Providers>
      </body>
    </html>
  );
}
