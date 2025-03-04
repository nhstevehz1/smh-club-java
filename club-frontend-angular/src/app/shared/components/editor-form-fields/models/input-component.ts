import {FormControl} from "@angular/forms";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {Directive, input} from "@angular/core";

@Directive()
export abstract class InputComponentBase<T> {
    formControlSignal =
        input.required<FormControl<T>>({alias: 'formControl'});

    appearanceSignal
        = input(<MatFormFieldAppearance>('outline'), {
            alias: 'appearance',
            transform(value: MatFormFieldAppearance | undefined | null) {
                const defaultVal = <MatFormFieldAppearance>('outline');
                return value || defaultVal;
        }
    });

    labelSignal = input<string>(undefined, {alias: 'label'});
}
