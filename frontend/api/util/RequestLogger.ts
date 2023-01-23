import {LOGGER} from "@api/util/Logger";
import {StringBuilder} from "@api/util/StringBuilder";

export default class RequestLogger {
    
    static log(path: RequestInfo | URL, request: RequestInit) {
        const sb: StringBuilder = new StringBuilder();
        const method: string = request.method ?? "GET";
        
        sb.append("[RequestLogger] Invoked the following request: \n")
            .append(method).append(' ')
            .append(path.toString()).append("\n");
        
        this.appendHeaders(request.headers, sb);
        this.appendBody(request.body, sb);
        this.appendCache(request, sb);
        this.appendCredentials(request, sb);
        this.appendKeepAlive(request, sb);
        this.appendModeOptions(request, sb);
        this.appendRedirects(request, sb);
        this.appendReferrer(request, sb);
        this.appendReferrerPolicy(request, sb);
        LOGGER.info(sb.build());
    }
    
    private static appendReferrerPolicy(request: RequestInit, sb: StringBuilder) {
        if (request.referrerPolicy) {
            sb.append("ReferrerPolicy: ").append(request.referrerPolicy).append("\n");
        }
    }
    
    private static appendReferrer(request: RequestInit, sb: StringBuilder) {
        if (request.referrer) {
            sb.append("Referrer: ").append(request.referrer).append("\n");
        }
    }
    
    private static appendRedirects(request: RequestInit, sb: StringBuilder) {
        if (request.redirect) {
            sb.append("Redirect: ").append(request.redirect).append("\n");
        }
    }
    
    private static appendModeOptions(request: RequestInit, sb: StringBuilder) {
        if (request.mode) {
            sb.append("Mode: ").append(request.mode).append("\n");
        }
    }
    
    private static appendKeepAlive(request: RequestInit, sb: StringBuilder) {
        if (request.keepalive) {
            sb.append("Keepalive: ").append(request.keepalive.toString()).append("\n");
        }
    }
    
    private static appendCredentials(request: RequestInit, sb: StringBuilder) {
        if (request.credentials) {
            sb.append("Credentials: ").append(request.credentials).append("\n");
        }
    }
    
    private static appendCache(request: RequestInit, sb: StringBuilder) {
        if (request.cache) {
            sb.append("Cache: ").append(request.cache).append("\n");
        }
    }
    
    private static appendBody(body: BodyInit | null | undefined, sb: StringBuilder) {
        if (body) {
            sb.append("Body:\n");
            if (body instanceof ReadableStream) {
                LOGGER.debug("[RequestLogger] Body is a ReadableStream.");
                const reader = body.getReader();
                reader.read().then(async function processText({value}): Promise<void> {
                    sb.append(new TextDecoder("utf-8").decode(value));
                    return await reader.read().then(processText);
                });
            } else if (typeof body === 'string') {
                LOGGER.debug("[RequestLogger] Body is a string.");
                sb.append(body);
            } else if (body instanceof Blob) {
                LOGGER.debug("[RequestLogger] Body is a Blob.");
                let fileReader = new FileReader();
                fileReader.onload = function () {
                    sb.append(fileReader.result as string);
                };
                fileReader.readAsText(body);
            } else if (body instanceof ArrayBuffer) {
                LOGGER.debug("[RequestLogger] Body is an ArrayBuffer.");
                sb.append(new TextDecoder("utf-8").decode(body));
            } else if (body instanceof FormData) {
                LOGGER.debug("[RequestLogger] Body is a FormData.");
                for (const pair of body.entries() as any) {
                    sb.append(pair[0] + ', ' + pair[1] + '\n');
                }
            } else if (body instanceof URLSearchParams) {
                LOGGER.debug("[RequestLogger] Body is a URLSearchParams.");
                for (const [key, value] of body.entries() as any) {
                    sb.append(`${key}=${value}\n`);
                }
            } else {
                LOGGER.error("[RequestLogger] Unsupported body type.");
            }
        }
    }
    
    private static appendHeaders(headers: HeadersInit | undefined, sb: StringBuilder) {
        if (headers) {
            sb.append("Headers:\n");
            if (headers instanceof Headers) {
                headers.forEach((value, key) => {
                    sb.append(`${key}: ${value}\n`);
                });
            } else if (Array.isArray(headers)) {
                headers.forEach(([key, value]) => {
                    sb.append(`${key}: ${value}\n`);
                });
            } else {
                for (const key in headers) {
                    if (headers.hasOwnProperty(key)) {
                        sb.append(`${key}: ${headers[key]}\n`);
                    }
                }
            }
        }
    }
}

