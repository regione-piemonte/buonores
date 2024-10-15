/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import * as uuid from 'uuid';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {
  intercept( req: HttpRequest<any>,next: HttpHandler ): Observable<HttpEvent<any>> {
    const authReq = req.clone({

      headers: new HttpHeaders({
        'X-Request-Id': uuid.v4(),
      }),
    });

    // console.log('Intercepted HTTP call', authReq);

    return next.handle(authReq);
  }
}
