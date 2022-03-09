import { formatDate, Time } from '@angular/common';
import { Component, Inject, LOCALE_ID, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Event1 } from 'src/app/_classes/Event1';
import { Event2 } from 'src/app/_classes/Event2';
import { Event2Team } from 'src/app/_classes/Event2Team';
import { Event3 } from 'src/app/_classes/Event3';
import { Game } from 'src/app/_classes/game';
import { Group } from 'src/app/_classes/Group';
import { AdminService } from 'src/app/_services/admin.service';
import { EventService } from 'src/app/_services/event.service';
import { UserdataService } from 'src/app/_services/userdata.service';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent implements OnInit {

  gamesArr: Game[] = [];
  userGroups: Group[] = [];

  topFormGrp: FormGroup;
  event1FormGrp: FormGroup;
  event2FormGrp: FormGroup;
  event3FormGrp: FormGroup;


  t2teamCount = new FormControl(2);
  t3teamCount = new FormControl(2);
  event2teams = [];
  event3teams = [];
  event3Slots = [];

  constructor(private router:Router, @Inject(LOCALE_ID) private locale: string, private _snackBar: MatSnackBar, private eventService: EventService, public formBuilder: FormBuilder, private loginService: UserdataService, private adminService: AdminService) {

    this.topFormGrp = this.formBuilder.group({
      game: ['', [Validators.required]],
      eventType: ['', [Validators.required]],
      eventName: ['', [Validators.required]],
      eventDate: ['', [Validators.required]],
      eventTime: ['', [Validators.required]],
      group: ['', [Validators.required]],
      description: ['']
    });

    this.event1FormGrp = this.formBuilder.group({
      minPeople: ['', [Validators.required]],
      maxPeople: ['', [Validators.required]]
    });

    this.event2FormGrp = this.formBuilder.group({});

    this.event3FormGrp = this.formBuilder.group({});
  }

  ngOnInit(): void {

    this.event1FormGrp.get('minPeople').addValidators([Validators.max(this.event1FormGrp.get('maxPeople').value)]);
    this.event1FormGrp.get('maxPeople').addValidators([Validators.min(this.event1FormGrp.get('minPeople').value)]);

    this.fetchGames();
    this.userGroups = this.loginService.userGroups;
  }


  fetchGames() {
    this.eventService.getAllGames().subscribe(data => {
      (data as Game[]).forEach(elem => {
        this.gamesArr.push(elem);
      });
      this.gamesArr.sort((a, b) => a.title > b.title ? 1 : -1);
    })
  }

  createEvent() {
    switch (this.topFormGrp.get('eventType').value) {
      case "event1": {
        if (this.topFormGrp.invalid || this.event1FormGrp.invalid) {
          this._snackBar.open("Bitte alle Felder ausfüllen.", null, { duration: 3000, panelClass: ['snackbar-red'] });
        }
        else {

          let fixedTime: Date = new Date (
            (this.topFormGrp.get('eventDate').value as Date).getFullYear(),
            (this.topFormGrp.get('eventDate').value as Date).getMonth(),
            (this.topFormGrp.get('eventDate').value as Date).getDate(),
            (this.topFormGrp.get('eventTime').value as Date).getHours(),
            (this.topFormGrp.get('eventTime').value as Date).getMinutes()
            );

          let event = {
            "eventName": this.topFormGrp.get('eventName').value,
            "gameID": (this.topFormGrp.get('game').value as Game).id,
            "date": formatDate(fixedTime,'yyyy-MM-dd HH:mm',this.locale),
            "description": this.topFormGrp.get('description').value,
            "groupID": this.topFormGrp.get('group').value == 'public' ? null : (this.topFormGrp.get('group').value as Group).id,
            "minPeople": this.event1FormGrp.get('minPeople').value,
            "maxPeople": this.event1FormGrp.get('maxPeople').value,
          };

          this.eventService.createEvent1(event).subscribe(data => {
            let returnObj = (data as Event1);
            this._snackBar.open("Event erstellt.", null, { duration: 2000, panelClass: ['snackbar-green'] });
            this.router.navigate(['/event', 1, returnObj.id]);
          }, error => {
            this._snackBar.open(error.error.message, null, { duration: 7000, panelClass: ['snackbar-red'] });
          });

        }
      }
      case "event2": {
        if (this.topFormGrp.invalid || this.event2FormGrp.invalid) {
          this._snackBar.open("Bitte alle Felder ausfüllen.", null, { duration: 3000, panelClass: ['snackbar-red'] });
        }
        else {
          let count:number = this.t2teamCount.value;

          let teamsArr = [];

          for (let i = 0; i < count; i++) {
            teamsArr.push({
              teamname: this.event2FormGrp.get("e2teamname"+i).value,
              teamdescription: this.event2FormGrp.get("e2teamdescription"+i).value,
              maxPeople: this.event2FormGrp.get("e2maxPeople"+i).value
            });
          }


          let fixedTime: Date = new Date (
            (this.topFormGrp.get('eventDate').value as Date).getFullYear(),
            (this.topFormGrp.get('eventDate').value as Date).getMonth(),
            (this.topFormGrp.get('eventDate').value as Date).getDate(),
            (this.topFormGrp.get('eventTime').value as Date).getHours(),
            (this.topFormGrp.get('eventTime').value as Date).getMinutes()
            );

          let event2toPush = {
            "gameID": (this.topFormGrp.get('game').value as Game).id,
            "eventName": this.topFormGrp.get("eventName").value,
            "date": formatDate(fixedTime,'yyyy-MM-dd HH:mm',this.locale),
            "groupID": this.topFormGrp.get('group').value == 'public' ? null : (this.topFormGrp.get('group').value as Group).id,
            "description": this.topFormGrp.get("description").value,
            "teams": teamsArr
          };

          this.eventService.createEvent2(event2toPush).subscribe(data => {
            let returnObj = (data as Event2);
            this._snackBar.open("Event erstellt.", null, { duration: 2000, panelClass: ['snackbar-green'] });
            this.router.navigate(['/event', 2, returnObj.id]);
          }, error => {
            this._snackBar.open(error.error.message, null, { duration: 7000, panelClass: ['snackbar-red'] });
          });
        }
      }
      case "event3": {
        if (this.topFormGrp.invalid || this.event3FormGrp.invalid) {
          this._snackBar.open("Bitte alle Felder ausfüllen.", null, { duration: 3000, panelClass: ['snackbar-red'] });
        }
        else {
          let count:number = this.t3teamCount.value;

          let teamsArr = [];

          for (let i = 0; i < count; i++) {
            
            let currTeam = {
              "squadname": this.event3FormGrp.get("e3teamname"+i).value,
              "description": this.event3FormGrp.get("e3teamdescription"+i).value,
              "slots": []
            };

            for (let j = 0; j < this.event3teams[i].e3slotCount; j++) {
              currTeam.slots.push({
                "role": this.event3FormGrp.get(("e3slot-"+i+"-"+j)).value
              });
            }
            
            teamsArr.push(currTeam);
          }


          let fixedTime: Date = new Date (
            (this.topFormGrp.get('eventDate').value as Date).getFullYear(),
            (this.topFormGrp.get('eventDate').value as Date).getMonth(),
            (this.topFormGrp.get('eventDate').value as Date).getDate(),
            (this.topFormGrp.get('eventTime').value as Date).getHours(),
            (this.topFormGrp.get('eventTime').value as Date).getMinutes()
            );

          let event3toPush = {
            "gameID": (this.topFormGrp.get('game').value as Game).id,
            "eventName": this.topFormGrp.get("eventName").value,
            "date": formatDate(fixedTime,'yyyy-MM-dd HH:mm',this.locale),
            "groupID": this.topFormGrp.get('group').value == 'public' ? null : (this.topFormGrp.get('group').value as Group).id,
            "description": this.topFormGrp.get("description").value,
            "teams": teamsArr
          };

          this.eventService.createEvent3(event3toPush).subscribe(data => {
            let returnObj = (data as Event3);
            this._snackBar.open("Event erstellt.", null, { duration: 2000, panelClass: ['snackbar-green'] });
            this.router.navigate(['/event', 3, returnObj.id]);
          }, error => {
            this._snackBar.open(error.error.message, null, { duration: 7000, panelClass: ['snackbar-red'] });
          });

        }
      }
    }
  }

  updateEvent2TeamCount() {
    let count:number = this.t2teamCount.value;
    this.event2teams = [];
    this.event2FormGrp = this.formBuilder.group({});

    for (let i = 0; i < count; i++) {
      this.event2FormGrp.addControl(('e2teamname'+i), new FormControl("Team #"+(i+1)));
      this.event2FormGrp.addControl(('e2teamdescription'+i), new FormControl(""));
      this.event2FormGrp.addControl(('e2maxPeople'+i), new FormControl(4));
      this.event2teams.push({teamname: "", teamdescription: "", maxPeople: 2});
    }

  }

  updateEvent3TeamCount() {
    let count:number = this.t3teamCount.value;
    this.event3teams = [];
    this.event3Slots = [];
    this.event3FormGrp = this.formBuilder.group({});

    for (let i = 0; i < count; i++) {
      this.event3FormGrp.addControl(('e3teamname'+i), new FormControl("Team #"+(i+1)));
      this.event3FormGrp.addControl(('e3teamdescription'+i), new FormControl(""));
      this.event3FormGrp.addControl(('e3slotcount'+i), new FormControl(2));
      this.event3teams.push({teamname: "", teamdescription: "", e3slotCount: 2, slots: []});
      this.event3Slots.push([]);
      this.updateEvent3SlotCount(i);
    }

  }

  eventTypeSelectionChanged() { 
    if (this.topFormGrp.get('eventType').value === "event2") this.updateEvent2TeamCount();
    else if (this.topFormGrp.get('eventType').value === "event3") this.updateEvent3TeamCount();
  }

  updateEvent3SlotCount(i:number) {
    this.event3Slots[i] = [];
    for (let j = 0; j < this.event3FormGrp.get("e3slotcount"+i).value; j++) {
      this.event3FormGrp.addControl(('e3slot-'+i+'-'+j), new FormControl(""));
      this.event3Slots[i].push([{role: ""}]);
    }
  }
  
}
