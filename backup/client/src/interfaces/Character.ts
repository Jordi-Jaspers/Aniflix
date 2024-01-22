export interface Character {
    interfaceType: "Character";
    id: number,
    role: string,
    name: {
        first: string,
        last: string,
        full: string
    },
    image: string,
}

