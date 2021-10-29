import { formatDate } from '@angular/common';
import { Component, Inject, LOCALE_ID, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Ban } from 'src/app/_classes/Ban';
import { Person } from 'src/app/_classes/person';
import { AdminService } from 'src/app/_services/admin.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  public actualBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();
  public permanentBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();
  public expiredBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();
  public allUsers:Person[] = [];
  public addBanFormGroup:FormGroup;

  constructor(private adminService:AdminService, private _snackBar:MatSnackBar, private formBuilder:FormBuilder, @Inject(LOCALE_ID) private locale: string) { 
    this.addBanFormGroup = this.formBuilder.group({
      person: ['',[Validators.required]],
      reason: ['',[Validators.required]],
      isPermanent: ['',[Validators.required]],
      bannedUntilDate: [''],
      bannedUntilTime: ['']
    });
  }

  addBanFormValid():boolean {
    if (this.addBanFormGroup.get('person').value == null) {return false;}
    if (this.addBanFormGroup.get('reason').value == "") {return false;}

    if (!this.addBanFormGroup.get('isPermanent').value && 
    (this.addBanFormGroup.get('bannedUntilDate').value == "" || 
    this.addBanFormGroup.get('bannedUntilTime').value == "")) {return false;}

    return true;
  }

  ngOnInit(): void {
    this.loadBans();
    this.getAllUsers();
  }

  banPerson() {

    let tmpJSON:any;

    if (this.addBanFormValid()) {
      if (this.addBanFormGroup.get('isPermanent').value) {
        tmpJSON = {
          bannedPerson: {
            id: (this.addBanFormGroup.get('person').value as Person).id,
            gamertag: (this.addBanFormGroup.get('person').value as Person).gamertag,
          },
          reason: this.addBanFormGroup.get('reason').value,
          permanent: true,
          bannedUntil: null
        };
      }
      else {

        let tmpBannedUntil = new Date (
          (this.addBanFormGroup.get('bannedUntilDate').value as Date).getFullYear(),
          (this.addBanFormGroup.get('bannedUntilDate').value as Date).getMonth(),
          (this.addBanFormGroup.get('bannedUntilDate').value as Date).getDate(),
          (this.addBanFormGroup.get('bannedUntilTime').value as Date).getHours(),
          (this.addBanFormGroup.get('bannedUntilTime').value as Date).getMinutes()
          );

        tmpJSON = {
          bannedPerson: {
            id: (this.addBanFormGroup.get('person').value as Person).id,
            gamertag: (this.addBanFormGroup.get('person').value as Person).gamertag,
          },
          reason: this.addBanFormGroup.get('reason').value,
          permanent: false,
          bannedUntil: formatDate(tmpBannedUntil,'yyyy-MM-dd HH:mm',this.locale),
        };
      }

      this.adminService.createBan(tmpJSON).subscribe(data => {
        this._snackBar.open("Bann erfolgreich hinzugefügt", null, {duration: 3000, panelClass: ['snackbar-green']})
        this.loadBans();
      }, error => {
        this._snackBar.open("Bann konnte nicht hinzugefügt werden", null, {duration: 3000, panelClass: ['snackbar-red']})
      });

    }
  }

  getAllUsers() {
    this.adminService.getAllPersons().subscribe(data => {
      let tmpArr  = (data as Person[]);
      tmpArr.sort((a,b) => a.gamertag.toLowerCase() > b.gamertag.toLocaleLowerCase() ? 1 : -1);
      this.allUsers = tmpArr;
    });
  }

  getTableRows(){
    return ['bannedPerson','reason','banTime','bannedUntil','bannedFrom','deleteButton'];
  }

  getBannedUntil(param:Ban) {
    if (param.isPermanent) {
      return "Permanent";
    }
    else {
      return param.bannedUntil;
    }
  }

  deleteBan(param:Ban, tbl:number, index:number) {
    this.adminService.removeBan(param.id).subscribe(data => {
      switch (tbl) {
        case 1: {
          this.actualBans.data.splice(index,1);
          this.actualBans._updateChangeSubscription();
          break;
        }
        case 2: {
          this.permanentBans.data.splice(index,1);
          this.permanentBans._updateChangeSubscription();
          break;
        }
        case 3: {
          this.expiredBans.data.splice(index,1);
          this.expiredBans._updateChangeSubscription();
          break;
        }
      }
      this._snackBar.open("Bann erfolgreich gelöscht", null, {duration: 3000, panelClass: ['snackbar-green']})
    }, error => {
      this._snackBar.open("Bann konnte nicht gelöscht werden", null, {duration: 3000, panelClass: ['snackbar-red']})
    });
  }

  loadBans() {

    this.adminService.getActualBans().subscribe(data => {
      if (data != null) {
        let tmpArr:any[] = [];
        (data as Ban[]).forEach(elem => {
          tmpArr.push(elem as Ban);
        })
        this.actualBans.data = tmpArr;
      }
    });

    this.adminService.getPermanentBans().subscribe(data => {
      if (data != null) {
        let tmpArr:any[] = [];
        (data as Ban[]).forEach(elem => {
          tmpArr.push(elem as Ban);
        })
        this.permanentBans.data = tmpArr;
      }
    });

    this.adminService.getExpiredBans().subscribe(data => {
      if (data != null) {
        let tmpArr:any[] = [];
        (data as Ban[]).forEach(elem => {
          tmpArr.push(elem as Ban);
        })
        this.expiredBans.data = tmpArr;
      }
    });

  }

}
