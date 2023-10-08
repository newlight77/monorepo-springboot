package io.oneprofile.shared.util.json


class Pipe<out A>(val v: A) {
    inline infix fun <B> pipe (function: (A) -> B) = Pipe(function(v))
    inline infix fun <T, R> T.into(other: (T) -> R) = Pipe(other(this))

    fun sum (x: Int, y: Int) : Int = x + y
    fun add(x: Int): Int = x + 1
    fun mult(x: Int): Int = x * 12
    fun dbl (x: Int): Int = x * 2
    fun trpl (x: Int): Int = x * 3

    fun main(args: Array<String>) {
        5 into ::add pipe ::mult pipe ::dbl pipe ::trpl pipe ::println
    }
}

class Pipe2 {
    infix fun <T, R> T.into(func: (T) -> R) = func(this)

    fun sum (x: Int, y: Int) : Int = x + y
    fun add(x: Int): Int = x + 1
    fun mult(x: Int): Int = x * 12
    fun dbl (x: Int): Int = x * 2
    fun trpl (x: Int): Int = x * 3

    fun main(args: Array<String>) {
        5 into ::add into ::mult into ::dbl into ::trpl into ::println
    }
}


class Pipe3 {
    infix fun <T, R> T.next(map : (T) -> R) : R = map(this)
    infix fun <T, R> T.into(func: (T) -> R) = func(this)

    fun sum (x: Int, y: Int) : Int = x + y
    fun add(x: Int): Int = x + 1
    fun mult(x: Int): Int = x * 12
    fun dbl (x: Int): Int = x * 2
    fun trpl (x: Int): Int = x * 3

    fun main(args: Array<String>) {
        sum(3,6) .let (::add) .let (::mult) .let (::dbl) .let (::trpl)

        sum(3,6) next ::add next ::mult next ::dbl next ::trpl

        sum(3,6) into (::add) into (::mult) into (::dbl) into (::trpl)

        sum(3,6) .run (::add) .run (::mult) .run (::dbl) .run (::trpl)
    }
}

class Pipe4 {
    fun sum (x: Int, y: Int) : Int = x + y
    fun add(x: Int): Int = x + 1
    fun mult(x: Int): Int = x * 12
    fun dbl (x: Int): Int = x * 2
    fun trpl (x: Int): Int = x * 3

    infix fun <A, B, C> ((A) -> B).then(other: (B) -> C): (A) -> C = { other.invoke(this.invoke(it)) }
    infix fun <T, R> T.into(func: (T) -> R) = func(this)

    val pipeline = ::add then ::mult then ::dbl then ::trpl then ::println

    fun main(args: Array<String>) {
        sum(1,2) into pipeline
    }
}

class Pipe5 {
    fun <T> T.pipe(vararg functions: (T) -> T): T = functions.fold(this) { value, f -> f(value) }

    fun sum (x: Int, y: Int) : Int = x + y
    fun add(x: Int): Int = x + 1
    fun mult(x: Int): Int = x * 12
    fun dbl (x: Int): Int = x * 2
    fun trpl (x: Int): Int = x * 3

    fun main(args: Array<String>) {
        9.pipe(
            ::add,
            ::mult,
            ::dbl,
            ::trpl
        )
    }
}
