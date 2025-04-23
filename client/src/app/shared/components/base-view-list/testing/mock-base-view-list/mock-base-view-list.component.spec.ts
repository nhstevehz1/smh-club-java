import {ComponentFixture, TestBed, fakeAsync, tick} from '@angular/core/testing';

import {MockBaseViewListComponent} from './mock-base-view-list.component';
import {
  MockViewListApiService,
  MockViewListDialogService,
  BaseViewListTest,
  BaseViewListModel
} from '@app/shared/components/base-view-list/testing/test-support';
import {EditAction, EditDialogResult} from '@app/shared/components/base-edit-dialog/models';
import {asyncData} from '@app/shared/testing';
import {EMPTY} from 'rxjs';

describe('BaseViewList', () => {
  let component: MockBaseViewListComponent;
  let fixture: ComponentFixture<MockBaseViewListComponent>;

  let apiSvcMock: jasmine.SpyObj<MockViewListApiService>;
  let dlgSvcMock: jasmine.SpyObj<MockViewListDialogService>

  let result: EditDialogResult<BaseViewListModel>;
  let context: BaseViewListModel;

  beforeEach(async () => {
    apiSvcMock = jasmine.createSpyObj('MockViewListApiService', ['create', 'update', 'delete']);
    dlgSvcMock = jasmine.createSpyObj('MockViewListDialogService', ['openDialog', 'generateDialogInput']);

    await TestBed.configureTestingModule({
      imports: [MockBaseViewListComponent],
      providers: [
        {provide: MockViewListDialogService, useValue: {}},
        {provide: MockViewListApiService, useValue: {}}
      ]
    }).overrideProvider(MockViewListApiService, {useValue: apiSvcMock})
      .overrideProvider(MockViewListDialogService, {useValue: dlgSvcMock})
    .compileComponents();

    fixture = TestBed.createComponent(MockBaseViewListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('test generateDialogInput', () => {
    beforeEach(() => {
      result = BaseViewListTest.generateDialogResult();
      dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      context = BaseViewListTest.generateModel(1);
    });

    it('should call with EditAction.Create', () => {
      const spy = dlgSvcMock.generateDialogInput.and.stub();
      component.processAction('title', context, EditAction.Create);

      expect(spy).toHaveBeenCalledWith('title', context, EditAction.Create);
    });

    it('should call with EditAction.Edit', () => {
      const spy = dlgSvcMock.generateDialogInput.and.stub();
      component.processAction('title', context, EditAction.Edit);

      expect(spy).toHaveBeenCalledWith('title', context, EditAction.Edit);
    });

    it('should call with EditAction.Delete', () => {
      const spy = dlgSvcMock.generateDialogInput.and.stub();
      component.processAction('title', context, EditAction.Delete);

      expect(spy).toHaveBeenCalledWith('title', context, EditAction.Delete);
    });


  });

  describe('test openDialog', () => {
    beforeEach(() => {
      result = BaseViewListTest.generateDialogResult();
      result.action = EditAction.Cancel;
      context = BaseViewListTest.generateModel(1);
      dlgSvcMock.generateDialogInput.and.stub();
    });

    it('should call openDialog when action is create', () => {
      const spy = dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      component.processAction('title', context, EditAction.Create);

      expect(spy).toHaveBeenCalled();
    });

    it('should call openDialog when action is edit', () => {
      const spy = dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      component.processAction('title', context, EditAction.Edit);

      expect(spy).toHaveBeenCalled();
    });

    it('should call openDialog when action is delete', () => {
      const spy = dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      component.processAction('title', context, EditAction.Delete);

      expect(spy).toHaveBeenCalled();
    });
  });

  describe('api interaction tests', () => {
    beforeEach(() => {
      result = BaseViewListTest.generateDialogResult();
      context = BaseViewListTest.generateModel(1);
      dlgSvcMock.generateDialogInput.and.stub();
    });

    it('should call create when edit action is create', fakeAsync(() => {
      result.action = EditAction.Create;
      dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      const spy = apiSvcMock.create.and.returnValue(asyncData(context));

      component.processAction('title', context, EditAction.Create);
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should call update when edit action is edit', fakeAsync(() => {
      result.action = EditAction.Edit;
      dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      const spy = apiSvcMock.update.and.returnValue(asyncData(context));

      component.processAction('title', context, EditAction.Edit);
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should call delete when edit action is delete', fakeAsync(() => {
      result.action = EditAction.Delete;
      dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      const spy = apiSvcMock.delete.and.returnValue(EMPTY);

      component.processAction('title', context, EditAction.Delete);
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should NOT call create when edit action is cancel', fakeAsync(() => {
      result.action = EditAction.Cancel;
      dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      const spy = apiSvcMock.create.and.returnValue(asyncData(context));

      component.processAction('title', context, EditAction.Create);
      tick();

      expect(spy).not.toHaveBeenCalled();
    }));

    it('should NOT call update when edit action is cancel', fakeAsync(() => {
      result.action = EditAction.Cancel;
      dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      const spy = apiSvcMock.update.and.returnValue(asyncData(context));

      component.processAction('title', context, EditAction.Edit);
      tick();

      expect(spy).not.toHaveBeenCalled();
    }));

    it('should call NOT call delete when edit action is cancel', fakeAsync(() => {
      result.action = EditAction.Cancel;
      dlgSvcMock.openDialog.and.returnValue(asyncData(result));
      const spy = apiSvcMock.delete.and.returnValue(EMPTY);

      component.processAction('title', context, EditAction.Delete);
      tick();

      expect(spy).not.toHaveBeenCalled();
    }));
  });
});
