import {FormControl} from "@angular/forms";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {computed, Directive, input, signal} from "@angular/core";
import {FormControlError} from "./form-control-error";

@Directive()
export abstract class InputComponentBase<T> {


    formControlSignal =
        input.required<FormControl<T>>({alias: 'formControl'});

    appearanceSignal
        = input(('outline'), { alias: 'appearance',
            transform(value: MatFormFieldAppearance | undefined) {
                const defaultVal = ('outline') as MatFormFieldAppearance;
                return value || defaultVal;
        }
    });

    labelSignal = input<string>(undefined, {alias: 'label'});

    controlErrorsSignal
        = input(<FormControlError[]>[], { alias: 'controlErrors',
            transform(value: FormControlError[] | undefined | null) {
                return value || [] as FormControlError[];
        }
    });

    protected invalidSignal = signal<boolean>(false);

    protected invalidMessageSignal = computed<string>(() => {
        if(this.invalidSignal()) {
            for (const val of this.controlErrorsSignal()) {
                const hasError = this.formControlSignal().hasError(val.type);
                if (hasError) {
                    return val.message;
                }
            }
        }
        //TODO: return a default translated message.
        return 'invalid';
    });

    protected onBlur(): void {
        this.invalidSignal.set(this.formControlSignal().invalid);
    }
}
