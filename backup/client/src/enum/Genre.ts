export enum Genres {
    Action = "Action",
    Adventure = "Adventure",
    Comedy = "Comedy",
    Drama = "Drama",
    Fantasy = "Fantasy",
    Horror = "Horror",
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

export const getRandomGenre = () => {
    const genres = Object.values(Genres)
    return genres[Math.floor(Math.random() * genres.length)]
}

// create a function which will select 4 random genres from the enum Genres without duplicates and return them as an array.
export const getRandomGenres = (amount: number) => {
    const genres = Object.values(Genres)
    const randomGenres: Genres[] = []
    while (randomGenres.length < amount) {
        const randomGenre = genres[Math.floor(Math.random() * genres.length)]
        if (!randomGenres.includes(randomGenre)) {
            randomGenres.push(randomGenre)
        }
    }
    return randomGenres
}
