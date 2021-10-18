import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { environment } from 'src/environments/environment';
import { Group } from '../_classes/Group';
import { Person } from '../_classes/person';
import { AdminService } from './admin.service';

@Injectable({
  providedIn: 'root'
})
export class UserdataService {

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

  constructor(private httpC: HttpClient, private adminService:AdminService, private keycloakService:KeycloakService) {
    this.initKeycloak();
  }

  private httpHeader = new HttpHeaders({
    'accept': '*/*'
  });

  initKeycloak() {

    let userID:string;
    let userName:string;

    try {
      userID = this.keycloakService.getKeycloakInstance().idTokenParsed['sub'];
      userName = this.keycloakService.getKeycloakInstance().idTokenParsed['preferred_username'];
    }
    catch(e) {
      console.error("Cannot load keycloak data");
      this.keycloakService.logout();
    }

    
    

    // mock data

    // Marcel     4e45e088-591a-4909-8bb9-0d923bd3e7c8
    // Donald     c4fcafc0-884d-49ff-8e4d-99be01e61615

    this.adminService.getPerson(userID).subscribe(data => {
      this.loadLoginData(data);
    },error => {

      console.warn("profil nicht gefunden.")

      this.adminService.createPerson(userID,userName).subscribe(data1 => {

        this.adminService.getPerson(userID).subscribe(data2 => {
          this.loadLoginData(data2);
        });

      },error => {

        console.warn("profil wieder nicht gefunden")

        this.keycloakService.logout();
      })
    });

    
    this._permissionArr = this.keycloakService.getUserRoles();
  }

  loadLoginData(data) {
    this._currentPerson = (data as Person);

      this.adminService.checkBans(this._currentPerson.id).subscribe(d => {
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
  }

  logout() {
    this.keycloakService.logout();
  }

  hasPermission(perm:String) {
    return this._permissionArr.includes(perm);
  }

  

  getLoginUsername() {
    if (this.currentPerson === null) {
      return "";
    }

    return this._currentPerson.gamertag;
  }

  

}
