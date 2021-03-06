import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private httpC: HttpClient) { }

  private httpGetHeader = new HttpHeaders({'accept': '*/*'});
  private httpSendHeader = new HttpHeaders({'accept': '*/*', 'content-type': 'application/json'});

  public getAllFutureEvents() {
    return this.httpC.get(environment.requestURL + `/event1/get/allFuture`, {headers: this.httpGetHeader });
  }

  public getAllFutureEvent2Events() {
    return this.httpC.get(environment.requestURL + `/event2/get/allFuture`, {headers: this.httpGetHeader })
  }

  public getAllFutureEvent3Events() {
    return this.httpC.get(environment.requestURL + `/event3/get/allFuture`, {headers: this.httpGetHeader })
  }

  public getAllPastEvents() {
    return this.httpC.get(environment.requestURL + `/event1/get/allPast`, {headers: this.httpGetHeader });
  }

  public getAllPastEvent2Events() {
    return this.httpC.get(environment.requestURL + `/event2/get/allPast`, {headers: this.httpGetHeader })
  }

  public getAllPastEvent3Events() {
    return this.httpC.get(environment.requestURL + `/event3/get/allPast`, {headers: this.httpGetHeader })
  }

  public getEventByID(param: string) {
    return this.httpC.get(environment.requestURL + `/event1/get/byID/` + param, {headers: this.httpGetHeader });
  }

  public getEvent2ByID(param: string) {
    return this.httpC.get(environment.requestURL + `/event2/get/byID/` + param, {headers: this.httpGetHeader });
  }

  public getEvent3ByID(param: string) {
    return this.httpC.get(environment.requestURL + `/event3/get/byID/` + param, {headers: this.httpGetHeader });
  }

  public getAllGames() {
    return this.httpC.get(environment.requestURL + `/game/get/all`, {headers: this.httpGetHeader});
  }

  public createEvent1(param:any) {
    return this.httpC.post(environment.requestURL + `/event1/create`, param, {headers: this.httpSendHeader});
  }

  public createEvent2(param:any) {
    return this.httpC.post(environment.requestURL + `/event2/create/event`, param, {headers: this.httpSendHeader});
  }

  public createEvent3(param:any) {
    return this.httpC.post(environment.requestURL + `/event3/create/event`, param, {headers: this.httpSendHeader});
  }
}
