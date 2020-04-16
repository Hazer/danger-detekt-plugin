package io.vithor.danger.plugins.detekt

sealed class SeverityLevel {
    object Warning : SeverityLevel()
    object Error : SeverityLevel()
    object Info : SeverityLevel()
    data class Unknown(val raw: String) : SeverityLevel()
}