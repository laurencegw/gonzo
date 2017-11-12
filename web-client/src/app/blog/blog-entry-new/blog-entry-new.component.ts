import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-blog-entry-new',
  templateUrl: './blog-entry-new.component.html',
  styleUrls: ['./blog-entry-new.component.css']
})
export class BlogEntryNewComponent implements OnInit {

  newBlogEntryFormGroup: FormGroup;

  constructor() {
  }

  ngOnInit() {
    this.newBlogEntryFormGroup = new FormGroup(
      {
        'title': new FormControl('', [Validators.required]),
        'content': new FormControl('')
      }
    );
  }

  onSubmit(){

  }

}
