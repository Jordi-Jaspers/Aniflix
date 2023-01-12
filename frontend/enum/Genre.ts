export enum Genres {
    Action = "Action",
    Adventure = "Adventure",
    Comedy = "Comedy",
    Drama = "Drama",
    Fantasy = "Fantasy",
    Horror = "Horror",
    MahouShoujo = "Mahou Shoujo",
    Mecha = "Mecha",
    Music = "Music",
    Mystery = "Mystery",
    Psychological = "Psychological",
    Romance = "Romance",
    SciFi = "Sci-Fi",
    SliceOfLife = "Slice of Life",
    Sports = "Sports",
    Supernatural = "Supernatural",
    Thriller = "Thriller"
}

// create a function which will select 4 random genres from the enum Genres without duplicates and return them as an array.
export const getRandomGenres = () => {
    const genres = Object.values(Genres)
    const randomGenres: Genres[] = []
    while (randomGenres.length < 4) {
        const randomGenre = genres[Math.floor(Math.random() * genres.length)]
        if (!randomGenres.includes(randomGenre)) {
            randomGenres.push(randomGenre)
        }
    }
    return randomGenres
}
