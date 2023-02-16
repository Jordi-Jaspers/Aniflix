FROM node:19

LABEL version="0.0.3-SNAPSHOT"
LABEL description="A beautiful UI for your favorite anime shows, movies, and tv series. Watch anime for free without ads on any device."

# update packages, to reduce risk of vulnerabilities
#RUN apt-get update && apt-get upgrade -y && apt-get autoclean -y && apt-get autoremove -y

# set a non-privileged user to use when running this image
RUN groupadd -r aniflix && useradd -g aniflix -s /bin/bash -d /home/aniflix -m aniflix
USER aniflix

# set right (secure) folder permissions
RUN mkdir -p /home/aniflix/app/node_modules && chown -R aniflix:aniflix /home/aniflix/app
WORKDIR /home/aniflix/app

# Copy project definition/dependencies files, for better reuse of layers
COPY --chown=aniflix:aniflix package*.json /home/aniflix/app/

# Install dependencies here, for better reuse of layers
RUN npm install --legacy-peer-deps

# Copy all sources in the container (exclusions in .dockerignore file)
COPY --chown=aniflix:aniflix . /home/aniflix/app

# Build the application
RUN npm run build

# exposed port/s
EXPOSE 3000

# run the application
CMD [ "npm", "run", "start" ]
