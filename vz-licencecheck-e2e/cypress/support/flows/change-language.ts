// function to ensure the correct language is selected
export function setLanguage(lang: string) {
  cy.get('#ob-language-dropdown')
    .invoke('text')
    .then(() => {
      cy.get('#ob-language-dropdown').click();
      cy.get('#ob-language-' + lang + '-option').click();
    });
}