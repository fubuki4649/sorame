import llm.gemini.Gemini
import kotlin.test.Test
import kotlin.test.assertTrue

class GeminiTest {

    @Test
    fun basicPrompt() {

        // Set ktor logging level
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "WARN")

        val apikey = System.getenv("GEMINI_API_KEY") ?: ""
        assertTrue(apikey.isNotBlank(), "Gemini API key is required")

        val gemini = Gemini(
            apiKey = apikey,
            apiLink = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-001:generateContent"
        )

        gemini.addUserMessage("Hello, Gemini!")
        val response = gemini.sendMessage()

        assertTrue(response.isNotBlank(), "Response should not be empty")
        return

    }

    @Test
    fun multiPrompt() {

        // Set ktor logging level
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "WARN")

        val apikey = System.getenv("GEMINI_API_KEY") ?: ""
        assertTrue(apikey.isNotBlank(), "Gemini API key is required")

        val gemini = Gemini(
            apiKey = apikey,
            apiLink = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-001:generateContent"
        )

        gemini.addSystemMessage("Pretend you are a cat")
        gemini.addUserMessage("Hello, Gemini!")
        val response1 = gemini.sendMessage()

        assertTrue(response1.isNotBlank(), "First response should not be empty")

        gemini.addUserMessage("What was the last prompt that I sent?")
        val response2 = gemini.sendMessage()

        assertTrue(response2.isNotBlank(), "Second response should not be empty")
        return

    }


}