import {useIsAuthenticated} from "@hooks/useIsAuthenticated";

import type {ReactNode} from 'react';


export default function RouteGuard({children}: { children: ReactNode }) {
    const {isAuthorized} = useIsAuthenticated()
    return (
        <>
            {isAuthorized && children}
        </>
    );
}
