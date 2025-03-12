import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  reactStrictMode: false,
  logging: {
    fetches: {
      fullUrl: true,
    },
  },
  sassOptions: {
    prependData:
      " @use '@/styles/variables' as *; @use '@/styles/mixin' as *; @use '@/styles/z-index' as *;",
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
        protocol: 'https',
        hostname: 'www.lottecinema.co.kr',
        pathname: '/NLCHS/Content/images/movie/**',
      },
      {
        protocol: 'https',
        hostname: 'cf.lottecinema.co.kr',
        pathname: '/NLCHS/Content/images/movie/**',
      },
      {
        protocol: 'https',
        hostname: 'cf.lottecinema.co.kr',
        pathname: '/Media/MovieFile/MovieImg/**',
      },
    ],
  },
};

export default nextConfig;
