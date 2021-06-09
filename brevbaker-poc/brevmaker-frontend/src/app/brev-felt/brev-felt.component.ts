import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-brev-felt',
  templateUrl: './brev-felt.component.html',
  styleUrls: ['./brev-felt.component.css']
})
export class BrevFeltComponent implements OnInit {
  @Input() form!: FormGroup;
  @Input() letterField!:string;

  constructor() { }

  ngOnInit(): void {
  }

}
