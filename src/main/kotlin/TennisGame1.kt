class TennisGame1(serverName: String, receiverName: String) : TennisGame {

    internal val server = Player(serverName)
    internal val receiver = Player(receiverName)

    override fun wonPoint(playerName: String) {
        if (server.name == playerName) server.winPoint() else receiver.winPoint()
    }

    override fun getScore(): String {
        val provider = Deuce(this)
            .orElse(GameWon(this))
            .orElse(Advantage(this))
            .orElse(DefaultResult(this))

        return provider.result.format()
    }

    internal fun wasWon() = receiverHasWon() || serverHasWon()
    internal fun winner() = if (serverHasWon()) server else if (receiverHasWon()) receiver else null

    internal fun hasAdvantageOwner() = receiverHasAdvantage() || serverHasAdvantage()
    internal fun advantageOwner() = if (serverHasAdvantage()) server else if (receiverHasAdvantage()) receiver else null

    internal fun receiverHasAdvantage() = receiver.hasAdvantageOver(server)

    internal fun serverHasAdvantage() = server.hasAdvantageOver(receiver)

    internal fun receiverHasWon() = receiver.hasBeaten(server)

    internal fun serverHasWon() = server.hasBeaten(receiver)

    internal fun isDeuce() = server.points >= 3 && receiver.points >= 3 && server.points == receiver.points
}


internal class TennisResult(var serverScore: String, var receiverScore: String) {
    fun format(): String {
        if ("" == receiverScore) return serverScore
        return if (serverScore == receiverScore) "$serverScore-All" else serverScore + "-" + receiverScore
    }

    fun isValid() = this != invalidResult

    companion object {
        val invalidResult = TennisResult("", "")
    }
}

internal interface ResultProvider {
    val result: TennisResult
    fun orElse(nextProvider: ResultProvider): ResultProvider
}

internal interface ResultProviderNew {
    val result: TennisResult
    fun providesValidResult(): Boolean
    fun or(nextProvider: ((TennisGame) -> ResultProviderNew)): ResultProviderNew
}


internal class Deuce(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.isDeuce()) TennisResult("Deuce", "") else TennisResult("", "")

    override fun orElse(nextProvider: ResultProvider): ResultProvider =
        if (result.format().isBlank()) nextProvider else this
}

internal class GameWon(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.wasWon()) TennisResult("Win for " + game.winner()!!.name, "") else TennisResult("", "")

    override fun orElse(nextProvider: ResultProvider): ResultProvider =
        if (result.format().isBlank()) nextProvider else this
}


internal class Advantage(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.hasAdvantageOwner()) TennisResult(
            "Advantage " + game.advantageOwner()!!.name,
            ""
        ) else TennisResult("", "")

    override fun orElse(nextProvider: ResultProvider): ResultProvider =
        if (result.format().isBlank()) nextProvider else this
}


internal class DefaultResult(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = TennisResult(
            scores[game.server.points], scores[game.receiver.points]
        )

    override fun orElse(nextProvider: ResultProvider): ResultProvider {
        TODO("Not yet implemented")
    }

    internal companion object {
        private val scores = arrayOf("Love", "Fifteen", "Thirty", "Forty")
    }
}
