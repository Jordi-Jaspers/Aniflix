'use client'

import type {ReactNode} from "react";
import React from "react";

import {QueryClient, QueryClientProvider} from "react-query";

interface Props {
    children: ReactNode;
}

const queryClient = new QueryClient();
const ReactQueryWrapper = ({children}: Props) => {
    return (
        <QueryClientProvider client={queryClient}>
            {children}
        </QueryClientProvider>
    )
}
export default ReactQueryWrapper
