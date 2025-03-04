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
        hostname: 'img.cgv.co.kr',
        pathname: '/Movie/Thumbnail/Poster/**',
      },
    ],
  },

  // async rewrites() {
  //   return [
  //     {
  //       source: '/api/:path*',
  //       destination: `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/:path*`,
  //     },
  //     {
  //       source: '/auth/:path*',
  //       destination: `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/auth/:path*`,
  //     },
  //   ];
  // },
};

export default nextConfig;
