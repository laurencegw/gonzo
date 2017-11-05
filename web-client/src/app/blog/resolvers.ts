import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {BlogEntry} from "./blog.models";
import {Observable} from "rxjs/Observable";
import {Inject, Injectable} from "@angular/core";
import {BlogService} from "./blog.service";


@Injectable()
export class BlogEntryResolver implements Resolve<BlogEntry> {

  constructor(@Inject("BlogService") private blogService: BlogService) {

  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<BlogEntry> | Promise<BlogEntry> | BlogEntry {
    return this.blogService.getBlogEntryById(route.params["id"]);
  }

}
