import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EventService implements OnInit {

  constructor(private httpC: HttpClient) { }

  private httpHeader = new HttpHeaders({
    'accept': '*/*'
  });

  ngOnInit() {
  }

  public getAllFutureEvents() {
    return this.httpC.get(environment.requestURL + `/event1/get/allFuture`, {headers: this.httpHeader });
  }

  public getAllFutureEvent2Events() {
    return this.httpC.get(environment.requestURL + `/event2/get/allFuture`, {headers: this.httpHeader })
  }

  public getAllFutureEvent3Events() {
    return this.httpC.get(environment.requestURL + `/event3/get/allFuture`, {headers: this.httpHeader })
  }




  public getAllPastEvents() {
    return this.httpC.get(environment.requestURL + `/event1/get/allPast`, {headers: this.httpHeader });
  }

  public getAllPastEvent2Events() {
    return this.httpC.get(environment.requestURL + `/event2/get/allPast`, {headers: this.httpHeader })
  }

  public getAllPastEvent3Events() {
    return this.httpC.get(environment.requestURL + `/event3/get/allPast`, {headers: this.httpHeader })
  }




  public getEventByID(param: string) {
    return this.httpC.get(environment.requestURL + `/event1/get/byID/` + param, {headers: this.httpHeader });
  }

  public getEvent2ByID(param: string) {
    return this.httpC.get(environment.requestURL + `/event2/get/byID/` + param, {headers: this.httpHeader });
  }

  public getEvent3ByID(param: string) {
    return this.httpC.get(environment.requestURL + `/event3/get/byID/` + param, {headers: this.httpHeader });
  }




  public getAllGames() {
    return this.httpC.get(environment.requestURL + `/game/get/all`, {headers: this.httpHeader});
  }
}
