// @ts-check

/**
 * Run `build` or `dev` with `SKIP_ENV_VALIDATION` to skip env validation.
 * This is especially useful for Docker builds.
 */
!process.env.SKIP_ENV_VALIDATION && (await import("./src/env.mjs"));

/** @type {import("next").NextConfig} */
const config = {
  reactStrictMode: true,
  images: {
    domains: [
      'media.kitsu.io',
      'rb.gy',
      's4.anilist.co',
      'i.ytimg.com',
      'gogocdn.net',
      'gogohd.pro',
      'gofcdn.com',
      'gogohd.net',
      'artworks.thetvdb.com',
      '0.0.0.0',
      'pocketbase.aniflix.stream',
      'consumet.aniflix.stream',
      'localhost'
    ],
  }
};
export default config;
