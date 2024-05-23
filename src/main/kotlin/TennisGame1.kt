class TennisGame1(serverName: String, receiverName: String) : TennisGame {

    internal val server = Player(serverName)
    internal val receiver = Player(receiverName)

    override fun wonPoint(playerName: String) {
        if (server.name == playerName) server.winPoint() else receiver.winPoint()
    }

    override fun getScore(): String {
        val provider = DeuceNew(this)
            .or(GameWonNew(this))
            .or(AdvantageNew(this))
            .or(DefaultResult(this))


        val result: TennisResult = Deuce(
            this, GameWon(
                this, Advantage(
                    this, DefaultResult(this)
                )
            )
        ).result
        return result.format()
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
}

internal class TennisResultNew(private val game: TennisGame1) {
    fun format() =
        when {
            game.isDeuce() -> "Deuce"
            game.receiverHasWon() -> "Win for ${game.receiver.name}"
            game.serverHasWon() -> "Win for ${game.server.name}"
            game.receiverHasAdvantage() -> "Advantage ${game.receiver.name}"
            game.serverHasAdvantage() -> "Advantage ${game.server.name}"
            else -> "${pointsToScore(game.server.points)}-${pointsToScore(game.receiver.points)}"
        }

    private fun pointsToScore(points: Int) = when (points) {
        0 -> "Love"
        1 -> "Fifteen"
        2 -> "Thirty"
        3 -> "Forty"
        else -> ""
    }
}

internal interface ResultProviderNew {
    val result: String
    fun check(nextProvider: ResultProviderNew): ResultProviderNew =
        if (result.isBlank()) nextProvider else this
}


internal interface ResultProvider {
    val result: TennisResult
    fun or(nextProvider: ResultProvider): ResultProvider
}


internal class DeuceNew(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.isDeuce()) TennisResult("Deuce", "") else TennisResult("", "")

    override fun or(nextProvider: ResultProvider): ResultProvider =
        if (result.format().isBlank()) nextProvider else this
}

internal class Deuce(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.isDeuce()) TennisResult("Deuce", "") else nextResult.result

    override fun or(nextProvider: ResultProvider): ResultProvider {
        TODO("Not yet implemented")
    }

}

internal class GameWonNew(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.wasWon()) TennisResult("Win for " + game.winner()!!.name, "") else TennisResult("", "")

    override fun or(nextProvider: ResultProvider): ResultProvider =
        if (result.format().isBlank()) nextProvider else this
}

internal class GameWon(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.wasWon()) TennisResult("Win for " + game.winner()!!.name, "") else TennisResult("", "")

    override fun or(nextProvider: ResultProvider): ResultProvider {
        TODO("Not yet implemented")
    }
}

internal class AdvantageNew(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = if (game.hasAdvantageOwner()) TennisResult(
            "Advantage " + game.advantageOwner()!!.name,
            ""
        ) else TennisResult("", "")

    override fun or(nextProvider: ResultProvider): ResultProvider =
        if (result.format().isBlank()) nextProvider else this
}

internal class Advantage(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.hasAdvantageOwner()) TennisResult(
            "Advantage " + game.advantageOwner()!!.name,
            ""
        ) else nextResult.result

    override fun or(nextProvider: ResultProvider): ResultProvider {
        TODO("Not yet implemented")
    }
}


internal class DefaultResult(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = TennisResult(
            scores[game.server.points], scores[game.receiver.points]
        )

    override fun or(nextProvider: ResultProvider): ResultProvider {
        TODO("Not yet implemented")
    }

    internal companion object {
        private val scores = arrayOf("Love", "Fifteen", "Thirty", "Forty")
    }
}
