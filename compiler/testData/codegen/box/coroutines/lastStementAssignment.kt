// WITH_RUNTIME
// WITH_COROUTINES
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*

suspend fun suspendHere(): String = suspendCoroutineOrReturn { x ->
    x.resume("OK")
    SUSPENDED_MARKER
}

fun builder(c: suspend () -> Unit) {
    var wasHandleResultCalled = false
    c.startCoroutine(handleResultContinuation {
        wasHandleResultCalled = true
    })

    if (!wasHandleResultCalled) throw RuntimeException("fail 1")
}

var varWithCustomSetter: String = ""
    set(value) {
        if (field != "") throw RuntimeException("fail 2")
        field = value
    }

fun box(): String {
    var result = ""

    builder {
        result += "O"

        if (suspendHere() != "OK") throw RuntimeException("fail 3")

        result += "K"
    }

    if (result != "OK") return "fail 4"

    builder {
        if (suspendHere() != "OK") throw RuntimeException("fail 5")

        varWithCustomSetter = "OK"
    }

    return varWithCustomSetter
}
