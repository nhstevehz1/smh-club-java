export class NavItem {
    displayName: string ='';
    disabled: boolean = false;
    iconName: string = '';
    route: string = '';
    children: NavItem[] = [];

    public static Empty() : NavItem {
        let navItem = new NavItem();
        navItem.displayName = '';
        navItem.disabled = true;
        navItem.route = '';
        navItem.iconName = '';
        navItem.children = [];
        return navItem;
    }
}
