import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Group } from '../_classes/Group';
import { Person } from '../_classes/person';
import { AdminService } from './admin.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  // Keycloak Stuff
  private _permissionArr:String[] = [];
  private _currentPerson: Person = null;
  private _isBanned: boolean = null;
  private _userGroups: Group[] = [];

  public get userGroups(): Group[] {
    return this._userGroups;
  }

  public get isBanned(): boolean {
    return this._isBanned;
  }

  public get currentPerson(): Person {
    return this._currentPerson;
  }

  public get permissionArr() {
    return this._permissionArr;
  }

  constructor(private httpC: HttpClient, private adminService:AdminService) {
    this.initKeycloak();
  }

  private httpHeader = new HttpHeaders({
    'accept': '*/*'
  });

  initKeycloak() {

    // mock data

    // Marcel     4e45e088-591a-4909-8bb9-0d923bd3e7c8
    // Donald     c4fcafc0-884d-49ff-8e4d-99be01e61615      

    this.getPerson("4e45e088-591a-4909-8bb9-0d923bd3e7c8").subscribe(data => {
      this._currentPerson = (data as Person);

      this.checkBans(this._currentPerson.id).subscribe(d => {
        this._isBanned = (d as boolean);
      })

      this.adminService.getGroupsOfPerson(this._currentPerson.id).subscribe(data => {
        if (data != null) {
          (data as Group[]).forEach(elem => {
            this.userGroups.push(elem as Group);
          });
          this.userGroups.sort((a,b) => a.name > b.name ? 1 : -1);
        }
      });
    });

    
    this._permissionArr = [
      "event_canCreateEvent",
      "event_canEditOwn",
      "event_canEditAll",
      "event_canDeleteOwn",
      "event_canDeleteAll",
      "event_usermanagement",
      "event_showAllEvents",
    ];
  }

  logout() {
    console.debug("logout button pressed");
  }

  hasPermission(perm:String) {
    return this._permissionArr.includes(perm);
  }

  getPerson(UUID:string) {
    return this.httpC.get(environment.requestURL + `/person/get/byID/` + UUID, {headers: this.httpHeader });
  }

  getLoginUsername() {
    if (this.currentPerson === null) {
      return "";
    }

    return this._currentPerson.gamertag;
  }

  checkBans(UUID:string) {
    return this.httpC.get(environment.requestURL + `/admin/ban/checkperson/` + UUID, {headers: this.httpHeader });
  }

}
