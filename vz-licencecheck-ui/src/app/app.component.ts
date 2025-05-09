import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ObIBanner} from '@oblique/oblique/lib/utilities.model';
import {VERSION} from '@environments/environment';
import {banner} from '@app/core/utils';
import {AppConfigService} from '@app/core/app-config/app-config.service';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs';
import {UseCaseService} from '@app/_services';
import {Meta, Title} from '@angular/platform-browser';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {ObButtonModule, ObIconModule, ObMasterLayoutModule, ObPopoverModule} from '@oblique/oblique';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatIconModule,
    MatButtonModule,
    ObButtonModule,
    ObPopoverModule,
    ObIconModule,
    ObMasterLayoutModule,
    NgOptimizedImage
  ]
})
export class AppComponent implements OnInit {
  banner: ObIBanner;
  appVersion: any;
  currentYear: number;
  title: string;
  supportItems = [
    {link: 'https://findmind.ch/c/Ce39-nUQL', label: 'i18n.support.feedback'},
    {link: 'https://www.eid.admin.ch/de/hilfe-support', label: 'i18n.support.help'},
    {link: 'https://forms.eid.admin.ch/elfa', label: 'i18n.support.contact'},
    {link: 'https://www.eid.admin.ch/de/pilotprojekte', label: 'i18n.support.more-information'}
  ];

  constructor(
    private readonly appConfigService: AppConfigService,
    private readonly router: Router,
    public useCaseService: UseCaseService,
    private readonly titleService: Title,
    private readonly translate: TranslateService,
    private readonly meta: Meta
  ) {
    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.updateTitle(event.urlAfterRedirects);
      });
  }

  ngOnInit(): void {
    this.updateTitle(this.router.url);
    this.appVersion = VERSION;
    this.currentYear = new Date().getFullYear();
    this.appConfigService.loadAppConfig().subscribe(({environment}) => {
      const env = environment || 'PROD';
      this.banner = banner(env);
    });

    this.meta.addTags([
      {
        name: 'keywords',
        content:
          'LicenceCheck, Licence Check, LicenseCheck, License Check, eLFA prüfen, eLFA verifizieren, elektronischer Lernfahrausweis prüfen, Lernfahrausweis elektronisch prüfen, Lernfahrausweis prüfen'
      }
    ]);
  }

  private updateTitle(url: string): void {
    let titleKey: string;
    switch (url) {
      case '/use-case':
        titleKey = 'i18n.title.proof';
        break;
      case '/scan-qr-code':
        titleKey = 'i18n.title.' + this.useCaseService.getUseCase()?.title;
        break;
      case '/verification-result':
        titleKey = this.getVerificationResultTitle();
        break;
      default:
        titleKey = 'i18n.title.home';
        break;
    }
    this.translate.get([titleKey, 'i18n.title']).subscribe(translate => {
      const translatedTitle = translate[titleKey];
      const appTitle = translate['i18n.title'];
      const fullTitle = `${translatedTitle} | ${appTitle}`;
      this.titleService.setTitle(fullTitle);
    });
  }

  private getVerificationResultTitle(): string {
    if (this.useCaseService.isVcValid()) {
      return 'i18n.title.licenceValid';
    } else if (this.useCaseService.isTimeout()) {
      return 'i18n.title.requestFailed';
    } else if (this.useCaseService.isRejected()) {
      return 'i18n.title.requestRejected';
    } else if (this.useCaseService.isVcInvalid()) {
      return 'i18n.title.licenceInvalid';
    } else {
      return 'i18n.title.verification';
    }
  }
}
