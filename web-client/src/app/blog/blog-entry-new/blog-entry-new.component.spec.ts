import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlogEntryNewComponent } from './blog-entry-new.component';

describe('BlogEntryNewComponent', () => {
  let component: BlogEntryNewComponent;
  let fixture: ComponentFixture<BlogEntryNewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlogEntryNewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlogEntryNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
