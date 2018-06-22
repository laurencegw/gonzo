import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {HomeComponent} from "./home/home.component";
import {BlogComponent} from "./blog/blog.component";
import {BlogEntryBlankComponent} from "./blog/blog-entry-view/blog-entry-blank.component";
import {BlogEntryResolverComponent} from "./blog/blog-entry-view/blog-entry-resolver.component";
import {BlogEntryResolver} from "./blog/resolvers";
import {BlogEntryNewComponent} from "./blog/blog-entry-new/blog-entry-new.component";


const appRoutes: Routes = [
  {path: "", redirectTo: "/home", pathMatch: 'full'},
  {path: "home", component: HomeComponent},
  {
    path: "blog", component: BlogComponent,
    children: [
      {path: "", pathMatch: 'full', component: BlogEntryBlankComponent},
      {path: "new", component: BlogEntryNewComponent},
      {path: ":id", resolve: {blogEntry: BlogEntryResolver}, component: BlogEntryResolverComponent},
    ]
  },
  {path: "**", redirectTo: "/home"}
];


@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {

}
