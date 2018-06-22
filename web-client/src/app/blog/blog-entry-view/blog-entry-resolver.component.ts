import {Component, OnDestroy, OnInit} from '@angular/core';
import {BlogEntry} from "../blog.models";
import {ActivatedRoute, Params} from "@angular/router";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-blog-entry-resolver',
  template: '<app-blog-entry-view [blogEntry]="blogEntry"></app-blog-entry-view>',
  styleUrls: ['./blog-entry-view.component.css']
})
export class BlogEntryResolverComponent implements OnInit, OnDestroy {

  blogEntry: BlogEntry;

  private subscription: Subscription;


  constructor(private activatedRoute: ActivatedRoute) {

  }

  ngOnInit() {
    this.subscription = this.activatedRoute.data.subscribe((data: Params) => {
      this.blogEntry = data["blogEntry"]
    })
  }

  ngOnDestroy() {
    this.subscription.unsubscribe()
  }

}
