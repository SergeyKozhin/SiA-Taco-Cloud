import { Component, OnInit, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {CartService} from "../cart/cart-service";

@Component({
  selector: 'recent-tacos',
  templateUrl: 'recents.component.html',
  styleUrls: ['./recents.component.css']
})

@Injectable()
export class RecentTacosComponent implements OnInit {
  recentTacos: any;

  constructor(private httpClient: HttpClient,
              private cart: CartService) { }

  ngOnInit() {
    this.httpClient.get('http://localhost:8080/design/recent') // <1>
        .subscribe(data => this.recentTacos = data);
  }

  addToCart(taco) {
    this.cart.addToCart(taco)
  }
}
