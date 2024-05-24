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

    private fun receiverHasAdvantage() = receiver.hasAdvantageOver(server)

    private fun serverHasAdvantage() = server.hasAdvantageOver(receiver)

    private fun receiverHasWon() = receiver.hasBeaten(server)

    private fun serverHasWon() = server.hasBeaten(receiver)

    internal fun isDeuce() = server.points >= 3 && receiver.points >= 3 && server.points == receiver.points
}


internal class TennisResult(private var serverScore: String, private var receiverScore: String = "") {
    fun format(): String {
        if ("" == receiverScore) return serverScore
        return if (serverScore == receiverScore) "$serverScore-All" else "$serverScore-$receiverScore"
    }

    fun isValid() = this != invalidResult

    companion object {
        val invalidResult = TennisResult("invalid", "invalid")
    }
}

internal interface ResultProvider {
    val result: TennisResult
    fun providesValidResult(): Boolean
    fun orElse(nextProvider: ResultProvider): ResultProvider =
        if (providesValidResult()) this else nextProvider
}

internal class Deuce(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.isDeuce()) TennisResult("Deuce") else TennisResult.invalidResult

    override fun providesValidResult() = result.isValid()
}

internal class GameWon(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.wasWon()) TennisResult("Win for " + game.winner()!!.name)
        else TennisResult.invalidResult

    override fun providesValidResult() = result.isValid()
}


internal class Advantage(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.hasAdvantageOwner()) TennisResult(
            "Advantage " + game.advantageOwner()!!.name
        )
        else TennisResult.invalidResult

    override fun providesValidResult() = result.isValid()
}


internal class DefaultResult(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = TennisResult(
            scores[game.server.points], scores[game.receiver.points]
        )

    override fun providesValidResult() = result.isValid()

    internal companion object {
        private val scores = arrayOf("Love", "Fifteen", "Thirty", "Forty")
    }
}
