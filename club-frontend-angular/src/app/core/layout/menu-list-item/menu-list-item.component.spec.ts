import {MenuListItemComponent} from './menu-list-item.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";

describe('MenuListItemComponent', () => {
  let fixture: ComponentFixture<MenuListItemComponent>;
  let component: MenuListItemComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        MenuListItemComponent
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(MenuListItemComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
