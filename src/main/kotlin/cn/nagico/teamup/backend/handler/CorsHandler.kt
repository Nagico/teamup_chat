package cn.nagico.teamup.backend.handler

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import org.springframework.stereotype.Component
import java.net.MalformedURLException
import java.net.URL


@Sharable
@Component
class CorsHandler : SimpleChannelInboundHandler<HttpObject>() {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: HttpObject) {
        val request = msg as FullHttpRequest
        if ("OPTIONS" == request.method().toString()) {
            var response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
            response = processResponse(request, response)
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        } else {
            ctx.fireChannelRead(msg.retain())
        }
    }

    private fun processResponse(request: FullHttpRequest, response: DefaultFullHttpResponse): DefaultFullHttpResponse {
        val origin = request.headers()["origin"] ?: return response

        try {
            val url = URL(origin)

            if (CORS_ALLOW_CREDENTIALS)
                response.headers()[ACCESS_CONTROL_ALLOW_CREDENTIALS] = "true"

            if (!originFoundInWhiteLists(origin, url))
                return response

            if (!CORS_ALLOW_CREDENTIALS)
                response.headers()[ACCESS_CONTROL_ALLOW_ORIGIN] = origin
            else
                response.headers()[ACCESS_CONTROL_ALLOW_ORIGIN] = "*"

            if (CORS_EXPOSE_HEADERS.isNotEmpty())
                response.headers()[ACCESS_CONTROL_EXPOSE_HEADERS] = CORS_EXPOSE_HEADERS.joinToString(",")

            response.headers()[ACCESS_CONTROL_ALLOW_METHODS] = CORS_ALLOW_METHODS.joinToString(",")
            response.headers()[ACCESS_CONTROL_ALLOW_HEADERS] = CORS_ALLOW_HEADERS.joinToString(",")
            CORS_PREFLIGHT_MAX_AGE?.run {
                response.headers()[ACCESS_CONTROL_MAX_AGE] = this.toString()
            }

            return response
        } catch (e: MalformedURLException) {
            return response
        }
    }

    private fun originFoundInWhiteLists(origin: String, url: URL): Boolean {
        return (origin == "null" && origin in CORS_ALLOWED_ORIGINS) || urlInWhiteLists(url) || regexDomainMatch(origin)

    }

    private fun urlInWhiteLists(url: URL): Boolean {
        val origins = CORS_ALLOWED_ORIGINS.map { URL(it) }
        return origins.any { it.host == url.host && it.protocol == url.protocol }
    }

    private fun regexDomainMatch(origin: String): Boolean {
        return CORS_ALLOWED_ORIGIN_REGEXES.any { origin.matches(it.toRegex()) }
    }

    companion object {
        private const val ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin"
        private const val ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers"
        private const val ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials"
        private const val ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers"
        private const val ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods"
        private const val ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age"

        private const val CORS_ALLOW_CREDENTIALS = true
        private val CORS_ALLOWED_ORIGINS = arrayOf(
            "http://localhost",
            "http://127.0.0.1"
        )
        private val CORS_ALLOWED_ORIGIN_REGEXES = emptyArray<String>()
        private val CORS_EXPOSE_HEADERS = emptyArray<String>()
        private val CORS_PREFLIGHT_MAX_AGE: Int? = null
        private val CORS_ALLOW_METHODS = arrayOf("DELETE", "GET", "OPTIONS", "PATCH", "POST", "PUT")
        private val CORS_ALLOW_HEADERS = arrayOf(
            "accept",
            "accept-encoding",
            "authorization",
            "content-type",
            "dnt",
            "origin",
            "user-agent",
            "x-csrftoken",
            "x-requested-with",
            "baggage",
            "sentry-trace",
            )
    }
}

