import {PermissionType} from "../../../auth/models/permission-type";

export interface NavItem {
    displayName: string;
    iconName: string;
    route: string;
    permission: PermissionType;
}
