import {Component, inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {UseCase} from '@app/core/api/generated';

import {TranslateModule} from '@ngx-translate/core';
import {MatButtonModule} from '@angular/material/button';
import {ObButtonModule} from '@oblique/oblique';

@Component({
  selector: 'app-info-dialog',
  templateUrl: './info-dialog.component.html',
  styleUrls: ['./info-dialog.component.scss'],
  imports: [TranslateModule, MatDialogModule, MatButtonModule, ObButtonModule]
})
export class InfoDialogComponent implements OnInit {
  dialogRef = inject<MatDialogRef<InfoDialogComponent>>(MatDialogRef);
  data = inject<{
    item: any;
  }>(MAT_DIALOG_DATA);

  item: UseCase;

  ngOnInit(): void {
    this.item = this.data.item;
  }

  close() {
    this.dialogRef.close();
  }
}
