# Blum
Blum is an unofficial, simple, fast Twitter client written in Kotlin.   
This project is a complete rewrite of the [Java version](https://github.com/ziggy42/Blum).  

## Screenshot

<img src='screenshots/screenshot_1.png' width="248"/>

## Build
To build this project you have to create a [twitter app](https://apps.twitter.com/) and a file named `gradle.properties` in the root of the project.   
The file must contain the following definitions:
```
CallBackUrl="<YOUR CALLBACK URL>"
ConsumerKey="<YOUR CONSUMER KEY>"
ConsumerSecret="<YOUR CONSUMER SECRET>"
DebugConsumerKey="<YOUR CONSUMER KEY>"
DebugConsumerSecret="<YOUR CONSUMER SECRET>"

LicensesUrl="<THIS CAN BE EMPTY>"
```