package com.example.restaurant.navigation

sealed class Route(var route: String) {
    data object EnterRoute : Route("enter")
    data object MenuRoute : Route("menu")
    data object CartRoute: Route("cart")
    data object AdminPanelRoute: Route("admin")

}