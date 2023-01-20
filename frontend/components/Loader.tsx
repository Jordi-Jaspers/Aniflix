import React from 'react'

interface Props {
    className?: string;
}

export default function Loader({className}: Props) {
    return (
        <div className={`"${className ? className : ""} bg-black w-screen h-screen flex flex-col space-y-4 justify-center items-center objects-center"`}>
            <div className='flex space-x-1'>
                <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce bounce`}/>
                <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce200`}/>
                <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce400`}/>
            </div>
        </div>
    )
}
