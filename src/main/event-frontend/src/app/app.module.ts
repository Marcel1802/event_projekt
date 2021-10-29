import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatTimepickerModule } from 'mat-timepicker';
import { MAT_DATE_LOCALE } from '@angular/material/core';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { UserdataService } from './_services/userdata.service';
import { ShowSingleEventComponent } from './_components/show-single-event/show-single-event.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { KeycloakBearerInterceptor, KeycloakService } from 'keycloak-angular';
import { AuthGuardService } from './_services/auth-guard.service';
import { initializer } from 'src/environments/environment';
import { ManageGroupsComponent } from './_components/manage-groups/manage-groups.component';


@NgModule({
  declarations: [
    AppComponent,
    UserManagementComponent,
    CreateEventComponent,
    DeleteEventComponent,
    EditEventComponent,
    EventListComponent,
    ShowSingleEventComponent,
    ManageGroupsComponent,
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
    MatProgressSpinnerModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatTimepickerModule
    
  ],
  exports: [],
  providers: [
    HttpClientModule,
    UserdataService,
    {provide: MAT_DATE_LOCALE, useValue: 'de-DE'},
    KeycloakService,
    KeycloakBearerInterceptor,
    AuthGuardService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: KeycloakBearerInterceptor,
      multi: true
    },
    {
      provide: APP_INITIALIZER,
      useFactory: initializer,
      multi: true,
      deps: [KeycloakService]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
