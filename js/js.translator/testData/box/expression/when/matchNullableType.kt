package foo

class A() {

}

fun box(): String {
    var a: A? = null
    when(a) {
        is A? -> return "OK"
        else -> return "fail"
    }
}