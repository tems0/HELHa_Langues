import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean | UrlTree {

    if (sessionStorage.getItem("email")!=null&&sessionStorage.getItem("email")!.includes("helha.be")) {
      const role = sessionStorage.getItem("role");
      if (role === 'Student') {
        return true;
      } else {
        return this.router.createUrlTree(['/not-autorised']);
      }
    } else {
      return this.router.createUrlTree(['/home']);
    }
  }

}
