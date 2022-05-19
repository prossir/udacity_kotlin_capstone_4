package paolo.udacity.foundation.mappers


abstract class SingleMapper<T1, T2>() {

    abstract fun map(value: T1): T2
    fun map(values: List<T1>): List<T2> = values.map(::map)

}