import {FormControl} from "@angular/forms";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {computed, Directive, input, signal} from "@angular/core";
import {FormControlError} from "./form-control-error";

@Directive()
export abstract class InputComponentBase<T> {

    formControlSignal =
        input.required<FormControl<T>>({alias: 'formControl'});

    appearanceSignal
        = input(<MatFormFieldAppearance>('outline'), { alias: 'appearance',
            transform(value: MatFormFieldAppearance | undefined | null) {
                const defaultVal = <MatFormFieldAppearance>('outline');
                return value || defaultVal;
        }
    });

    labelSignal = input<string>(undefined, {alias: 'label'});

    controlErrorsSignal
        = input(<FormControlError[]>[], { alias: 'controlErrors',
            transform(value: FormControlError[] | undefined | null) {
                return value || <FormControlError[]>[];
        }
    });

    protected hasErrorSignal = signal<boolean>(false);

    protected errorMessageSignal = computed<string>(() => {
        if(this.hasErrorSignal()) {
            for (let val of this.controlErrorsSignal()) {
                let hasError = this.formControlSignal().hasError(val.type);
                console.debug(`type ${val.type}, hasError ${hasError}`);
                if (hasError) {
                    return val.message;
                }
            }
        }
        //TODO: return a default translated message.
        return 'invalid';
    });

    protected onBlur(): void {
        this.hasErrorSignal.set(this.formControlSignal().invalid);
    }
}
