import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TempController(private val passwordEncoder: PasswordEncoder) {
    @GetMapping("/generate-password")
    fun generatePassword(@RequestParam password: String): String {
        return passwordEncoder.encode(password)
    }
}