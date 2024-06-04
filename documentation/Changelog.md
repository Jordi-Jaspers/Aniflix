# CHANGELOG

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## UNRELEASED

### ANI-149:
* Showing a changelog in the application to keep users informed about the changes via a dialog.
* The dialog contains the changes between the current version and the previous version.

### ANI-147:
* Introduced a loading indicator when interacting with an anime for more user feedback.
  * Loading indicator is necessary because of high latency of some API's

### ANI-148:
* Replacing current toast notifications with a more user-friendly version.

### ANI-146:
* Replace Stripe with 'buy me a coffee' link in the header.

### ANI-133:
* Retrieving the episodes from (api.ani.zip) because that api is more up-to-date, stable and faster.
* Rerouted all the anime requests to the new api.
* Removed Tracing from the application because it is unnecessary.
* Database changes:
  * Added version to the user table for optimistic locking.
  * Dropped image_url column from the episode table.
  * Added a summary to the episode table.
  * Added fan_art and clear_logo to the anime table.

## [0.1.4] - 2024-06-03
### ANI-140:
* Removed the default shadcn logo from the login page.

### ANI-142:
* Adding tailwind prettier configuration.

### ANI-141:
* Removed overkill password requirements in the server.
* Made the client component more interactive by indicating the password requirements.
* Strength meter is synced with server requirements.

## [0.1.3] - 2024-06-02
* Hotfix: password reset flow did not work because of wrong request form.

## [0.1.2] - 2024-06-01
### ANI-134:
* Try-catch to not break the application when the database is down.
* Removing first-name and last-name requirement from the user registration form.
* Increase the default metaspace size and heap size for the JVM.
* Fixing the forgot password link in the login page.
* Partly implemented import and export from the library.

## [0.1.1] - 2024-06-01
### ANI-133:
* Adding generic exception handler for all database operations.
* Fixing the stripe link in the header.
* Updated the readme for a more professional look.

## [0.1.0] - 2024-06-01
*Initial Release*

