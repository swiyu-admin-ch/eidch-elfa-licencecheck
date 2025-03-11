import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UseCaseResponse} from '@app/core/api/generated/model/use-case-response';

@Component({
  selector: 'app-info-dialog',
  templateUrl: './info-dialog.component.html',
  styleUrls: ['./info-dialog.component.scss']
})
export class InfoDialogComponent implements OnInit {
  item: UseCaseResponse;

  constructor(public dialogRef: MatDialogRef<InfoDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: {item: any}) {}

  ngOnInit(): void {
    this.item = this.data.item;
  }

  close() {
    this.dialogRef.close();
  }
}
