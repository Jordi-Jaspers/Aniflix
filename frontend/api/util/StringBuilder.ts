export class StringBuilder {
    
    private _parts: string[];
    
    /**
     * Initializes a new instance of the StringBuilder class.
     */
    constructor() {
        this._parts = [];
    }
    
    /**
     * Appends a string to the string builder.
     * @param part The string to append.
     */
    public append(part: string): StringBuilder {
        this._parts.push(part);
        return this;
    }
    
    /**
     * Builds and concatenates all parts of the string.
     */
    public build(): string {
        return this._parts.join('');
    }
}
