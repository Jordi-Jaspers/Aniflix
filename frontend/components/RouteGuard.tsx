import {useIsAuthenticated} from "@hooks/useIsAuthenticated";
import {ReactNode} from 'react';

export default function RouteGuard({children}: { children: ReactNode }) {
    const {isAuthorized} = useIsAuthenticated()
    
    return (
        <>
            {isAuthorized && children}
        </>
    );
}
