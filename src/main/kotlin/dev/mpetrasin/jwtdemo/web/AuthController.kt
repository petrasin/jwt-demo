package dev.mpetrasin.jwtdemo.web

import dev.mpetrasin.jwtdemo.service.TokenService
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val tokenService: TokenService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/token")
    fun token(authentication: Authentication): String {
        log.debug("Token requested for user '{}'", authentication.name)
        val token = tokenService.generateToken(authentication)
        log.debug("Token granted {}", token)
        return token
    }
}