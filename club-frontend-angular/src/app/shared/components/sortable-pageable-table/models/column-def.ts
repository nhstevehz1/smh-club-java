export interface ColumnDef <Type> {
    name: string;
    displayName: string;
    isSortable: boolean;
    cellDelimiter?: string;
    cell: (element: Type) => any;
}

export interface ColumnCell <Type> {
    cell: (element: Type)  => any;
}
