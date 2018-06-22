import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';

import {BlogListItemComponent} from './blog-list-item.component';
import {blogEntryHeader} from "../../fixtures";
import {Component} from "@angular/core";

@Component({
  template: ''
})
class DummyComponent {
}

describe('BlogListItemComponent', () => {
  let component: BlogListItemComponent;
  let fixture: ComponentFixture<BlogListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BlogListItemComponent, DummyComponent],
      imports: [
        RouterTestingModule.withRoutes([
          {path: ":id", component: DummyComponent}
        ])
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlogListItemComponent);
    component = fixture.componentInstance;
    component.blogEntryHeader = blogEntryHeader();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
