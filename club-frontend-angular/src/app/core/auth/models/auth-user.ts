import {RoleType} from './role-type';

export interface AuthUser {
    id: string;
    username: string;
    roles: Array<RoleType>;
    lastLogin: string;
}

export interface  AuthData  {
    id: string;
    username: string;
    roles: Array<RoleType>;
    token: string;
    refreshToken: string;
    lastLogin: string;
}
