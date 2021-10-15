import { Component, OnInit, ViewChild } from '@angular/core';
import { Event1 } from 'src/app/_classes/Event1';
import { EventService } from 'src/app/_services/event.service';
import { Event3 } from 'src/app/_classes/Event3';;
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { Event2 } from 'src/app/_classes/Event2';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {

  constructor(private eventService: EventService) { }

  public dataSourceFutureArr: MatTableDataSource<(Event1 | Event2 | Event3)> = new MatTableDataSource<(Event1 | Event2 | Event3)>();
  public dataSourcePastArr: MatTableDataSource<(Event1 | Event2 | Event3)> = new MatTableDataSource<(Event1 | Event2 | Event3)>();

  getTableRows(){
    return ['game', 'eventName', 'date', 'createdBy', 'slots', 'group', 'button'];
  }

  getGroup(event: Event1 | Event2 | Event3) {
    if (event.group == null) {
      return "Öffentlichs Event";
    }
    else {
      return "\"" + event.group.name + "\"";
    }
  }

  ngOnInit(): void {
    this.getFutureEvents();
    this.getPastEvents();
  }

  @ViewChild(MatSort, { static: true }) sortPast: MatSort;
  @ViewChild(MatSort, { static: true }) sortFuture: MatSort;

  resetArrFuture() {
    this.dataSourceFutureArr.sortingDataAccessor = (item, property) => {
      switch(property) {
        case 'game': return item.game.title;
        default: return item[property];
      }
    };
    this.dataSourceFutureArr.sort = this.sortFuture;
  }

  resetArrPast() {
    this.dataSourcePastArr.sortingDataAccessor = (item, property): string | number => {
      switch(property) {
        case 'game': return item.game.title;
        case 'slots': return (this.getUsedSlots(item) / this.getTotalSlots(item));
        default: return item[property];
      }
    }
    this.dataSourcePastArr.sort = this.sortPast;
  }

  getFutureEvents() {

    let tmpArr:any[] = [];

    this.eventService.getAllFutureEvents().subscribe(data => {
      if (data != null) {
        (data as Event1[]).forEach(elem => {
          elem.type = 1;
          tmpArr.push((elem as Event1));
        });
        this.dataSourceFutureArr.data = tmpArr;
        this.resetArrFuture();
      }
    });

    this.eventService.getAllFutureEvent2Events().subscribe(data => {
      if (data != null) {
        (data as Event2[]).forEach(elem => {
          elem.type = 2;
          tmpArr.push((elem as Event2));
        });
        this.dataSourceFutureArr.data = tmpArr;
        this.resetArrFuture();
      }
    });

    this.eventService.getAllFutureEvent3Events().subscribe(data => {
      if (data != null) {
        (data as Event3[]).forEach(elem => {
          elem.type = 3;
          tmpArr.push((elem as Event3));
        });
        this.dataSourceFutureArr.data = tmpArr;
        this.resetArrFuture();
      }
    });

  }

  getPastEvents() {

    let tmpArr:any[] = [];

    this.eventService.getAllPastEvents().subscribe(data => {
      if (data != null) {
        (data as Event1[]).forEach(elem => {
          elem.type = 1;
          tmpArr.push((elem as Event1));
        });
        this.dataSourcePastArr.data = tmpArr;
        this.resetArrPast();
      }
    });

    this.eventService.getAllPastEvent2Events().subscribe(data => {
      if (data != null) {
        (data as Event2[]).forEach(elem => {
          elem.type = 2;
          tmpArr.push((elem as Event2));
        });
        this.dataSourcePastArr.data = tmpArr;
        this.resetArrPast();
      }
    });

    this.eventService.getAllPastEvent3Events().subscribe(data => {
      if (data != null) {
        (data as Event3[]).forEach(elem => {
          elem.type = 3;
          tmpArr.push((elem as Event3));
        });
        this.dataSourcePastArr.data = tmpArr;
        this.resetArrPast();
      }
    });
  }

  public getEventGameTitle(param: any) {
    return param.game.title;
  }

  public getTotalSlots(param: any) {


    if (param.type == 3) {
      let i = 0;

      if (param.squads == null) {
        return 0;
      }

      for (let sq of param.squads) {
        sq.slots.forEach(element => {
          i = i + 1;
        });
      }
      return i;
    }
    else if (param.type == 2) {
      let sum: number = 0;

      if (param.teams == null) {
        return 0;
      }

      param.teams.forEach(elem => {
        sum += elem.maxPeople;
      });
      return sum;
    }
    else {
      return param.maxPeople;
    }

  }

  public getUsedSlots(param: any) {

    if (param.type == 3) {
      let i = 0;

      if (param.squads == null) {
        return 0;
      }

      for (let sq of param.squads) {
        sq.slots.forEach(element => {
          if (element.slotUsedBy != null) {
            i = i + 1;
          }

        });
      }
      return i;
    }
    else if (param.type == 2) {
      let sum: number = 0;

      param.teams.forEach(elem => {
        elem.members.forEach(e => {
          sum += 1;
        })
      });


      return sum;
    }
    else {
      let i = 0;

      (param as Event1).participants.forEach(elem => {
        i = i + 1;
      });

      return i;
    }
  }

}
