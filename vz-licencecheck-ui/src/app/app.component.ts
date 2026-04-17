import {Component, inject, OnInit, ViewEncapsulation} from '@angular/core';
import {VERSION} from '@environments/environment';
import {banner} from '@app/core/utils';
import {AppConfigService} from '@app/core/app-config/app-config.service';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs';
import {Meta, Title} from '@angular/platform-browser';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {NgOptimizedImage} from '@angular/common';
import {ObButtonModule, ObIBanner, ObIconModule, ObMasterLayoutModule, ObPopoverModule} from '@oblique/oblique';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {TimedOut, VerificationStore} from '@app/_services/verification.store';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    TranslateModule,
    MatIconModule,
    MatButtonModule,
    ObButtonModule,
    ObPopoverModule,
    ObIconModule,
    ObMasterLayoutModule,
    NgOptimizedImage,
    MatTooltip
  ]
})
export class AppComponent implements OnInit {
  private readonly appConfigService = inject(AppConfigService);
  private readonly router = inject(Router);
  private readonly store = inject(VerificationStore);
  private readonly titleService = inject(Title);
  private readonly translate = inject(TranslateService);
  private readonly meta = inject(Meta);

  banner: ObIBanner;
  appVersion: any;
  currentYear: number;
  title: string;
  supportItems = [
    {link: 'https://findmind.ch/c/Ce39-nUQL', label: 'i18n.support.feedback'},
    {link: 'https://www.eid.admin.ch/de/help-pilot', label: 'i18n.support.help'},
    {link: 'https://forms.eid.admin.ch/elfa', label: 'i18n.support.contact'},
    {link: 'https://www.eid.admin.ch/de/pilotprojekte', label: 'i18n.support.more-information'}
  ];

  constructor() {
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
    const env = this.appConfigService.appConfig.environment || 'PROD';
    this.banner = banner(env);

    this.meta.addTags([
      {
        name: 'keywords',
        content:
          'LicenceCheck, Licence Check, LicenseCheck, License Check, eLFA prüfen, eLFA verifizieren, elektronischer Lernfahrausweis prüfen, Lernfahrausweis elektronisch prüfen, Lernfahrausweis prüfen'
      }
    ]);
  }

  get helpToolTip() {
    return this.translate.instant('i18n.support.help');
  }

  private updateTitle(url: string): void {
    let titleKey: string;
    switch (url) {
      case '/use-case':
        titleKey = 'i18n.title.proof';
        break;
      case '/scan-qr-code':
        titleKey = 'i18n.title.' + this.store.useCase()?.title;
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
    if (this.store.isValid()) {
      return 'i18n.title.licenceValid';
    } else if (this.store.status() === TimedOut) {
      return 'i18n.title.requestFailed';
    } else if (this.store.isRejected()) {
      return 'i18n.title.requestRejected';
    } else if (this.store.isInvalid()) {
      return 'i18n.title.licenceInvalid';
    } else if (this.store.isPremature()) {
      return 'i18n.title.jwtPremature';
    } else {
      return 'i18n.title.verification';
    }
  }
}
