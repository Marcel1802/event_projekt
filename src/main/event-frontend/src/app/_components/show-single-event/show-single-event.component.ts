import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Event3 } from 'src/app/_classes/Event3';
import { Event1 } from 'src/app/_classes/Event1';
import { EventService } from 'src/app/_services/event.service';
import { Event2 } from 'src/app/_classes/Event2';

@Component({
  selector: 'app-show-single-event',
  templateUrl: './show-single-event.component.html',
  styleUrls: ['./show-single-event.component.scss']
})
export class ShowSingleEventComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute, private router: Router, private eventService: EventService) { }

  event: Event1 | Event2 | Event3 = null;

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      
      if (params.type == 1) {
        this.eventService.getEventByID(params.id).subscribe(data => {
          console.log(data);
          this.event = data as Event1;
          this.event.type = 1;
        }, error => {
          this.router.navigate(['/event-list']);
        });
      }
      else if (params.type == 2) {
        this.eventService.getEvent2ByID(params.id).subscribe(data => {
          console.log(data);
          this.event = data as Event2;
          this.event.type = 2;
        }, error => {
          this.router.navigate(['/event-list']);
        });
      }
      else {
        this.eventService.getEvent3ByID(params.id).subscribe(data => {
          console.log(data);
          this.event = data as Event3;
          this.event.type = 3;
        }, error => {
          this.router.navigate(['/event-list']);
        });
      }
      
    });
  }

}
