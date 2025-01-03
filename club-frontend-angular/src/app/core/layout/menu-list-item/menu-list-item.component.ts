import {Component, HostBinding, Input} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {NavItem} from "./nav-item";
import {Router} from "@angular/router";
import {MatListItem} from "@angular/material/list";
import {MatIcon} from "@angular/material/icon";
import {NgClass, NgForOf, NgIf, NgStyle} from "@angular/common";
import {NavService} from "../services/nav.service";

@Component({
  selector: 'app-menu-list-item',
  imports: [
    MatListItem,
    MatIcon,
    NgStyle,
    NgClass,
    NgForOf,
    NgIf
  ],
  templateUrl: './menu-list-item.component.html',
  styleUrl: './menu-list-item.component.scss',
  animations: [
    trigger('indicatorRotate', [
      state('collapsed', style({transform: 'rotate(0deg)'})),
      state('expanded', style({transform: 'rotate(180deg)'})),
      transition('expanded <=> collapsed',
          animate('225ms cubic-bezier(0.4,0.0,0.2,1)')
      ),
    ])
  ]
})
export class MenuListItemComponent {

  expanded: boolean = false;
  @HostBinding('attr.aria-expanded') ariaExpanded = this.expanded;
  @Input() item: NavItem = NavItem.Empty()!;
  @Input() depth: number = 0;

  get displayName(): string {
    return this.item!.displayName;
  }

  get iconName(): string {
    return this.item!.iconName;
  }

  get routeName(): string {
    return this.item!.route;
  }

  get hasChildren(): boolean {
    return this.item!.children.length > 0;
  }

  get children(): NavItem[] {
    return this.item!.children;
  }

  constructor(public router: Router,
              private navService: NavService) {}

  onItemSelected() {
    if (this.item.children.length == 0) {
      this.router.navigate([this.item.route]).then(r => {
        if (r) {
          this.navService.closeNav();
        }
      });
    } else {
      this.expanded = !this.expanded;
    }
  }
}
