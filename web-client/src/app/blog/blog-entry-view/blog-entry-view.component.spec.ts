import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlogEntryViewComponent } from './blog-entry-view.component';

describe('BlogEntryViewComponent', () => {
  let component: BlogEntryViewComponent;
  let fixture: ComponentFixture<BlogEntryViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlogEntryViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlogEntryViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
