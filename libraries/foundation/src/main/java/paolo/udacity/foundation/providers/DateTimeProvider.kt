package paolo.udacity.foundation.providers


interface DateTimeProvider<T> {
    fun now(): T
}
