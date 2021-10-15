import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UserManagementComponent } from './_components/user-management/user-management.component';
import { CreateEventComponent } from './_components/create-event/create-event.component';
import { DeleteEventComponent } from './_components/delete-event/delete-event.component';
import { EditEventComponent } from './_components/edit-event/edit-event.component';
import { AppRoutingModule } from './app-routing.module';
import { EventListComponent } from './_components/event-list/event-list.component';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatSortModule } from '@angular/material/sort';
import { MatSelectModule } from '@angular/material/select';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

import { HttpClientModule } from '@angular/common/http';
import { LoginService } from './_services/login.service';
import { ShowSingleEventComponent } from './_components/show-single-event/show-single-event.component';

@NgModule({
  declarations: [
    AppComponent,
    UserManagementComponent,
    CreateEventComponent,
    DeleteEventComponent,
    EditEventComponent,
    EventListComponent,
    ShowSingleEventComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MatToolbarModule,
    MatSidenavModule,
    MatIconModule,
    MatTableModule,
    MatButtonModule,
    MatTabsModule,
    MatCardModule,
    MatSortModule,
    MatSelectModule,
    MatProgressSpinnerModule
  ],
  exports: [],
  providers: [HttpClientModule, LoginService],
  bootstrap: [AppComponent]
})
export class AppModule { }
