/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.core.network.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("MagicNumber")
class CurlLoggingInterceptor @Inject constructor(
    private val logger: HttpLoggingInterceptor.Logger
) :
    Interceptor {

    private var curlOptions: String? = null

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        logRequest(chain)
        return logResponse(chain)
    }

    private fun logRequest(chain: Interceptor.Chain) {
        val request = chain.request()

        var compressed = false

        var curlCmd = "curl"
        if (curlOptions != null) {
            curlCmd += " " + curlOptions!!
        }
        curlCmd += " -X " + request.method

        val headers = request.headers
        for (i in 0 until headers.size) {
            val name = headers.name(i)
            var value = headers.value(i)

            val start = 0
            val end = value.length - 1
            if (value[start] == '"' && value[end] == '"') {
                value = "\\\"" + value.substring(1, end) + "\\\""
            }

            if ("Accept-Encoding".equals(name, ignoreCase = true) && "gzip".equals(
                    value,
                    ignoreCase = true
                )
            ) {
                compressed = true
            }
            curlCmd += " -H \"$name: $value\""
        }

        request.body?.let {
            if (isExceptRequest(request).not()) {
                val buffer = Buffer().apply { it.writeTo(this) }
                val charset = it.contentType()?.charset(UTF8) ?: UTF8
                // try to keep to a single line and use a subshell to preserve any line breaks
                curlCmd += " --data $'" + buffer.readString(charset).replace("\n", "\\n") + "'"
            }
        }

        curlCmd += (if (compressed) " --compressed " else " ") + "\"${request.url}\""

        logger.log("╭--- cURL (" + request.url + ")")
        logger.log(curlCmd)
        logger.log("╰--- (copy and paste the above line to a terminal)")
    }

    private fun isExceptRequest(request: Request): Boolean {
        return EXCEPTED_BODY_LOGGING_REQUEST.any { getUrlPath(request).contains(it) }
    }

    private fun getUrlPath(request: Request): String {
        return request.url.toUrl().path
    }

    @Suppress("LongMethod", "NestedBlockDepth", "TooGenericExceptionCaught")
    private fun logResponse(chain: Interceptor.Chain): Response {

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(chain.request())
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e")
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
        val bodySize =
            if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        logger.log(
            "<-- "
                    + response.code
                    + (if (response.message.isEmpty()) "" else ' ' + response.message)
                    + ' '.toString() + response.request.url
                    + " (" + tookMs + "ms" + ", $bodySize body" + ')'.toString()
        )

        val headers = response.headers
        var i = 0
        val count = headers.size
        while (i < count) {
            logger.log(headers.name(i) + ": " + headers.value(i))
            i++
        }

        if (bodyHasUnknownEncoding(response.headers)) {
            logger.log("<-- END HTTP (encoded body omitted)")
        } else {
            val source = responseBody.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            var buffer = source.buffer

            var gzippedLength: Long? = null
            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                gzippedLength = buffer.size
                var gzippedResponseBody: GzipSource? = null
                try {
                    gzippedResponseBody = GzipSource(buffer.clone())
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                } finally {
                    gzippedResponseBody?.close()
                }
            }

            var charset: Charset? =
                UTF8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }

            if (!isPlaintext(buffer)) {
                logger.log("")
                logger.log("<-- END HTTP (binary " + buffer.size + "-byte body omitted)")
                return response
            }

            if (contentLength != 0L && isExceptResponse(response).not()) {
                logger.log("")
                logger.log(buffer.clone().readString(charset!!))
            }

            if (gzippedLength != null) {
                logger.log(
                    "<-- END HTTP (" + buffer.size + "-byte, "
                            + gzippedLength + "-gzipped-byte body)"
                )
            } else {
                logger.log("<-- END HTTP (" + buffer.size + "-byte body)")
            }
        }

        return response
    }

    private fun isExceptResponse(response: Response): Boolean {
        val url = response.request.url.toUrl().toString()
        return EXCEPTED_BODY_LOGGING_RESPONSE.any { url.contains(it) }
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }

    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return (contentEncoding != null
                && !contentEncoding.equals("identity", ignoreCase = true)
                && !contentEncoding.equals("gzip", ignoreCase = true))
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
        private val EXCEPTED_BODY_LOGGING_REQUEST = arrayListOf("petitions/attach_upload")
        private val EXCEPTED_BODY_LOGGING_RESPONSE = arrayListOf("delo.ru/documents")
    }

}