package com.project.simplecoffee.common


sealed class Resource<T>(val data: T? = null) {
    class OnSuccess<T>(data: T?) : Resource<T>(data)
    class OnFailure<T>(data: T?, messenger: String) : Resource<T>(data)
}
