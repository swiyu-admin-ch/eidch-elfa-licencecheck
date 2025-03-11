import {setLanguage} from '../support/flows/change-language';

describe('The Home Page', () => {
  beforeEach(() => {
    cy.visit('/');
    setLanguage('de');
  });

  it('successfully loads', () => {
    cy.url().should('include', '/');
  });

  it('displays correct elements', () => {
    cy.visit('/');
    cy.get('ob-master-layout-navigation').should('exist');
    cy.get("img[src='assets/images/welcome.svg']").should('exist');
    cy.get('.title').should('contain', 'Gültigkeit eines elektronischen Lernfahrausweises überprüfen');
    cy.get('.description').should(
      'contain',
      'Elektronische Lernfahrausweise sind ein Pilotprojekt vom Bund.'
    );
  });

  it('shows error message when policy is not confirmed and button is clicked', () => {
    cy.get('#close-button').click();
    cy.get('.message')
      .should('exist')
      .and(
        'contain',
        'Bitte lesen Sie die Datenschutzerklärung und Nutzungsbedingungen und akzeptieren diese wenn Sie damit einverstanden sind.'
      );
  });

  it('navigates to use-case page when policy is confirmed and button is clicked', () => {
    cy.get('mat-checkbox').click();
    cy.get('#close-button').click();
    cy.url().should('include', '/use-case');
  });

  it('checks policy and navigates to nachweis überprüfen page', () => {
    // Click the policy checkbox
    cy.get('mat-checkbox').click();

    // Click the "Nachweis überprüfen" button
    cy.get('#close-button').click();

    // Confirm navigation to the use-case page
    cy.url().should('include', '/use-case');

    // Add assertions to verify that the use-case page is displayed correctly
    cy.get('#use-cases').should('exist');
  });
});
