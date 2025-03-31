import {computed, Directive, input, signal} from '@angular/core';
import {FormControl} from '@angular/forms';
import {MatFormFieldAppearance} from '@angular/material/form-field';

import {FormControlError} from '@app/shared/components/editor-form-fields';

@Directive()
export abstract class BaseInputComponent<T> {


    formControl =
        input.required<FormControl<T>>();

    appearance
        = input(('outline' as MatFormFieldAppearance), {
            transform(value: MatFormFieldAppearance | undefined) {
                const defaultVal = ('outline') as MatFormFieldAppearance;
                return value || defaultVal;
        }
    });

    label = input<string>();

    controlErrors
        = input([] as FormControlError[], {
            transform(value: FormControlError[] | undefined | null) {
                return value || [] as FormControlError[];
        }
    });

    protected invalid = signal<boolean>(false);

    protected invalidMessage = computed<string>(() => {
        if(this.invalid()) {
            for (const val of this.controlErrors()) {
                const hasError = this.formControl().hasError(val.type);
                if (hasError) {
                    return val.message;
                }
            }
        }
        //TODO: return a default translated message.
        return 'invalid';
    });

    protected onBlur(): void {
        this.invalid.set(this.formControl().invalid);
    }
}
