import {FormControl, FormGroup} from "@angular/forms";

export type FormModelGroup<T> = FormGroup<{
    [K in keyof T]: FormControl<T[K]>;
}>;
