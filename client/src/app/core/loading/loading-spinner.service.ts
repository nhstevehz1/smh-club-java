import {inject, Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "./loading-spinner/loading-spinner.component";

@Injectable({
  providedIn: 'root'
})
export class LoadingSpinnerService {
  private dialog = inject(MatDialog);
  private dialogRef: MatDialogRef<LoadingSpinnerComponent> | null = null;
  private isLoading = false;
  private loadingMap = new Map<string, boolean>();

  setLoading(loading: boolean, url: string): void {
    if (!url) {
      return;
    }

    if (loading) {
      this.loadingMap.set(url, loading);
      if(!this.isLoading){
        this.isLoading = true;
        this.dialogRef = this.start();
      }
    }else if (!loading && this.loadingMap.has(url)) {
      this.loadingMap.delete(url);
    }
    if (this.loadingMap.size === 0) {
      if (this.dialogRef) {
        this.dialogRef.close();
        this.dialogRef = null;
      }
    }
  }

  private start(): MatDialogRef<LoadingSpinnerComponent> {
    return this.dialog.open(LoadingSpinnerComponent, {
      disableClose: true
    });
  }
}
