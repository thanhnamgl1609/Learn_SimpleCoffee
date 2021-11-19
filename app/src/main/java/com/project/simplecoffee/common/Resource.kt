package com.project.simplecoffee.common


sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class OnSuccess<T>(data: T?) : Resource<T>(data)
    class OnFailure<T>(data: T?, message: String?) : Resource<T>(data, message)
}
