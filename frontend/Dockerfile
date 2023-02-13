FROM node:19 as builder

# update packages, to reduce risk of vulnerabilities
RUN apt-get update && apt-get upgrade -y && apt-get autoclean -y && apt-get autoremove -y

# set a non-privileged user to use when running this image
RUN groupadd -r aniflix && useradd -g aniflix -s /bin/bash -d /home/aniflix -m aniflix
USER aniflix

# set right (secure) folder permissions
RUN mkdir -p /home/aniflix/app/node_modules && chown -R aniflix:aniflix /home/aniflix/app
WORKDIR /home/aniflix/app

# Setup environment variables which are used in the next application
ARG NEXT_PUBLIC_CONSUMET_BASE_URL
ARG NEXT_PUBLIC_POCKETBASE_URL
ARG NEXT_PUBLIC_STRIPE_LINK
ARG NEXT_PUBLIC_LOGGING_LEVEL
ARG NEXT_NODE_ENV
ARG NEXT_PORT

# Setup environment variables which are used in the next application
ENV NEXT_PUBLIC_CONSUMET_BASE_URL=${NEXT_PUBLIC_CONSUMET_BASE_URL}
ENV NEXT_PUBLIC_POCKETBASE_URL=${NEXT_PUBLIC_POCKETBASE_URL}
ENV NEXT_PUBLIC_STRIPE_LINK=${NEXT_PUBLIC_STRIPE_LINK}
ENV NEXT_PUBLIC_LOGGING_LEVEL=${NEXT_PUBLIC_LOGGING_LEVEL}
ENV NODE_ENV=${NEXT_NODE_ENV}
ENV PORT=${NEXT_PORT}

# Copy project definition/dependencies files, for better reuse of layers
COPY --chown=aniflix:aniflix package*.json /home/aniflix/app

# Copy all sources in the container (exclusions in .dockerignore file)
COPY --chown=aniflix:aniflix . /home/aniflix/app

# install dependencies here, for better reuse of layers
RUN npm install && npm update && npm cache clean --force

# Build the application
RUN npm run build

# exposed port/s
EXPOSE 3000

# Add an healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s CMD npm run healthcheck-manual

# run the application
CMD [ "npm", "run", "start" ]
