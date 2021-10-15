import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowSingleEventComponent } from './show-single-event.component';

describe('ShowSingleEventComponent', () => {
  let component: ShowSingleEventComponent;
  let fixture: ComponentFixture<ShowSingleEventComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowSingleEventComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowSingleEventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
