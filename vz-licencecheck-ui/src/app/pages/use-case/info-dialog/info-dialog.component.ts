import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {UseCase} from '@app/core/api/generated';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatButtonModule} from '@angular/material/button';
import {ObButtonModule} from '@oblique/oblique';

@Component({
  selector: 'app-info-dialog',
  templateUrl: './info-dialog.component.html',
  styleUrls: ['./info-dialog.component.scss'],
  standalone: true,
  imports: [CommonModule, TranslateModule, MatDialogModule, MatButtonModule, ObButtonModule]
})
export class InfoDialogComponent implements OnInit {
  item: UseCase;

  constructor(public dialogRef: MatDialogRef<InfoDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: {item: any}) {}

  ngOnInit(): void {
    this.item = this.data.item;
  }

  close() {
    this.dialogRef.close();
  }
}
