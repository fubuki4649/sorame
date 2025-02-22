package llm

interface LanguageModel {

    val apiKey: String
    val apiLink: String

    fun addSystemMessage(msg: String)

    fun addUserMessage(msg: String)

    fun sendMessage(): String

    override fun toString(): String

}