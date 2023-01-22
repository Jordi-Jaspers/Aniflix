export class LOGGER {
    private static STYLE = {
        BASE: [
            "color: #fff",
            "background-color: #444",
            "padding: 2px 4px",
            "border-radius: 2px"
        ],
        WARNING: [
            "color: #eee",
            "background-color: yellow",
        ],
        ERROR: [
            "color: #eee",
            "background-color: red",
        ],
        INFO: [
            "color: #eee",
            "background-color: blue"
        ],
        DEBUG: [
            "color: #eee",
            "background-color: green"
        ]
    };
    
    static warn(text: string, ...optionalParams: any[]): void {
        let style = this.STYLE.BASE.join(';') + ';';
        style += this.STYLE.WARNING.join(';');
        console.warn(`%c${text}`, style, ...optionalParams);
    }
    
    static error(text: string, ...optionalParams: any[]): void {
        let style = this.STYLE.BASE.join(';') + ';';
        style += this.STYLE.ERROR.join(';');
        console.error(`%c${text}`, style, ...optionalParams);
    }
    
    static debug(text: string, ...optionalParams: any[]): void {
        let style = this.STYLE.BASE.join(';') + ';';
        style += this.STYLE.DEBUG.join(';');
        console.debug(`%c${text}`, style, ...optionalParams);
    }
    
    static info(text: string, ...optionalParams: any[]): void {
        let style = this.STYLE.BASE.join(';') + ';';
        style += this.STYLE.INFO.join(';');
        console.info(`%c${text}`, style, ...optionalParams);
    }
    
    static log(text: string, ...optionalParams: any[]): void {
        let style = this.STYLE.BASE.join(';');
        console.log(`%c${text}`, style, ...optionalParams);
    }
}

