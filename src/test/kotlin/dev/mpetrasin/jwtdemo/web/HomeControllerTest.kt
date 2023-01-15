package dev.mpetrasin.jwtdemo.web

import dev.mpetrasin.jwtdemo.config.SecurityConfig
import dev.mpetrasin.jwtdemo.service.TokenService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HomeController::class, AuthController::class)
@Import(SecurityConfig::class, TokenService::class)
internal class HomeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `home when unauthorized should return 401`() {
        mockMvc
            .perform(get("/"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser
    fun `home when mock user should return 200`() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
    }

    @Test
    fun `home when authenticated should say hello user`() {
        mockMvc.perform(get("/").header("Authorization", "Bearer ${token()}"))
            .andExpect(status().isOk)
            .andExpect(content().string("Hello, user!"))
    }

    private fun token() =
        mockMvc.perform(
            post("/token")
                .with(httpBasic("user", "password"))
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString
}