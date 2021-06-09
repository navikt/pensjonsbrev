import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BrevService} from "../brev-service.service";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import { FormControl, ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-brev-viser',
  templateUrl: './brev-viser.component.html',
  styleUrls: ['./brev-viser.component.css']
})
export class BrevViserComponent implements OnInit {
  formFields: Array<string> = []
  constructor(private http: HttpClient,
              private brevService: BrevService,
              private fb: FormBuilder) {
    brevService.getLetterTypes()
      .subscribe(value => {
        this.templates = value
      })

  }

  letterForm = this.fb.group({
    letterTemplateController: ['', Validators.required],
    letterField: []

  });
  pdf: Blob = new Blob();
  templates: Array<String> = [];
  selectedLetter: any;

  ngOnInit(): void {
  }

  onSubmit() {
    this.brevService.getLetter(this.letterForm.value, this.selectedLetter)
      .subscribe(value  => {
        this.pdf = value
      })
  }

  selectLetterTemplate(e: any) {
    this.selectedLetter = e.target.value
    this.brevService.getLetterFields(this.selectedLetter)
      .subscribe(value => {
        this.formFields = value
        value.forEach(field => {
          this.letterForm.addControl(field, new FormControl(field))

        })
      })
  }
}
