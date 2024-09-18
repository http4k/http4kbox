package http4kbox

import dev.forkhandles.values.NonBlankStringValueFactory
import dev.forkhandles.values.StringValue

class FileName private constructor(value: String) : StringValue(value) {
    companion object : NonBlankStringValueFactory<FileName>(::FileName)
}