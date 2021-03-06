import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { UserdataService } from './_services/userdata.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'event-frontend';

  username: String = "";

  currentPage: String = "";

  constructor(private route: ActivatedRoute, public loginService: UserdataService){}

  ngOnInit(): void {

  }

  logout() {
    this.loginService.logout();
  }

  public hasPermission(perm:String) {
    return this.loginService.hasPermission(perm);
  }
}
