export interface AuthUser {
    username: string;
    roles: Array<RoleType>;
}

export enum RoleType {
    Admin = 'ROLE_club-admin',
    User = 'ROLE_club-user',
}
