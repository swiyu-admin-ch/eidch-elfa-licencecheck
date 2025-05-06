import {setLanguage} from '../support/flows/change-language';

const languages = ['fr', 'de', 'en', 'it'];

interface Translation {
  useCase0: string;
  restrictionsA: string;
  invalidMessage: string;
  restrictionsB: string;
  qrCodeTitle: string;
  useCase1: string;
  categoryRestrictions: string;
  timeoutError: string;
  categoryCode: string;
  successMessage: string;
}

const translations: Record<string, Translation> = {
  de: {
    useCase0: 'Gültigkeit überprüfen',
    restrictionsA: 'Zusatzangaben (A)',
    invalidMessage: 'Dieser Lernfahrausweis ist ungültig.',
    restrictionsB: 'Zusatzangaben (B)',
    qrCodeTitle: 'Gültigkeit überprüfen',
    useCase1: 'Alle Daten anfragen',
    categoryRestrictions: 'Zusatzangaben',
    timeoutError:
      'Die Datenübermittlung ist fehlgeschlagen. Der Ausweis konnte nicht überprüft werden. Bitte versuchen Sie es nochmals mit einem neuen QR-Code.',
    categoryCode: 'Kategorie',
    successMessage: 'Gültig'
  },
  fr: {
    useCase0: 'Contrôler la validité',
    restrictionsA: 'Indications complémentaires (A)',
    invalidMessage: 'Ce permis d’élève conducteur n’est pas valable.',
    restrictionsB: 'Indications complémentaires (B)',
    qrCodeTitle: 'Contrôler la validité',
    useCase1: 'Toutes les données demander',
    categoryRestrictions: 'Indications complémentaires',
    timeoutError:
      'La transmission des données a échoué. Le permis n’a pas pu être vérifié. Veuillez réessayer avec un nouveau code QR.',
    categoryCode: 'Catégorie',
    successMessage: 'Valable'
  },
  en: {
    useCase0: 'Verify validity',
    restrictionsA: 'Additional information (A)',
    invalidMessage: 'This learner-driver permit is not valid.',
    restrictionsB: 'Additional information (B)',
    qrCodeTitle: 'Verify validity',
    useCase1: 'All data request',
    categoryRestrictions: 'Additional information',
    timeoutError: 'The data transmission failed. The licence could not be verified. Please try a new QR code.',
    categoryCode: 'Category',
    successMessage: 'Valid'
  },
  it: {
    useCase0: 'Verificare la validità',
    restrictionsA: 'Dati supplementari (A)',
    invalidMessage: 'Questa licenza per allievo conducente non è valida.',
    restrictionsB: 'Dati supplementari (B)',
    qrCodeTitle: 'Verificare la validità',
    useCase1: 'Tutti i dati richiedere',
    categoryRestrictions: 'Dati supplementari',
    timeoutError:
      'La trasmissione dei dati è fallita. Non è stato possibile verificare la licenza. Si prega di riprovare con un nuovo codice QR.',
    categoryCode: 'Categoria',
    successMessage: 'Valida'
  }
};

