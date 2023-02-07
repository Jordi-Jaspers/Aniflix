import {useIsAuthenticated} from "@hooks/useIsAuthenticated";
import {NextRouter, useRouter} from 'next/router';
import {ReactNode} from 'react';

export default function RouteGuard({children}: { children: ReactNode }) {
    const {isAuthorized} = useIsAuthenticated()
    
    return (
        <>
            {isAuthorized && children}
        </>
    );
}
