import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {BrevViserComponent} from "./brev-viser/brev-viser.component";
import {HttpClientModule} from "@angular/common/http";
import {BrevService} from "./brev-service.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { BrevFeltComponent } from './brev-felt/brev-felt.component';

@NgModule({
  declarations: [
    AppComponent,
    BrevViserComponent,
    BrevFeltComponent
  ],
  imports: [
    BrowserModule,
    NgxExtendedPdfViewerModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [BrevService,],
  bootstrap: [AppComponent]
})
export class AppModule { }
