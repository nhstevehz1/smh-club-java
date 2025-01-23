export class NavItem {
    displayName: string ='';
    iconName: string = '';
    route: string = '';

    public static Empty() : NavItem {
        let navItem = new NavItem();
        navItem.displayName = '';
        navItem.route = '';
        navItem.iconName = '';
        return navItem;
    }
}