const runTestsInLanguage = (lang: string) => {
  describe(`Tests in ${lang.toUpperCase()}`, () => {
    describe('QR-Code scanning screen', () => {
      beforeEach(() => {
        // Intercept the GET request to the /elfa-vz-licencecheck/api/v1/verifier/verify endpoint
        cy.intercept('POST', '/api/v1/verification/verify', {
          statusCode: 200,
          fixture: 'verify.json'
        }).as('postVerify');

        cy.intercept('GET', '/api/v1/verification/verify/534a8d81-081f-4f01-9e37-38856c8b06e4', {
          statusCode: 200,
          fixture: 'pollingResult-pending.json'
        }).as('polling');

        cy.setCookie('policyConfirmed', 'true');
        cy.visit('/');

        cy.url().should('include', '/use-case');
        setLanguage(lang);
      });

      it('displays QR-Code scanning screen', () => {
        cy.get('#use-cases').find('#use-case-0').find('.ob-button').click();

        cy.get('.qr-centered-component').find('.component-title').should('contain', translations[lang].qrCodeTitle);
      });
    });

    describe('Verification Result screen', () => {
      beforeEach(() => {
        // Intercept common API calls
        cy.intercept('GET', '/api/v1/verification/use-cases', {
          statusCode: 200,
          fixture: 'use-cases.json'
        }).as('getUseCases');

        cy.intercept('POST', '/api/v1/verification/verify', {
          statusCode: 200,
          fixture: 'verify.json'
        }).as('postVerify');

        let interceptCount = 0;

        cy.intercept('GET', '/api/v1/verification/verify/534a8d81-081f-4f01-9e37-38856c8b06e4', req => {
          req.reply(res => {
            if (interceptCount === 0) {
              interceptCount += 1;
              res.send({
                statusCode: 200,
                fixture: 'pollingResult-pending.json'
              });
            } else {
              res.send({
                statusCode: 200,
                fixture: 'pollingResult-success.json'
              });
            }
          });
        }).as('polling');

        cy.setCookie('policyConfirmed', 'true');
        cy.clock(null, ['setTimeout', 'clearTimeout']);
        cy.visit('/');
        setLanguage(lang);
      });

      it('success should display minimal valid vc', () => {
        // Ensure the #use-cases element exists and is ready
        cy.get('#use-cases', {timeout: 10000}).should('exist');

        // Intercept the polling request
        cy.intercept('GET', '/api/v1/verification/verify/534a8d81-081f-4f01-9e37-38856c8b06e4', {
          statusCode: 200,
          fixture: 'pollingResult-success.json'
        }).as('polling');

        // Simulate user action
        cy.get('#use-cases').find('#use-case-0').find('.ob-button').click();

        // Wait for verification to succeed
        cy.wait('@postVerify').its('response.statusCode').should('eq', 200);
        cy.wait('@polling').its('response.statusCode').should('eq', 200);

        // Validate UI updates
        cy.get('#photo-image', {timeout: 1000}).should('have.attr', 'src').should('not.be.empty');
        cy.get('#status')
          .find('.mat-icon')
          .should('have.attr', 'data-mat-icon-name')
          .should('include', 'checkmark-circle');
        cy.get('#categoryCode-value', {timeout: 10000}).should('contain', 'B');
      });

      it('success should display valid vc', () => {
        // Intercept the GET request for polling
        cy.intercept('GET', '/api/v1/verification/verify/534a8d81-081f-4f01-9e37-38856c8b06e4', {
          statusCode: 200,
          fixture: 'pollingResult-success.json'
        }).as('polling');

        // Wait for use cases to load and click on the second use case
        cy.get('#use-cases').find('#use-case-1').find('.ob-button').click();

        // Wait for POST verification request and validate response
        cy.wait('@postVerify').its('response.statusCode').should('eq', 200);

        // Wait for polling request and validate response
        cy.wait('@polling').its('response.statusCode').should('eq', 200);

        // Validate the success message
        cy.get('#status-success-message', {timeout: 10000}).should('contain', translations[lang].successMessage);

        // Validate that the photo image has a non-empty source
        cy.get('#photo-image').should('have.attr', 'src').should('not.be.empty');

        // Validate the status icon and ensure it includes the checkmark-circle
        cy.get('#status')
          .find('.mat-icon')
          .should('have.attr', 'data-mat-icon-name')
          .should('include', 'checkmark-circle');

        // Validate category code contains expected values
        cy.get('#categoryCode').should('contain', translations[lang].categoryCode).and('contain', 'B');

        // Validate category restrictions contain the correct text
        cy.get('#categoryRestrictions')
          .should('contain', translations[lang].categoryRestrictions)
          .and('contain', '10.01 Handschaltung');

        // Validate additional restriction labels and values
        cy.get('#restrictionsA-label').should('contain', translations[lang].restrictionsA);
        cy.get('#restrictionsA-value').should(
          'contain',
          '05.05 Fahren nur mit Beifahrer, der im Besitz eines Führerausweises sein muss'
        );
        cy.get('#restrictionsB-label').should('contain', translations[lang].restrictionsB);
        cy.get('#restrictionsB-value').should(
          'contain',
          '05.05 Fahren nur mit Beifahrer, der im Besitz eines Führerausweises sein muss'
        );
      });

      it('displays verification result screen', () => {
        cy.get('#use-cases').find('#use-case-0').find('.ob-button').click();

        cy.wait('@polling');
        cy.tick(4000);
        cy.wait('@polling');
        cy.get('#status-success-message').should('contain', translations[lang].successMessage);
      });

      it('displays invalid VC message', () => {
        cy.intercept('GET', '/api/v1/verification/verify/534a8d81-081f-4f01-9e37-38856c8b06e4', {
          statusCode: 200,
          fixture: 'pollingResult-success-invalid.json'
        }).as('pollingInvalid');

        cy.get('#use-cases').find('#use-case-0').find('.ob-button').click();

        cy.wait('@pollingInvalid');
        cy.get('.status-icon').find('.mat-icon').should('have.attr', 'data-mat-icon-name').should('include', 'ban');
        cy.get('.error-text').should('contain', translations[lang].invalidMessage);
      });

      it('displays timeout error', () => {
        cy.intercept('GET', '/api/v1/verification/verify/534a8d81-081f-4f01-9e37-38856c8b06e4', {
          statusCode: 200,
          fixture: 'pollingResult-pending.json'
        }).as('pollinga');
        cy.get('#use-cases').find('#use-case-0').find('.ob-button').click();
        cy.wait('@pollinga');
        cy.tick(900000);
        cy.get('.status-icon-error')
          .find('.mat-icon')
          .should('have.attr', 'data-mat-icon-name')
          .should('include', 'warning-triangle');
        cy.get('.error-text').should('contain', translations[lang].timeoutError);
      });
    });
  });
};

languages.forEach(lang => {
  runTestsInLanguage(lang);
});
