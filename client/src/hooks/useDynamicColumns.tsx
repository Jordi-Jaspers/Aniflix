"use client"
import {useEffect, useState} from "react";

export const useDynamicColumns = () => {
    const [cols, setCols] = useState(1);
    const [width, setWidth] = useState(205);
    
    useEffect(() => {
        const handleResize = () => {
            const cardWidth = (window.innerWidth > 390) ? 205 : 330;
            setWidth(cardWidth);
            
            const gridWidth = window.innerWidth;
            setCols(Math.floor(gridWidth / cardWidth));
        };
        handleResize();
        window.addEventListener("resize", handleResize);
        return () => window.removeEventListener("resize", handleResize);
    }, []);
    
    return {cols, width};
};
