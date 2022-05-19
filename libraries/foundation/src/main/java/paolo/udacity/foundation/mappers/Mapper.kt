package paolo.udacity.foundation.mappers

abstract class Mapper<T1, T2>: SingleMapper<T1, T2>() {

    abstract fun reverseMap(value: T2): T1
    fun reverseMap(values: List<T2>): List<T1> = values.map { reverseMap(it) }

}