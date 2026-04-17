# Changelog

## [2.0.0]
- [EIDRAEDA-1513](https://jira.bit.admin.ch/browse/EIDRAEDA-1513): Adjust LC so that mDLs can be verified
- [EIDRAEDA-1433](https://jira.bit.admin.ch/browse/EIDRAEDA-1433): Integrate Keycloak - Verifier Service

## [1.13.0]
- [EIDRAEDA-1395](https://jira.bit.admin.ch/browse/EIDRAEDA-1395): Improved error handling and logging from verifier-service
- [EIDRAEDA-1464](https://jira.bit.admin.ch/browse/EIDRAEDA-1464): Fixed handling of new date format in VC verification for expiration dates
- fix: fixed browser refresh on QR-Code page leading to inconsistent state
- [EIDRAEDA-1160](https://jira.bit.admin.ch/browse/EIDRAEDA-1160): Update to Angular 19 and Oblique 13 and other UI dependencies
- [EIDRAEDA-1160](https://jira.bit.admin.ch/browse/EIDRAEDA-1160): Upgrade and cleanup of java dependencies
- [EIDRAEDA-1480](https://jira.bit.admin.ch/browse/EIDRAEDA-1480): Migration Retrofit nach Spring, E2E Trace Context, Http Logging.

## [1.12.0]
- [EIDRAEDA-1366](https://jira.bit.admin.ch/browse/EIDRAEDA-1366): Transition omni issuer/verifier components to single issuer/verifier
- [EIDRAEDA-1318](https://jira.bit.admin.ch/browse/EUDRAEDA-1318): VCs that are only valid in the future get verified appropriately
- [EIDRAEDA-1421](https://jira.bit.admin.ch/browse/EIDRAEDA-1421): Swapped incorrect support link in menu navigation

## [1.11.0]
- [EIDRAEDA-EIDRAEDA-1391](https://jira.bit.admin.ch/browse/EIDRAEDA-EIDRAEDA-1391): Refactor & improve Logging
- [EIDRAEDA-1226](https://jira.bit.admin.ch/browse/EIDRAEDA-1226): Reorganize package structure according to other vz services

## [1.10.0]
- [EIDRAEDA-1303](https://jira.bit.admin.ch/browse/EIDRAEDA-1303): Extended verification request with allowed-issuer-did property

## [1.9.1]
- fix: Translation typos and corrections

## [1.9.0]
- [EIDRAEDA-1227](https://jira.bit.admin.ch/browse/EIDRAEDA-1227): Refactoring to use standalone feature in Angular UI

## [1.8.0]
- Remove unused dependency management section
- [EIDRAEDA-1110](https://jira.bit.admin.ch/browse/EIDRAEDA-1110): Added correct VC attribute translations
- [EIDRAEDA-1188](https://jira.bit.admin.ch/browse/EIDRAEDA-1188): Multiple technical debts addressed and adding jEAP
  lib

## [1.7.0]
- [EIDRAEDA-600](https://jira.bit.admin.ch/browse/EIDRAEDA-600) UI API Code generation with openapi-code-generator
- [EIDRAEDA-535](https://jira.bit.admin.ch/browse/EIDRAEDA-535): Implement translations into LicenceCheck
- [EIDRAEDA-538](https://jira.bit.admin.ch/browse/EIDRAEDA-538): Adapt code to new Verifier-API
- [EIDRAEDA-880](https://jira.bit.admin.ch/browse/EIDRAEDA-880): VC Verification Rejection handled correctly
- [EIDRAEDA-950](https://jira.bit.admin.ch/browse/EIDRAEDA-950): Feedback Screens adjusted to correct validator error codes
- [EIDRAEDA-938](https://jira.bit.admin.ch/browse/EIDRAEDA-938): prepare licenceCheck for abn and prd
- [EIDRAEDA-962](https://jira.bit.admin.ch/browse/EIDRAEDA-962): update PresentationDefinitionDto
- [EIDRAEDA-938](https://jira.bit.admin.ch/browse/EIDRAEDA-938): Initial Preparation for ABN deployment
- [EIDRAEDA-1031](https://jira.bit.admin.ch/browse/EIDRAEDA-1031): Bug: Rejecting shows 2 error screens
- [EIDRAEDA-935](https://jira.bit.admin.ch/browse/EIDRAEDA-935): Display category icon for vc validation
- [EIDRAEDA-1039](https://jira.bit.admin.ch/browse/EIDRAEDA-1039): Remove Registration ID from UseCases
- [EIDRAEDA-1010](https://jira.bit.admin.ch/browse/EIDRAEDA-1010): Remove Swagger and SourceMaps for PROD
- [EIDRAEDA-1093](https://jira.bit.admin.ch/browse/EIDRAEDA-1093): Fix frontend navigation bug for static pages
- [EIDRAEDA-1113](https://jira.bit.admin.ch/browse/EIDRAEDA-1113): fix MDC not clearing in unforeseen errors
- [EIDRAEDA-1133](https://jira.bit.admin.ch/browse/EIDRAEDA-1133): Change verifier-agent-management API to versioned endpoints

## [1.6.0]
- [EIDRAEDA-536](https://jira.bit.admin.ch/browse/EIDRAEDA-536): Title adaptations


## [1.4.0] - 2024-06-04
- [EIDRAEDA-442](https://jira.bit.admin.ch/browse/EIDRAEDA-442): Logging adoptions
- [EIDRAEDA-433](https://jira.bit.admin.ch/browse/EIDRAEDA-433): Pages must have a unique, descriptive title.
- [EIDRAEDA-500](https://jira.bit.admin.ch/browse/EIDRAEDA-500): Oblique and Angular update
- [EIDRAEDA-426](https://jira.bit.admin.ch/browse/EIDRAEDA-426): Fix UI bug
- [EIDRAEDA-526](https://jira.bit.admin.ch/browse/EIDRAEDA-526): Add SEO keywords

## [1.3.0] - 2024-06-04
- [EIDRAEDA-512](https://jira.bit.admin.ch/browse/EIDRAEDA-512): Gray page displayed instead of red 
- [EIDRAEDA-429](https://jira.bit.admin.ch/browse/EIDRAEDA-429): Update Menu on Mobile LicenceCheck
- [EIDRAEDA-449](https://jira.bit.admin.ch/browse/EIDRAEDA-449): Remove jEAP framework
- [EIDRAEDA-492](https://jira.bit.admin.ch/browse/EIDRAEDA-492): Prepare the project for open source release

## [1.2.0] - 2024-04-25
- [EIDRAEDA-434](https://jira.bit.admin.ch/browse/EIDRAEDA-434): Adjusting Text Size and Scrollable Content (modals)

## [1.1.0] - 2024-04-02
- [EIDRAEDA-374](https://jira.bit.admin.ch/browse/EIDRAEDA-374): Cookies Verifier
- [EIDRAEDA-430](https://jira.bit.admin.ch/browse/EIDRAEDA-430): REF / ABN banner on LicenceCheck & VZ Controller
- [EIDRAEDA-432](https://jira.bit.admin.ch/browse/EIDRAEDA-432): Wording updates
- [EIDRAEDA-374](https://jira.bit.admin.ch/browse/EIDRAEDA-374): Cookies Verifier

## [1.0.0] - 2024-03-25 (Go Live!)
- [EIDRAEDA-405](https://jira.bit.admin.ch/browse/EIDRAEDA-405): Logo & Name Verifier - Implementation
- [EIDRAEDA-413](https://jira.bit.admin.ch/browse/EIDRAEDA-413): Dynamic Title Font Size in Verifier
- [EIDRAEDA-416](https://jira.bit.admin.ch/browse/EIDRAEDA-416): Verifier UI: Show Invalid Screen for Expired eLFA
- [EIDRAEDA-417](https://jira.bit.admin.ch/browse/EIDRAEDA-417): Verifier Homepage Landingpage
- [EIDRAEDA-418](https://jira.bit.admin.ch/browse/EIDRAEDA-418): Favicon update - LicenceCheck
- [EIDRAEDA-419](https://jira.bit.admin.ch/browse/EIDRAEDA-419): Update QR-Code and Foto text on Verifier
- [EIDRAEDA-424](https://jira.bit.admin.ch/browse/EIDRAEDA-424): Update LicenceCheck footer
- [EIDRAEDA-427](https://jira.bit.admin.ch/browse/EIDRAEDA-427): Update LicenceCheck and Issuer Version
- [EIDRAEDA-380](https://jira.bit.admin.ch/browse/EIDRAEDA-380): Add Verifier LogoURI & Name

## [0.3.0] - 2024-02-15
- [EIDRAEDA-373](https://jira.bit.admin.ch/browse/EIDRAEDA-373): Update VC Schema

## [0.2.1] - 2024-02-08
- [EIDRAEDA-369](https://jira.bit.admin.ch/browse/EIDRAEDA-369): Verifier - Timeout after each check
