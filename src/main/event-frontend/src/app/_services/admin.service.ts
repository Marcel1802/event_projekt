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

  public removeBan(id:string) {
    return this.httpC.delete(environment.requestURL + `/admin/ban/delete/` + id, {headers: this.httpHeader });
  }

  public getGroupsOfPerson(id:string) {
    return this.httpC.get(environment.requestURL + `/admin/group/get/forPerson/` + id, {headers: this.httpHeader });
  }

  public createPerson(id:string,name:string) {
    return this.httpC.post(environment.requestURL + `/person/create/` + id + "/" + name, {headers: this.httpHeader });
  }

  public getPerson(UUID:string) {
    return this.httpC.get(environment.requestURL + `/person/get/byID/` + UUID, {headers: this.httpHeader });
  }

  public checkBans(UUID:string) {
    return this.httpC.get(environment.requestURL + `/admin/ban/checkperson/` + UUID, {headers: this.httpHeader });
  }

}
