export type RegisterResponse = {
  email: string;
  authorities: string[];
  enabled: boolean;
  validated: boolean;
};
