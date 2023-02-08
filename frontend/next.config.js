/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: true,
    images: {
        domains: ['media.kitsu.io', 'rb.gy', 's4.anilist.co', 'i.ytimg.com', 'gogocdn.net', 'gogohd.pro', 'gofcdn.com', 'gogohd.net', 'artworks.thetvdb.com', '0.0.0.0', 'localhost'],
    }
}

module.exports = nextConfig
