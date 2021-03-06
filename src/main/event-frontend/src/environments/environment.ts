// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import { KeycloakService } from "keycloak-angular";

export const environment = {
  production: false,
  requestURL: "http://127.0.0.1:8080"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.

export function initializer(keycloak: KeycloakService) {
  return () =>
  keycloak.init({
    config: {
      url: 'http://localhost:8180/auth',
      realm: 'test-realm',
      clientId: 'event-frontend-client',
    },
    initOptions: {
      onLoad: 'login-required',
      silentCheckSsoRedirectUri:
        window.location.origin + '/assets/slient-check-sso.html',
    },
    enableBearerInterceptor: true
  });
}
