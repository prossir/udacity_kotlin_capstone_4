package paolo.udacity.foundation.providers


interface DateTimeProvider<out T> {
    fun now(): T
}
