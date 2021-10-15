import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Ban } from 'src/app/_classes/Ban';
import { AdminService } from 'src/app/_services/admin.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  constructor(private adminService:AdminService) { }

  public actualBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();
  public permanentBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();
  public expiredBans:MatTableDataSource<Ban[]> = new MatTableDataSource<Ban[]>();

  ngOnInit(): void {
    this.loadBans();
  }

  getTableRows(){
    return ['bannedPerson','bannedUntil','reason','bannedFrom','banTime','deleteButton'];
  }

  getBannedUntil(param:Ban) {
    if (param.isPermanent) {
      return "Permanent";
    }
    else {
      return param.bannedUntil;
    }
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
