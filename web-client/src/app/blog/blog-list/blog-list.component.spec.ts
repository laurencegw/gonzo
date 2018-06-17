import {Component, CUSTOM_ELEMENTS_SCHEMA, Input} from '@angular/core';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BlogListComponent} from './blog-list.component';
import {StubBlogService} from "../blog.service";
import {BlogEntryHeader} from "../blog.models";
import {BlogListItemComponent} from "./blog-list-item/blog-list-item.component";

@Component({
  selector: 'app-blog-list-item',
  template: '{{ blogEntryHeader.title }}',
})
class MockBlogListItem {
  @Input()
  public blogEntryHeader: BlogEntryHeader;
}


describe('BlogListComponent', () => {
  let component: BlogListComponent;
  let fixture: ComponentFixture<BlogListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BlogListComponent],
      providers: [
        {
          provide: BlogListItemComponent,
          useClass: MockBlogListItem
        },
        {provide: "BlogService", useClass: StubBlogService}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlogListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
