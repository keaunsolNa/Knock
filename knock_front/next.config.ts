import type { NextConfig } from 'next';
import withPWA from 'next-pwa';

const isProd = process.env.NODE_ENV === 'production';

const pwaConfig = withPWA({
  dest: 'public',
  disable: !isProd,
});

const nextConfig: NextConfig = {
  reactStrictMode: false,
  logging: {
    fetches: {
      fullUrl: true,
    },
  },
  sassOptions: {
    prependData: " @use '@/styles/variables' as *; @use '@/styles/mixin' as *; @use '@/styles/z-index' as *;",
  },
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'img.megabox.co.kr',
        pathname: '/SharedImg/**',
      },
      {
        protocol: 'https',
        hostname: 'img.megabox.co.kr',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'img.cgv.co.kr',
        pathname: '/Movie/Thumbnail/Poster/**',
      },
      {
        protocol: 'http',
        hostname: 'www.kopis.or.kr',
        pathname: '/upload/pfmPoster/**',
      },
    ],
    unoptimized: true,
  },
};

export default pwaConfig(nextConfig);
