import {PermissionType} from '@app/core/auth';

export interface NavItem {
    displayName: string;
    iconName: string;
    route: string;
    permission: PermissionType;
}
