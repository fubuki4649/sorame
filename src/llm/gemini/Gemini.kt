package llm.gemini

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import llm.LanguageModel
import llm.gemini.Content.Role


class Gemini(override val apiKey: String, override val apiLink: String) : LanguageModel {

    private val contentsContainer = ContentsContainer()

    // Private methods

    private fun addPart(part: Part, role: Role) {
        if(contentsContainer.lastOrNull()?.role != role) contentsContainer.add(Content(role))
        contentsContainer.last().parts.add(part)
    }

    // Public methods

    override fun addSystemMessage(msg: String) {
        addPart(Part(msg), Role.USER)
    }

    override fun addUserMessage(msg: String) {
        addPart(Part(msg), Role.USER)
    }

    override fun sendMessage(): String {

        val client = HttpClient()

        val rawResponse: String
        runBlocking {

            val httpResponse: HttpResponse = client.post(apiLink) {
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                }
                parameter("key", apiKey)
                setBody(Json.encodeToString(contentsContainer))
            }

            rawResponse = httpResponse.bodyAsText()
            client.close()

        }

        // Extract the "PARTS" from the response
        val partsList = mutableListOf<String>()

        val candidates = Json.parseToJsonElement(rawResponse).jsonObject["candidates"]?.jsonArray ?: emptyList()
        for (candidate in candidates) {
            val parts = candidate.jsonObject["content"]?.jsonObject?.get("parts")?.jsonArray ?: emptyList()
            for (part in parts) {
                println(part.toString())
                partsList.add(part.toString())
            }
        }

        // Add the "PARTS" into chat history
        partsList.forEach {
            addPart(Part(it), Role.MODEL)
        }

        return partsList.joinToString("\n")

    }

    override fun toString(): String {

        val json = Json { prettyPrint = true }
        return json.encodeToString(contentsContainer)

    }

}