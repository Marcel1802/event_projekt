import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Ban } from 'src/app/_classes/Ban';
import { AdminService } from 'src/app/_services/admin.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  constructor(private adminService:AdminService, private _snackBar:MatSnackBar) { }

  public actualBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();
  public permanentBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();
  public expiredBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();

  ngOnInit(): void {
    this.loadBans();
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
        }
        case 2: {
          this.permanentBans.data.splice(index,1);
          this.permanentBans._updateChangeSubscription();
        }
        case 3: {
          this.expiredBans.data.splice(index,1);
          this.expiredBans._updateChangeSubscription();
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
