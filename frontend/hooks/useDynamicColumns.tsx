import {useEffect, useState} from "react";

export const useDynamicColumns = (cardWidth: number) => {
    const [cols, setCols] = useState(1);
    useEffect(() => {
        const handleResize = () => {
            const gridWidth = window.innerWidth;
            setCols(Math.floor(gridWidth / cardWidth));
        };
        handleResize();
        window.addEventListener("resize", handleResize);
        return () => window.removeEventListener("resize", handleResize);
    }, []);
    
    return cols
};
