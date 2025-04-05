import {EditDialogInput, EditAction, EditDialogResult} from '@app/shared/components/edit-dialog/models';
import {EditDialogModel, DialogTest} from '@app/shared/components/edit-dialog/testing/test-support';
import {
  MockDialogEditorComponent
} from '@app/shared/components/edit-dialog/testing/mock-dialog-editor/mock-dialog-editor.component';
import {TestBed, ComponentFixture} from '@angular/core/testing';
import {MockDialogComponent} from '@app/shared/components/edit-dialog/testing/mock-dialog/mock-dialog.component';
import {MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {TranslateModule} from '@ngx-translate/core';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {By} from '@angular/platform-browser';
import {MatButtonHarness} from '@angular/material/button/testing';

describe('DialogComponent', () => {
  let component: MockDialogComponent;
  let fixture: ComponentFixture<MockDialogComponent>;
  let dialogRef: MatDialogRef<MockDialogComponent>;
  let loader: HarnessLoader;

  let dialogInput: EditDialogInput<EditDialogModel, MockDialogEditorComponent>;
  let dialogResult: EditDialogResult<EditDialogModel>;
  const model = DialogTest.generateEditDialogModel();

  beforeEach(async () => {
    dialogInput = DialogTest.generatedEditDialogInput(model, EditAction.Create);
    dialogResult = {context: dialogInput.context, action: dialogInput.action};

    await TestBed.configureTestingModule({
      imports: [
        MockDialogComponent,
        MockDialogEditorComponent,
        MatDialogModule,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations(),
        {
          provide: MatDialogRef,
          useValue: {
            close: () => dialogResult
          }
        },
        {provide: MAT_DIALOG_DATA, useValue: dialogInput}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MockDialogComponent);
    component = fixture.componentInstance;
    dialogRef = TestBed.inject(MatDialogRef);

    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  describe('test component', () => {
    it('should create edit component', async () => {
      fixture.detectChanges();
      await fixture.whenStable();
      expect(component).toBeTruthy();
      expect(dialogRef).toBeTruthy();
    });

    it('should contain a title', () => {
      expect(component.title).toBeTruthy();
    });

    it('should contain correct title',  () => {
      const title = 'MyMockTestTitle';
      component.dialogInput.update((input) => {
        input.title = title; return input;});
      expect(component.title()).toBe(title);
    });

    it('should contain an edit form', () => {
      expect(component.editForm).toBeTruthy();
    });

    it('should contain an editorConfig', () => {
      expect(component.editorConfig).toBeTruthy();
    });

    it('should contain isDeleteAction', () => {
      expect(component.isDeleteAction).toBeTruthy()
    });

    it('should contain the correct editor template', async () => {
      fixture.detectChanges();
      await fixture.whenStable();
      const element = fixture.debugElement.query(By.css('app-mock-dialog-editor'));
      expect(element).toBeTruthy();
    });
  });

  describe('test EditAction.create', () => {
    beforeEach(() => {
      component.dialogInput.update((input) => {
        input.action = EditAction.Create;
        return input;
      });
    });


    it('form should be visible when action is create', async () => {
      fixture.detectChanges();
      await fixture.whenStable();

      const form = fixture.debugElement.query(By.css('form'));
      expect(form).toBeTruthy();
    });

    it('form should NOT be set to context', async() => {
      const value = component.editForm().value as EditDialogModel;
      expect(value).not.toEqual(dialogInput.context);
    });

    it('form should be set to original values', () => {
      const value = component.editForm().value as EditDialogModel;
      expect(value).toEqual(dialogInput.editorConfig.form.value as EditDialogModel);
    });

    it('should contain two buttons when action is create', async () => {
      const harnesses = await loader.getAllHarnesses(MatButtonHarness);
      expect(harnesses.length).toEqual(2);
    });

    it('should contain cancel button when action is create', async () => {
      const harness = await loader.getHarnessOrNull(MatButtonHarness.
            with({text: 'cancel', variant: 'basic'}));

      expect(harness).toBeTruthy();
    });

    it('should contain save button when action is create', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({text: 'save', variant: 'basic'}));
      expect(harness).toBeTruthy();
    });

    it('should NOT contain delete button action is create', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({text: 'delete', variant: 'basic'}));
      expect(harness).toBeFalsy();
    });

    it('save button should be disabled on init when action is create', async () => {
      const harness =
        await loader.getHarness(MatButtonHarness.with({text: 'save', variant: 'basic'}));
      const disabled = await harness.isDisabled();
      expect(disabled).toBeTrue();
    });

    it('cancel click should close the dialog when action is create', async () => {
      const spy = spyOn(dialogRef, 'close').and.callThrough();
      const harness = await loader.getHarness(MatButtonHarness.
      with({text: 'cancel', variant: 'basic'}));

      await harness.click();
      expect(spy).toHaveBeenCalled();
    });

    it('save click should close the dialog when action is create', async () => {
      // button is disabled until touched and valid.
      component.editForm().markAsTouched();

      const spy = spyOn(dialogRef, 'close').and.callThrough();
      const harness = await loader.getHarness(MatButtonHarness.
      with({text: 'save', variant: 'basic'}));
      await harness.click();

      expect(spy).toHaveBeenCalled();
    });
  });

  describe('test EditAction.edit', () => {
    beforeEach(() => {
      component.dialogInput.update((input) => {
        input.action = EditAction.Edit;
        return input;
      });
    });

    it('form should be visible when action is edit', async () => {
      fixture.detectChanges();
      await fixture.whenStable();

      const form = fixture.debugElement.query(By.css('form'));
      expect(form).toBeTruthy();
    });

    it('form should be set to the context', () => {
      const value = component.editForm().value as EditDialogModel;
      expect(value).toEqual(dialogInput.context);
    });

    it('should contain two buttons when action is edit', async () => {
      const harnesses = await loader.getAllHarnesses(MatButtonHarness);
      expect(harnesses.length).toEqual(2);
    });

    it('should contain cancel button when action is edit', async () => {
      const harness = await loader.getHarnessOrNull(MatButtonHarness.
      with({text: 'cancel', variant: 'basic'}));
      expect(harness).toBeTruthy();
    });

    it('should contain save button when action is edit', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({text: 'save', variant: 'basic'}));
      expect(harness).toBeTruthy();
    });

    it('should NOT contain delete button action is edit', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({text: 'delete', variant: 'basic'}));
      expect(harness).toBeFalsy();
    });

    it('save button should be disabled when form is untouched', async () => {
      const harness =
        await loader.getHarness(MatButtonHarness.with({text: 'save', variant: 'basic'}));
      const disabled = await harness.isDisabled();
      expect(disabled).toBeTrue();
    });

    it('cancel click should close the dialog when action is edit', async () => {
      const spy = spyOn(dialogRef, 'close').and.callThrough();
      const harness = await loader.getHarness(MatButtonHarness.
      with({text: 'cancel', variant: 'basic'}));

      await harness.click();
      expect(spy).toHaveBeenCalled();
    });

    it('save click should close the dialog when action is edit', async () => {
      // button is disabled until touched and valid.
      component.editForm().markAsTouched();

      const spy = spyOn(dialogRef, 'close').and.callThrough();
      const harness = await loader.getHarness(MatButtonHarness.
      with({text: 'save', variant: 'basic'}));
      await harness.click();

      expect(spy).toHaveBeenCalled();
    });
  });

  describe ('test EditAction.delete', () => {
    beforeEach(() => {
      component.dialogInput.update((input) => {
        input.action = EditAction.Delete;
        return input;
      });
    });

    it('form should not be visible when action is delete', async () => {
      fixture.detectChanges();
      await fixture.whenStable();

      const form = fixture.debugElement.query(By.css('form'));
      expect(form).toBeFalsy();
    });

    it('should contain two buttons when action is delete', async () => {
      const harnesses = await loader.getAllHarnesses(MatButtonHarness);
      expect(harnesses.length).toEqual(2);
    });

    it('should contain cancel button when action is delete', async () => {
      const harness = await loader.getHarnessOrNull(MatButtonHarness.
      with({text: 'cancel', variant: 'basic'}));
      expect(harness).toBeTruthy();
    });

    it('should contain ok button when action is delete', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({text: 'ok', variant: 'basic'}));
      expect(harness).toBeTruthy();
    });

    it('should NOT contain save button action is edit', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({text: 'save', variant: 'basic'}));
      expect(harness).toBeFalsy();
    });


    it('cancel click should close the dialog when action is delete', async () => {
      const spy = spyOn(dialogRef, 'close').and.callThrough();
      const harness = await loader.getHarness(MatButtonHarness.
          with({text: 'cancel', variant: 'basic'}));

      await harness.click();
      expect(spy).toHaveBeenCalled();
    });


    it('ok click should close the dialog when action is delete', async () => {
      const spy = spyOn(dialogRef, 'close').and.callThrough();
      const harness = await loader.getHarness(MatButtonHarness.
      with({text: 'ok', variant: 'basic'}));

      await harness.click();
      expect(spy).toHaveBeenCalled();
    });
  });
});
