<p align="center">
    <img width="150px" src="https://github.com/Jordi-Jaspers/Aniflix/blob/develop/documentation/assets/aniflix-icon-medium.png" alt="logo"/>
    <h1 align="center">Aniflix (Development)</h1>
</p>
<p align="center">ad-free anime streaming website with a modern UI and QoL features.</p>
<p align="center">
    <img alt="license" src="https://img.shields.io/github/license/Jordi-Jaspers/aniflix"> 
    <img alt="GitHub release (with filter)" src="https://img.shields.io/github/v/release/Jordi-Jaspers/Aniflix?sort=semver">
    <img alt="GitHub Issues or Pull Requests" src="https://img.shields.io/github/issues/Jordi-Jaspers/aniflix?color=red">
    <img src="https://img.shields.io/github/commit-activity/m/Jordi-Jaspers/aniflix" alt="Commit Activity" >
    <img src="https://img.shields.io/github/last-commit/Jordi-Jaspers/aniflix" alt="Last Commit" >
    <a href="https://discord.gg/Y6YUQfEJ">
        <img alt="Discord" src="https://img.shields.io/discord/1246037775673720862?label=discord&color=%235567E3">
    </a>
</p>

---

**Author:** Jordi
Jaspers [[Github](https://github.com/Jordi-Jaspers "Github Page"), [Linkedin](https://www.linkedin.com/in/jordi-jaspers/ "Linkedin Page")]
<p align="left">
      <a href="https://ie.linkedin.com/in/jordi-jaspers">
         <img alt="Mail" title="Connect via email" src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white"/></a>
      <a href="https://ie.linkedin.com/in/jordi-jaspers">
         <img alt="LinkedIn" title="Connect on LinkedIn" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"/></a>
      <a href="https://github.com/Jordi-Jaspers?tab=followers">
         <img alt="followers" title="Follow me on Github" src="https://custom-icon-badges.demolab.com/github/followers/Jordi-Jaspers?color=236ad3&labelColor=1155ba&style=for-the-badge&logo=person-add&label=Follow&logoColor=white"/></a>
      <a href="https://github.com/Jordi-Jaspers?tab=repositories&sort=stargazers">
         <img alt="total stars" title="Total stars on GitHub" src="https://custom-icon-badges.demolab.com/github/stars/Jordi-Jaspers?color=55960c&style=for-the-badge&labelColor=488207&logo=star"/></a>
    <a href="https://donate.stripe.com/4gw5mfcbu3yo4kUaEE">
        <img alt="Donate" title="Donate" src="https://img.shields.io/badge/Donate-Stripe-FF69B4?style=for-the-badge&logo=stripe&logoColor=white"/></a>
</p>

---

## 📝 Introduction

Welcome to **Aniflix** - a modern anime streaming website with a focus on quality of life features. Backed by the powerful [Consumet API](https://github.com/consumet/consumet.ts). 

The client is build with SvelteKit and TailwindCSS using bun as JS runtime for the fastest response times and smoothest user experience as you consume you daily amount of anime. 

The server is built with Spring Boot for its security and robustness. This way you don't have to worry about any downtime or security issues. Not only that but it also comes with a lot of features like bookmarks, watch history, news, recommendations, watch history, etc. All this while keeping the website ad-free and easy to use.

**📦 Features**
<details>
<summary>Complete list:</summary>

[//]: # (Add a list of features here)

- [x] **User Authentication**
- [x] **User Profile**
- [x] **User Settings**
- [x] **Anime Search**
- [x] **Anime Details**
- [x] **Anime Episodes**
- [x] **Build your own library**
- [x] **Add likes to Anime you love**
- [x] **Remember playback/Watch History**
- [x] **Latest News Page**
- [x] **Provider Health status check**
- [x] **Trailer preview**
- [x] **Custom video player for Web/Mobile**
- [x] **Responsive Design**
- [x] **Recommendations**
- [ ] **Continue Watching**
- [ ] **Anime Schedule**
- [ ] **...**
</details>

**Website Preview:** https://aniflix.stream/  
**Website Health endpoint:** https://aniflix.stream/actuator/health  
**Server API description:** https://aniflix.stream/api/public/docs/openapi.html

<img title="img" alt="preview" src="https://github.com/Jordi-Jaspers/Aniflix/blob/develop/documentation/assets/img.png">

## Dang, you found a bug, now what? 🐞

No worries, just check out our [**issues**](https://github.com/Jordi-Jaspers/Aniflix) section on GitHub. If you can't find your issue, feel free to create a new one. We trample that bug as soon as possible.

## ☕️ Support & Contributions

#### Want to contribute to the project?
All the extra help is welcome! Just fork the project and create a pull request. If you have any questions, feel free to contact me via email <jordijaspers@gmail.com> or discord for any additional information.

#### Any more ideas for features?
Feel free to create an issue and we can discuss it further.

#### Want to support the project?
You can support and keep alive your anime sanctum by donating here:

<p>
    <a href="https://donate.stripe.com/4gw5mfcbu3yo4kUaEE">
        <img alt="Donate" title="Donate" src="https://img.shields.io/badge/Donate-Stripe-FF69B4?style=for-the-badge&logo=stripe&logoColor=white"/>
    </a>
</p>

## ⭐ Stargazers over time
[![Stargazers over time](https://starchart.cc/Jordi-Jaspers/Aniflix.svg?variant=adaptive)](https://starchart.cc/Jordi-Jaspers/Aniflix)

## 📢 Disclaimer

- Aniflix helps users find anime by using 3th-party API's which scrapes from various websites.
- Aniflix or its developers do not host the content on the website. All the information is fetched from publicly available API's / Websites.
- Streams are not hosted on the website and are not controlled by the developers.
- Aniflix is not responsible for any misuse of the content outside the website.
- Aniflix is not responsible for incorrect information or broken links.
- For internet violations, please contact the source website. The developer is not legally responsible.

## 🙌 Credits

**Dependent API's**

- [Consumet API](https://github.com/consumet/consumet.ts): Fetch initial data and episode links
- [ani.zip API](https://api.ani.zip/mappings?anilist_id=21): Extra Episode info
- [Jikan API](https://jikan.moe/): Extra Anime info

**Dependent Datasources**

- [AniList](https://anilist.gitbook.io/anilist-apiv2-docs/): User info
- [MyAnimeList](https://myanimelist.net/apiconfig/references/api/v2): User info
- [GogoAnime](https://anitaku.so/): Episode links
- [Zoro](https://hianime.to/): Episode links

## 🛠️ Stack

- [Spring Boot](https://spring.io/projects/spring-boot) - Java framework for building back-end applications.
- [Redis](https://redis.io/) - In-memory data structure store.
- [SvelteKit](https://kit.svelte.dev/) - Frontend framework for building web applications.
- [TailwindCSS](https://tailwindcss.com/) - Utility-first CSS framework.
- [Shadcn Svelte](https://www.shadcn-svelte.com/) - Svelte components library.
- [Lucide](https://lucide.dev/) - SVG icons library.
- [Cloudflare](https://www.cloudflare.com/) - CDN and DNS provider.
- [Traefik](https://traefik.io/) - Reverse proxy and load balancer.
- [Docker](https://www.docker.com/) - Containerization platform.
- [Liquibase](https://www.liquibase.org/) - Database migration tool
- [MariaDB](https://mariadb.org/) - Database
- [Git](https://git-scm.com/) - Version Control

## 📜 License

Licensed under [GPL-3.0](https://www.gnu.org/licenses/gpl-3.0.html#license-text).
