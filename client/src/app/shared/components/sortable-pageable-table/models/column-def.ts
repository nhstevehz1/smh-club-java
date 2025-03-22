export interface ColumnDef <T> {
    columnName: string;
    displayName: string;
    translateDisplayName?: boolean;
    isSortable: boolean;
    // suppressing es-lint.  any is acceptable
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    cell: (element: T) => any;
}
