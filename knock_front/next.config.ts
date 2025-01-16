import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  logging: {
    fetches: {
      fullUrl: true,
    },
  },
  sassOptions: {
    prependData: "@use '@/styles/mixin' as *; @use '@/styles/variables' as *;",
  },
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'img.megabox.co.kr',
        pathname: '/SharedImg/**',
      },
    ],
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: `${process.env.NEXT_PUBLIC_API_BACKEND_URL}/api/:path*`,
      },
    ];
  },
};

export default nextConfig;
