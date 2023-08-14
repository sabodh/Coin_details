# Coin paprika - Android Application - Kotlin
Coin paprika is an Android application for listing the coins and it's details from the server.
## Architecture
This application follows basic SOLID Principles and clean code approach and also uses MVVM pattern.
## Modularization
This application follows modularization strategy know as "by layer"
## Features
A list of technologies/ features used within the project:
* [MVVM]()
* [Clean Code]()
* [Jetpack Compose]()
* [Compose Navigation Component]()
* [Compose Navigation Args]()
* [Lazy Column]()
* [Flow, StateFlow]()
* [Retrofit]()
* [Mockito]()
* [JUnit4]()
* [Coin paprika API service](https://api.coinpaprika.com/v1/coins)

## Functionality
1. When the app is started, load and display a list of coins.
2. Order entries by name.
3. Filter the list based on tags.
4. Display a divider between each entry.
5. Display any extra coin info in some kind of popup/fragment when a coin entry is clicked, based on the second API request, with the said ID as the query.
6. Provide some kind of refresh option that reloads the list.
7. Display an error message if the list cannot be loaded (e.g., no network).

# Prerequisites 
1. compiler sdk 33
2. java sdk 11
3. gradle 7.5



