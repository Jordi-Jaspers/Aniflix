<h1 align="center">
  ANIFLIX
</h1>
<p align="center">
    <img src="https://img.shields.io/github/package-json/v/Jordi-Jaspers/aniflix?filename=frontend%2Fpackage.json" alt="GitHub package.json version (subfolder of monorepo)">
    <img src="https://img.shields.io/github/license/Jordi-Jaspers/aniflix" alt="License" >
    <img src="https://img.shields.io/github/commit-activity/m/Jordi-Jaspers/aniflix" alt="Commit Activity" >
    <img src="https://img.shields.io/github/last-commit/Jordi-Jaspers/aniflix" alt="Last Commit" >
</p>

---

## Table of Contents

- [Introduction](#introduction)
- [Learning Goals](#learning-goals)
- [Getting Started](#getting-started)
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

**Description:**

> This is a small project to clone the Netflix UI with anime content instead of regular movies and series. The goal of this project is to learn more about Next.js and Tailwind CSS. Aswell as how to handle API calls in a frontend application. The API used for this project is the unofficial [GoGoAnime API V1](https://github.com/riimuru/gogoanime-api), there is also a newly updated [GoGoAnime API V2](https://github.com/consumet/api.consumet.org).

**Preview:**
Still need to share a link to the deployment

## 📝 Learning Goals <a name = "learning-goals"></a>

- [x] Learn Next.js
- [x] Learn TailwindCSS
- [x] Learn how to use Github Actions
- [x] Learn how to handle API Calls in a frontend application
- [x] Learn how to use the GoGoAnime API
- [x] Learn how to use the GoGoAnime API V2
- [x] Learn how to deploy a Next.js application to Vercel
- [x] (Optional) Learn how to authentication.
- [x] ...

## 🏁 Getting Started <a name = "getting-started"></a>

add information here please.



```bash
docker pull riimuru/consumet-api
docker run -p {port}:3000 riimuru/consumet-api
```

Configure the following env variables

| ENV VARIABLE                  | DESCRIPTION                              |
|-------------------------------|------------------------------------------|
| NEXT_PUBLIC_CONSUMET_BASE_URL | The base url for the consumet Anime API. |
| NEXT_PUBLIC_CONSUMET_API_KEY  | The API key for the consumet Anime API.  |


to use env variable at runtime or at client side instead of just server side you can use next.config.js or prefix env variable with NEXT_PUBLIC_*. more details....


## 🛠️ Stack <a name = "stack"></a>

- [Spring Boot](https://spring.io/projects/spring-boot) - Java framework for building back-end applications.
- [Next.js](https://nextjs.org/) - React framework for building client-side applications.
- [Liquibase](https://www.liquibase.org/) - Database migration tool
- [PostgreSQL](https://www.postgresql.org/) - Open source object-relational database system.
- [Git](https://git-scm.com/) - Version Control

## 🚀 References <a name = "references"></a>

* Troubleshooting: <https://stackoverflow.com/>
* Next.js: <https://nextjs.org/docs/getting-started>
* TailwindCSS: <https://tailwindcss.com/docs>
