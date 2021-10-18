import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { KeycloakService } from 'keycloak-angular';
import { Observable, from } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { logging } from 'selenium-webdriver';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private keycloak:KeycloakService) {}

  public intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const {enableBearerInterceptor, excludedUrls } = this.keycloak;
    if (!enableBearerInterceptor) {
      return next.handle(req);
    }

    return from(this.keycloak.isLoggedIn()).pipe(
      mergeMap((loggedIn: boolean) => loggedIn
      ? this.handleRequestWithTokenHeader(req,next)
      : next.handle(req))
    );
  }

  private handleRequestWithTokenHeader(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<any> {
    return this.keycloak.addTokenToHeader(req.headers).pipe(
      mergeMap(headersWithBearer => {
        const kcReq = req.clone({headers: headersWithBearer});
        return next.handle(kcReq);
      })
    );
  }







}
