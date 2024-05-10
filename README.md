<h1 align="center">
  ANIFLIX
</h1>
<p align="center">
    <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License" >
    <img src="https://img.shields.io/github/package-json/v/Jordi-Jaspers/aniflix?filename=client%2Fpackage.json" alt="application version">
    <img src="https://img.shields.io/github/commit-activity/m/Jordi-Jaspers/aniflix" alt="Commit Activity" >
    <img src="https://img.shields.io/github/last-commit/Jordi-Jaspers/aniflix" alt="Last Commit" >
</p>

---

## Table of Contents

- [Introduction](#introduction)
- [Learning Goals](#learning-goals)
- [Notes](#notes)
- [Stack](#stack)
- [References](#references)

---

## 📝 Introduction

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
   </p>

**Website Preview:** https://aniflix.stream/
**Website Health endpoint:** https://aniflix.stream/actuator/health
**Server API description:** https://aniflix.stream/api/public/docs/openapi.html

**Description:**

> An enterprise level streaming project with a dedicated spring boot server which will interact with mulitple external API's and it's own
> database. The client will be build with SvelteKit and TailwindCSS. The application is an anime streamings website with quality of life
> features like bookmarks, watch history, news, recommendations, etc.

## 📝 Learning Goals <a name = "learning-goals"></a>

- [x] Learn SvelteKit
- [x] Learn TailwindCSS
- [x] Learn how to handle API Calls in a frontend application
- [x] Learn how to use the multiple external API's
- [x] Learn how to use Docker
- [x] Learn how to use Cache with Redit
- [x] Learn how to use Liquibase
- [x] Apply the correct way of MVC pattern for more interchangable code
- [x] Dockerize Spring Boot application
- [x] Deploy on a server with Docker Compose
- [x] Use Cloudflare for SSL
- [x] Use Traefik as a reverse proxy
- [x] CI/CD with Github Actions
- [x] ...

## 📝 Notes <a name = "notes"></a>

---
### TODO List
* Re-use of refresh token detection.
* Implementing User management
* Bug Support email
* Data sync between API and database
* ...

---

### Regexing with Asian Characters

The ranges of Unicode characters which are routinely used for Chinese and Japanese text are:

U+3040 - U+30FF: hiragana and katakana (Japanese only)
U+3400 - U+4DBF: CJK unified ideographs extension A (Chinese, Japanese, and Korean)
U+4E00 - U+9FFF: CJK unified ideographs (Chinese, Japanese, and Korean)
U+F900 - U+FAFF: CJK compatibility ideographs (Chinese, Japanese, and Korean)
U+FF66 - U+FF9F: half-width katakana (Japanese only)

to check if a character contains a japanese/roman character you can use the following regex:
`/[一-龠]+|[ぁ-ゔ]+|[ァ-ヴー]+|[a-zA-Z0-9]+|[ａ-ｚＡ-Ｚ０-９]+|[々〆〤ヶ]+/u`
---

### Autoplaying Videos

Videos can only be autoplayed in the client whenever the videos are muted. This is a policy that is enforced by the browser. The following
link will provide more information on this policy: https://developer.chrome.com/blog/autoplay/

---
### Anilist Rate Limiting

Rate limiting is currently set to 90 requests per minute.
When you make a request to the AniList API you'll receive X-RateLimit-Limit and X-RateLimit-Remaining headers in the response.

```angular2html
HTTP/1.1 200 OK
... other headers here ...
X-RateLimit-Limit: 90
X-RateLimit-Remaining: 59
If you go over the rate limit you'll receive a 1-minute timeout, any further requests in this timeout period will also include the Retry-After and X-RateLimit-Reset response headers.
```

more info: https://anilist.gitbook.io/anilist-apiv2-docs/overview/rate-limiting
---

### Docker Builds

```bash
docker pull redis:latest
docker run -v /myredis/conf:/usr/local/etc/redis -p 6379:6379 --name aniflix-redis redis:latest redis-server /usr/local/etc/redis/redis.conf

docker pull riimuru/consumet-api:latest
docker run -p 3001:3000 --name aniflix-consumet-api -d riimuru/consumet-api:latest

docker build -t aniflix-backend ./server
docker run -p 8080:8080 --name aniflix-backend -d aniflix-backend

docker build -t aniflix-frontend ./client
docker run -p 3000:3000 --name aniflix-frontend -d aniflix-frontend
```

---

### Environment variables

#### Server Environment Variables

| Variable Name                | Default Value                                 | Description                                      |
|------------------------------|-----------------------------------------------|--------------------------------------------------|
| `EMAIL_HOST`                 | localhost                                     | Hostname for the email server                    |
| `EMAIL_PORT`                 | 2500                                          | Port number for the email server                 |
| `EMAIL_ADDRESS`              | no-reply@localhost                            | Sender email address                             |
| `JWT_LIFETIME_SECONDS`       | 900                                           | Lifetime of JWT tokens in seconds                |
| `CORS_ALLOWED_ORIGINS`       | *                                             | Comma-separated list of allowed origins for CORS |
| `CONSUMET_URL`               | https://consumet.aniflix.stream               | URL for the CONSUMET service                     |
| `TOMCAT_AJP_ENABLED`         | true                                          | Whether AJP connector is enabled for Tomcat      |
| `TOMCAT_AJP_PORT`            | 8009                                          | Port number for AJP connector                    |
| `TOMCAT_AJP_SECRET_REQUIRED` | false                                         | Whether secret is required for AJP connector     |
| `TOMCAT_AJP_SECRET`          | 6qyuET3rNuzYRBtrBQjh8zCe                      | Secret for AJP connector                         |
| `LOGGING_CONFIG`             | ./config/logback-spring.xml                   | Path to the logging configuration file           |
| `SPRING_PROFILES_INCLUDE`    | development                                   | Comma-separated list of profiles to include      |
| `SPRING_REDIS_HOST`          | 100.72.31.107                                 | Hostname for the Redis server                    |
| `SPRING_REDIS_PORT`          | 6379                                          | Port number for the Redis server                 |
| `SPRING_DATASOURCE_URL`      | jdbc:mariadb://100.72.31.107:3306/tst_aniflix | URL for the MariaDB datasource                   |
| `SPRING_DATASOURCE_USERNAME` | tst_aniflix                                   | Username for the MariaDB datasource              |
| `SPRING_DATASOURCE_PASSWORD` | tst_aniflix                                   | Password for the MariaDB datasource              |
| `SHOW_SQL`                   | false                                         | Whether to show SQL queries in logs              |
| `ASYNC_CONFIG`               | ./config/async-config.yml                     | Path to the asynchronous configuration file      |

#### Client Environment Variables

| Variable Name          | Default Value          | Description                                      |
|------------------------|------------------------|--------------------------------------------------|
| `VITE_SERVER_BASE_URL` | http://localhost:8080  | Base URL for the server                          |
| `VITE_SERVER_ISSUER`   | https://aniflix.stream | Issuer for the server                            |
| `VITE_ENV`             | development            | Environment mode (e.g., development, production) |

## 🛠️ Stack <a name = "stack"></a>
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

## 🚀 References <a name = "references"></a>
* Troubleshooting: <https://stackoverflow.com/>
* Next.js: <https://nextjs.org/docs/getting-started>
* TailwindCSS: <https://tailwindcss.com/docs>
* Consumet Documentation: <https://docs.consumet.org/>
* Svelte Motion: <https://www.npmjs.com/package/svelte-motion>
* Svelte French Toast: <https://svelte-french-toast.com/>
* Svelte Player: <https://www.npmjs.com/package/svelte-player>
* Svelte Player docs: <https://fikryfahrezy.github.io/svelte-player>
* Svelte Player example: <https://github.com/fikryfahrezy/svelte-player/blob/main/src/routes/%2Bpage.svelte>
* Lucide: <https://lucide.dev/>
* Shadcn Svelte: <https://www.shadcn-svelte.com/>
* Spring Boot Redis Properties: <https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.cache.spring.cache.redis.cache-null-values>
* Jikan API: <https://jikan.moe/>
* Jikan API Docs: <https://docs.api.jikan.moe/>
* Reactive Jikan For Spring Boot: <https://github.com/SandroHc/reactive-jikan>
* screenfull: <https://www.npmjs.com/package/screenfull>
* NPM Vs. Bun Cheatsheet: <https://dev.to/equiman/npm-vs-yarn-vs-pnpm-commands-cheatsheet-3el8>

