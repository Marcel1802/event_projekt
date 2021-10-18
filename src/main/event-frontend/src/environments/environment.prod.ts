import { KeycloakService } from "keycloak-angular";

export const environment = {
  production: true,
  requestURL: "http://127.0.0.1:8080"
};

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
