# Danger Detekt Plugin 

## Setup

Install and run [Danger Kotlin] as normal and in your `Dangerfile.df.kts` add the following dependency:
```kotlin
@file:DependsOn("io.vithor.danger.plugins:detekt-plugin:0.0.5")
```
Then register your plugin before the `danger` initialisation and use the plugin:
```kotlin
import io.vithor.danger.plugins.detekt.Detekt

register plugin Detekt

val danger = Danger(args)

Detekt.report {
    path = "/path/to/the/detekt/reports/"
}
```