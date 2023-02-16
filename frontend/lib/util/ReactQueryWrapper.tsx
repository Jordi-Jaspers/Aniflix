'use client'

import React, {ReactNode} from "react";
import {QueryClient, QueryClientProvider} from "react-query";

interface Props {
    children: ReactNode;
}

export default function ReactQueryWrapper({children}: Props) {
    const queryClient = new QueryClient();
    
    return (
        <QueryClientProvider client={queryClient}>
            {children}
        </QueryClientProvider>
    )
}
