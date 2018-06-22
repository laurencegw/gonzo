import {Component, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core"
import {async, ComponentFixture, TestBed} from "@angular/core/testing"

import {BlogComponent} from "./blog.component"
import {BlogListComponent} from "./blog-list/blog-list.component"
import {StubBlogService} from "./blog.service"
import {RouterTestingModule} from "@angular/router/testing"

@Component({
  selector: "app-blog-list",
  template: "{{ blogEntryHeader.title }}",
})
class MockBlogListComponent {
}

@Component({
  template: ""
})
class MockNewEntryComponent {
}


describe("BlogComponent", () => {
  let component: BlogComponent
  let fixture: ComponentFixture<BlogComponent>

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BlogComponent, MockNewEntryComponent],
      imports: [
        RouterTestingModule.withRoutes([
          {path: "new", component: MockNewEntryComponent}
        ])
      ],
      providers: [
        {
          provide: BlogListComponent,
          useClass: MockBlogListComponent
        },
        {provide: "BlogService", useClass: StubBlogService}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents()
  }))

  beforeEach(() => {
    fixture = TestBed.createComponent(BlogComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
