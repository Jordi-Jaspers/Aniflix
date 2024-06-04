<h1 align="center">
  Project Notes
</h1>

---

## Table of Contents

- [Learning Goals](#learning-goals)
- [Notes](#notes)
- [Stack](#stack)
- [References](#references)

---

## 📝 Learning Goals <a name = "learning-goals"></a>

- [x] Learn SvelteKit
- [x] Learn TailwindCSS
- [x] Learn how to handle API Calls in a frontend application
- [x] Learn how to use the multiple external API's
- [x] Learn how to use Docker
- [x] Learn how to use Cache with Redis using `@Cacheable` and `@CacheEvict`
- [x] Learn how to use Liquibase
- [x] Apply the correct way of MVC pattern for more interchangeable code
- [x] Dockerized Spring Boot application
- [x] Deploy on a server with Docker Compose
- [x] Use Cloudflare for SSL
- [x] Use Traefik as a reverse proxy
- [x] CI/CD with GitHub Actions
- [x] Use of MariaDB
- [ ] ...

## 📝 Notes <a name = "notes"></a>

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

### Native Compilation with spring boot.

Native compilation is still a weird thing.. need to figure this stuff out. but that will take some time.

``` gradle
// add native build support
id("org.graalvm.buildtools.native") version "0.10.1"

graalvmNative {
    binaries.all {
        resources.autodetect()
    }
    toolchainDetection = false
}


tasks.named<JavaCompile>("compileAotJava").configure {
    options.compilerArgs.remove("-Werror")
}

tasks.named<JavaCompile>("compileAotTestJava").configure {
    options.compilerArgs.remove("-Werror")
}
```

---

### Autoplaying Videos

Videos can only be auto played in the client whenever the videos are muted. This is a policy that is enforced by the browser. The following
link will provide more information on this policy: https://developer.chrome.com/blog/autoplay/

---

### HSL Issue on IOS

IOS devices do not support HLS streaming because they have built-in HLS functionlity and should be triggered by the use of the <video> tag.

https://stackoverflow.com/questions/43287226/hls-video-streaming-on-ios-safari
https://stackoverflow.com/questions/66776643/how-can-i-stream-video-with-hls-js-in-iphone

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

| Variable Name                | Default Value                    | Description                                               |
|------------------------------|----------------------------------|-----------------------------------------------------------|
| `EMAIL_HOST`                 | localhost                        | Hostname for the email server                             |
| `EMAIL_PORT`                 | 2500                             | Port number for the email server                          |
| `EMAIL_ADDRESS`              | no-reply@localhost               | Sender email address                                      |
| `EMAIL_USERNAME`             | no-login@localhost               | The username of the mail server                           |
| `EMAIL_PASSWORD`             | password                         | Path to the asynchronous configuration file               |
| `JWT_LIFETIME_SECONDS`       | 900                              | Lifetime of JWT tokens in seconds                         |
| `CORS_ALLOWED_ORIGINS`       | *                                | Comma-separated list of allowed origins for CORS          |
| `CONSUMET_URL`               | https://localhost.com            | URL for the CONSUMET service                              |
| `TOMCAT_AJP_ENABLED`         | true                             | Whether AJP connector is enabled for Tomcat               |
| `TOMCAT_AJP_PORT`            | 8009                             | Port number for AJP connector                             |
| `TOMCAT_AJP_SECRET_REQUIRED` | false                            | Whether secret is required for AJP connector              |
| `TOMCAT_AJP_SECRET`          | 6qyuET3rNuzYRBtrBQjh8zCe         | Secret for AJP connector                                  |
| `LOGGING_CONFIG`             | ./config/logback-spring.xml      | Path to the logging configuration file                    |
| `SPRING_PROFILES_INCLUDE`    | development                      | Comma-separated list of profiles to include               |
| `SPRING_REDIS_HOST`          | 100.72.31.107                    | Hostname for the Redis server                             |
| `SPRING_REDIS_PORT`          | 6379                             | Port number for the Redis server                          |
| `SPRING_DATASOURCE_URL`      | jdbc:mariadb://mariadb:3306/test | URL for the MariaDB datasource                            |
| `SPRING_DATASOURCE_USERNAME` | username                         | Username for the MariaDB datasource                       |
| `SPRING_DATASOURCE_PASSWORD` | password                         | Password for the MariaDB datasource                       |
| `SHOW_SQL`                   | false                            | Whether to show SQL queries in logs                       |
| `ASYNC_CONFIG`               | ./config/async-config.yml        | Path to the asynchronous configuration file               |
| `SPRING_APPLICATION_NAME`    | aniflix-development              | The name of the service                                   |
| `SPRING_APPLICATION_URL`     | http://localhost:8080            | The URL of the service                                    |
| `OTLP_METRICS_EXPORT_URL`    | http://localhost:4317/v1/metrics | The URL of the OTLP metrics exporter                      |
| `OTLP_TRACING_ENDPOINT`      | http://localhost:4317/v1/traces  | The URL of the OTLP tracing endpoint                      |
| `LOG_APPENDER`               | HUMANREADABLE                    | The log appender to use (HUMANDREADABLE, LOGSTASHENCODER) |

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
* Sveltkit: <https://kit.svelte.dev/docs>
* TailwindCSS: <https://tailwindcss.com/docs>
* Consumet Documentation: <https://docs.consumet.org/>
* Svelte Motion: <https://www.npmjs.com/package/svelte-motion>
* Svelte French Toast: <https://svelte-french-toast.com/>
* Svelte Sonner: <https://sonner.emilkowal.ski/>
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
* DockerHub github actions: <https://github.com/marketplace/actions/docker-build-push-action?version=v6.4#tagging-the-image-using-gitops>
* Image resizing: <https://kit.svelte.dev/docs/images>
* HLS Issue: <https://github.com/CookPete/react-player/issues/699>
* Speed analysis: https://pagespeed.web.dev/analysis/>
* Web audit: <https://stackoverflow.com/questions/10650107/spring-aop-best-way-to-implement-an-activities-log-in-the-database>

