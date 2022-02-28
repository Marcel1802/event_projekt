import { formatDate, Time } from '@angular/common';
import { Component, Inject, LOCALE_ID, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Event1 } from 'src/app/_classes/Event1';
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

  team2TeamCount = 2;
  t2teamCount = new FormControl();
  event2teams = [];

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

      }
      case "event3": {

      }
    }
  }

  updateEvent2TeamCount() {
    let count:number = this.t2teamCount.value;
    this.event2teams = [];
    this.event2FormGrp = this.formBuilder.group({});

    for (let i = 0; i < count; i++) {
      this.event2FormGrp.addControl(('e2teamname'+i), new FormControl("Team #"+(i+1)));
      this.event2FormGrp.addControl(('e2maxPeople'+i), new FormControl(4));
      this.event2teams.push({teamname: "", maxPeople: 2});
    }

  }

  eventTypeSelectionChanged() { if (this.topFormGrp.get('eventType').value === "event2") this.updateEvent2TeamCount();}
  
}
