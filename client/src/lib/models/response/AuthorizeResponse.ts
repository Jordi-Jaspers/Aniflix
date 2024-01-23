export type AuthorizeResponse = {
  email: string;
  authorities: string[];
  lastLogin: Date;
  accessToken: string;
  refreshToken: string;
  expiresAt: Date;
  enabled: boolean;
  validated: boolean;
};
