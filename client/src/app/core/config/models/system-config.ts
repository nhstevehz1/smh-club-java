import {PermissionType} from '@app/core/auth/models/permission-type';

export interface SystemConfig {
    permissions: RolePermission[];
}

export interface RolePermission {
    role_name: string;
    permission: PermissionType;
}
