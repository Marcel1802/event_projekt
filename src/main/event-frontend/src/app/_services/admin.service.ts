import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private httpC:HttpClient) { }

  private httpHeader = new HttpHeaders({
    'accept': '*/*'
  });

  public getActualBans() {
    return this.httpC.get(environment.requestURL + `/admin/ban/get/actual`, {headers: this.httpHeader });
  }

  public getPermanentBans() {
    return this.httpC.get(environment.requestURL + `/admin/ban/get/permanent`, {headers: this.httpHeader });
  }

  public getExpiredBans() {
    return this.httpC.get(environment.requestURL + `/admin/ban/get/expired`, {headers: this.httpHeader });
  }



  
}
