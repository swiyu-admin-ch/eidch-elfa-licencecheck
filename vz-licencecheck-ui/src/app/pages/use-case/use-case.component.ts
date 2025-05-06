import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {UseCaseService} from '@app/_services';
import {Router} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {InfoDialogComponent} from '@app/pages/use-case/info-dialog/info-dialog.component';
import {UseCase, VerifierApi} from '@app/core/api/generated';
import {UntilDestroy, untilDestroyed} from '@ngneat/until-destroy';

@UntilDestroy()
@Component({
  selector: 'app-use-case',
  templateUrl: './use-case.component.html',
  styleUrls: ['./use-case.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UseCaseComponent implements OnInit {
  useCases: UseCase[];

  constructor(
    private readonly useCaseService: UseCaseService,
    private readonly router: Router,
    private readonly verifierApi: VerifierApi,
    private readonly dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.verifierApi
      .getUseCases()
      .pipe(untilDestroyed(this))
      .subscribe(useCases => {
        useCases.sort((a, b) => a.order - b.order);
        this.useCases = useCases;
      });
  }

  createVerificationRequest(useCase: UseCase): void {
    this.useCaseService.setUseCase(useCase);
    this.router.navigate(['/scan-qr-code']);
  }

  openInfoDialog(useCase: UseCase) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.maxWidth = window.innerWidth <= 600 ? '90vw' : '35vw';
    dialogConfig.maxHeight = '90vh';
    dialogConfig.autoFocus = false;
    dialogConfig.data = {item: useCase};

    this.dialog.open(InfoDialogComponent, dialogConfig);
  }
}
