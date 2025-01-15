import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  sassOptions: {
    prependData: "@use '@/styles/mixin' as *; @use '@/styles/variables' as *;",
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: `${process.env.NEXT_PUBLIC_API_SERVER_URL}/:path*`,
      },
    ];
  },
};

export default nextConfig;
