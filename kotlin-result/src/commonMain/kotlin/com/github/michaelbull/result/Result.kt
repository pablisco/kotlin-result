package com.github.michaelbull.result

import kotlin.jvm.JvmInline

/**
 * [Result] is a type that represents either success ([Ok]) or failure ([Err]).
 *
 * - Elm: [Result](http://package.elm-lang.org/packages/elm-lang/core/5.1.1/Result)
 * - Haskell: [Data.Either](https://hackage.haskell.org/package/base-4.10.0.0/docs/Data-Either.html)
 * - Rust: [Result](https://doc.rust-lang.org/std/result/enum.Result.html)
 */
public sealed interface Result<out V, out E> {

    public operator fun component1(): V?
    public operator fun component2(): E?

    public companion object {

        /**
         * Invokes a [function] and wraps it in a [Result], returning an [Err]
         * if an [Exception] was thrown, otherwise [Ok].
         */
        @Deprecated("Use top-level runCatching instead", ReplaceWith("runCatching(function)"))
        public inline fun <V> of(function: () -> V): Result<V, Exception> {
            return try {
                Ok(function.invoke())
            } catch (ex: Exception) {
                Err(ex)
            }
        }
    }
}

/**
 * Represents a successful [Result], containing a [value].
 */
public interface Ok<out V> : Result<V, Nothing> {
    public val value: V
}

/**
 * Constructor function for [Ok] as a success representation of [Result]
 */
@Suppress("FunctionName")
public fun <V> Ok(value: V): Ok<V> = InternalOk(value)

@JvmInline
private value class InternalOk<out V>(private val raw: Any?) : Ok<V> {

    override fun component1(): V? = value
    override fun component2(): Nothing? = null

    @Suppress("UNCHECKED_CAST")
    override val value: V
        get() = raw as V

    override fun toString(): String = "Ok($value)"
}

/**
 * Represents a failed [Result], containing an [error].
 */
public interface Err<out E> : Result<Nothing, E> {
    public val error: E
}

/**
 * Constructor function for [Err] as a failure representation of [Result]
 */
@Suppress("FunctionName")
public fun <E> Err(error: E): Err<E> = InternalErr(error)

@JvmInline
private value class InternalErr<out E>(private val raw: Any?) : Err<E> {

    override fun component1(): Nothing? = null
    override fun component2(): E? = error

    @Suppress("UNCHECKED_CAST")
    override val error: E
        get() = raw as E

    override fun toString(): String = "Err($error)"
}
