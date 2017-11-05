import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlogEntryViewComponent } from './blog-entry-view.component';
import {BlogEntry} from "../blog.models";
import {By} from "@angular/platform-browser";

describe('BlogEntryViewComponent', () => {
  let component: BlogEntryViewComponent;
  let fixture: ComponentFixture<BlogEntryViewComponent>;
  let blogEntry = new BlogEntry(
    1,
    new Date(),
    new Date(),
    "My Blog Entry",
    "The contents of my blog entry"
  );

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlogEntryViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlogEntryViewComponent);
    component = fixture.componentInstance;
    component.blogEntry = blogEntry;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the Title and contents of the blog', () =>{
    var titleDebug = fixture.debugElement.query(By.css('#title'));
    var titleElement = titleDebug.nativeElement;
    expect(titleElement.textContent).toContain(blogEntry.title);

    var contentDebug = fixture.debugElement.query(By.css('#content'));
    var contentElement = contentDebug.nativeElement;
    expect(contentElement.textContent).toContain(blogEntry.content);
  })
});
