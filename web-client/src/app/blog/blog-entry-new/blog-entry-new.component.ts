import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {BlogService} from "../blog.service";
import {BlogEntry, NewBlogEntry} from "../blog.models";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-blog-entry-new',
  templateUrl: './blog-entry-new.component.html',
  styleUrls: ['./blog-entry-new.component.css']
})
export class BlogEntryNewComponent implements OnInit {

  newBlogEntryFormGroup: FormGroup;

  constructor(@Inject('BlogService') private blogService: BlogService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.newBlogEntryFormGroup = new FormGroup(
      {
        'title': new FormControl('', [Validators.required]),
        'content': new FormControl('')
      }
    );
  }

  onSubmit() {
    this.blogService.createBlogEntry(new NewBlogEntry(
      this.newBlogEntryFormGroup.get('title').value,
      this.newBlogEntryFormGroup.get('content').value,
      true
    )).subscribe(
      (blogEntry: BlogEntry) => {
        this.newBlogEntryFormGroup.reset();
        this.router.navigate(['../' + blogEntry.id], {relativeTo: this.route})
      }
    );
  }

}
