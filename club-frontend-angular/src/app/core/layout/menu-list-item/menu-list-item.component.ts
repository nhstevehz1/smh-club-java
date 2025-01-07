import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NavItem} from "./nav-item";
import {MatListItem} from "@angular/material/list";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-menu-list-item',
  imports: [
    MatListItem,
    MatIcon
  ],
  templateUrl: './menu-list-item.component.html',
  styleUrl: './menu-list-item.component.scss',
})
export class MenuListItemComponent {
  @Input({required: true}) item: NavItem | undefined;
  @Output() menuClicked = new EventEmitter();

  get displayName(): string {
    return this.item!.displayName;
  }

  get iconName(): string {
    return this.item!.iconName;
  }

  get routeName(): string {
    return this.item!.route;
  }

  onItemSelected() {
    this.menuClicked.emit();
  }
}
