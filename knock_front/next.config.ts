import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  sassOptions: {
    prependData: "@use '@/styles/mixin' as *; @use '@/styles/variables' as *;",
  },
};

export default nextConfig;
