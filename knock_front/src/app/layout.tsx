import { Providers } from '@/redux/provider';
import './globals.scss';
import '@/utils/firebaseConfig';
import type { Metadata, Viewport } from 'next';
import { Noto_Sans_KR, Anton, Roboto } from 'next/font/google';
import styles from './layout.module.scss';
import { UnauthorizedModal } from '@/components/UnauthorizedModal';

export const metadata: Metadata = {
  title: 'KNOCK',
  description: '영화, 공연예술을 사랑하는 당신을 위한 티켓팅 헬퍼',
  manifest: '/manifest.json',
  icons: [{ rel: 'icon', url: '/icons/icon512_maskable.png', sizes: '512x512' }],
  appleWebApp: {
    capable: true,
    statusBarStyle: 'black-translucent',
  },
};

export const viewport: Viewport = {
  minimumScale: 1,
  initialScale: 1,
  width: 'device-width',
  viewportFit: 'cover',
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
