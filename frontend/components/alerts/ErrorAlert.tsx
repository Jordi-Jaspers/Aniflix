import React from 'react';

interface Props {
    message: string;
    show: boolean;
}

export default function ErrorAlert({message, show}: Props) {
    return (
        <div className={`${show ? "block" : "hidden"} bg-red-300/80 rounded-lg py-3 px-6 mb-4 text-poppins text-red-700 text-xs text-light mb-3`} role="alert">
            <p>{message}</p>
        </div>
    );
}
