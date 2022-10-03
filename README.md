# Eat Well Food Delivery Web Platform Development

## About

This project is a Java web development that completes the backend features for an online food ordering and delivery system like Uber Eats. It includes a "server" side for the restaurant to manage its menu and employees, and a "client" side for the users to view menu, order food and checkout.

## Tools and Frameworks

- Spring Boot
- MyBatis Plus
- MySQL
- Redis
- Maven

## Features

- Global:
  - Filter: built with `FilterChain` and `ThreadLocal` to prevent unauthorized user from accessing certain webpages that require logging in
  - Use **Redis** to cache dish/combo data
  - Implement **database sharding** to separate MySQL read and write
  - Global Exception Handler: customized exception

- Server side for Restaurant management:
  - Restaurant staff login
  - Display/Add/Edit employee information
  - Display/Add/Edit dish (and/or combo meal)

- Client side for User experience:
  - Mobile phone number SMS verification number login
  - View dish (and/or combo meal), select and put into shopping cart, checkout

## Demo Photos

- Server side:

    ![web_interface](/demo/web_interface.png)

    ![web_adddish](/demo/web_adddish.png)

- Client side:

    <img src="/demo/mobile_login.png" width=30% height=30%>

    <img src="/demo/mobile_interface.png" width=26.7% height=26.7%>
