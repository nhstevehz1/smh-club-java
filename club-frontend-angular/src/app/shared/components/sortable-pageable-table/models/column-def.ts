export interface ColumnDef <T> {
    columnName: string;
    displayName: string;
    isSortable: boolean;
    cell: (element: T) => any;
}
