export type ErrorResponse = {
  method: string;
  uri: string;
  query: null;
  contentType: string;
  statusCode: number;
  statusMessage: string;
  errorMessage: string;
  apiErrorCode: string;
  apiErrorReason: string;
};
