import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { Game } from 'src/app/_classes/game';
import { EventService } from 'src/app/_services/event.service';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent implements OnInit {

  gamesArr:Game[] = [];

  topFormGrp: FormGroup;

  constructor(private eventService: EventService, public formBuilder:FormBuilder) {
    this.topFormGrp = this.formBuilder.group({
      game: ['', [Validators.required]],
      eventType: ['', [Validators.required]],
      eventName: ['', [Validators.required]],
      eventDate: ['', [Validators.required]],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.fetchGames();
  }

  fetchGames() {
    this.eventService.getAllGames().subscribe(data => {
      (data as Game[]).forEach(elem => {
        this.gamesArr.push(elem);
      });
      this.gamesArr.sort((a,b) => a.title > b.title ? 1 : -1);
    })
  }
}
