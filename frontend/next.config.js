/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  images: {
    domains: ['rb.gy', 's4.anilist.co', 'i.ytimg.com', 'gogocdn.net', 'gogohd.pro', 'gofcdn.com', 'gogohd.net'],
  }
}

module.exports = nextConfig
