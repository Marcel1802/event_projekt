import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { Game } from 'src/app/_classes/game';
import { EventService } from 'src/app/_services/event.service';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent implements OnInit, AfterViewInit {

  gamesArr:Game[] = [];

  formTop = new FormGroup({
    game: new FormControl(''),
    eventType: new FormControl('')
  });

  constructor(private eventService: EventService) {
  }

  ngOnInit(): void {
    this.fetchGames();
  }

  ngAfterViewInit(): void {
    //TODO: downdown 1st value selected
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
