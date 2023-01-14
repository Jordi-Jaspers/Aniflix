export interface Character {
    interfaceType: "Character";
    id: number,
    role: String,
    name: {
        first: string,
        last: string,
        full: string
    },
    image: string,
}

