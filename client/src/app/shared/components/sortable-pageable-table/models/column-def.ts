export interface ColumnDef <T> {
    columnName: string;
    displayName: string;
    translateDisplayName?: boolean;
    isSortable: boolean;
    cell: (element: T) => any;
}
