package com.aaa.samplestore.data.remote.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class CurlLogInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Build the curl command
        val curlCommand = StringBuilder("curl -X ${request.method} '${request.url}'")
        request.headers.forEach { (name, value) ->
            curlCommand.append(" -H '$name: $value'")
        }

        // Add request body if available
        request.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            val requestBody = buffer.readUtf8()
            curlCommand.append(" -d '$requestBody'")
        }

        Log.d("CURL", curlCommand.toString()) // Logs the cURL command

        return chain.proceed(request)

    }
}