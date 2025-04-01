import {PermissionType} from '@app/core/auth/models/permission-type';

export interface NavItem {
    displayName: string;
    iconName: string;
    route: string;
    permission: PermissionType;
}
