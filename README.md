# iSolutions Hotels

iSolutions Hotels is an open source launcher for Android TV applications built with Kotlin and Jetpack Compose. It serves as a launcher for hotel applications, providing a user-friendly interface for guests to access various hotel services.

## Dependencies

Backend : [https://github.com/manch93/isolutions-hotels-backend](https://github.com/manch93/isolutions-hotels-backend)

## Dependencies

The project uses the following major libraries and technologies:

*   [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   [Kotlin](https://kotlinlang.org/)
*   [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
*   [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
*   [Dagger Hilt](https://dagger.dev/hilt/)
*   [Retrofit](https://square.github.io/retrofit/)
*   [Room](https://developer.android.com/training/data-storage/room)
*   [Coil](https://coil-kt.github.io/coil/)

## Screenshoot
<div align="center">
    <img alt="App image" src="docs/screenshoot/screensaver.png" width="30%">
    <img alt="App image" src="docs/screenshoot/home.png" width="30%">
    <img alt="App image" src="docs/screenshoot/main-menu.png" width="30%">
    <img alt="App image" src="docs/screenshoot/content.png" width="30%">
    <img alt="App image" src="docs/screenshoot/detail-content.png" width="30%">
</div>

## Demo


<video
    src='https://github.com/user-attachments/assets/de92c8bd-e7a5-4851-9ba1-48ef249c6732'
    controls
    width="100%"
    alt="Demo video of LauncherCompose">
    Your browser does not support the video element. You can view the demo at our website.
</video>

## Telegram Group

You can join on telegram:

[https://t.me/karhundeveloper](https://t.me/karhundeveloper)

## Dependency graph
![Dependency graph](docs/images/graphs/dep_graph_app.svg)

## License

Distributed under the Apache License 2.0. See `LICENSE` for more information.

## Configuration

Open core/network/src/main/java/com/karuhun/core/network/interceptor/AuthInterceptor.kt. The interceptor currently hard‑codes a token in runBlocking
Replace the string inside runBlocking { "..." } with the token you generated in the back‑end for that hotel:

val token = runBlocking {
    "eyJpdiI6Ij...your-encrypted-token-here..."  // from Crypt::encrypt
}

Do not change the header name; the interceptor already adds X‑API‑KEY.