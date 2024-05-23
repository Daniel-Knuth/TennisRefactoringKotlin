class TennisGame1(serverName: String, receiverName: String) : TennisGame {

    internal val server = Player(serverName)
    internal val receiver = Player(receiverName)

    override fun wonPoint(playerName: String) {
        if (server.name == playerName) server.winPoint() else receiver.winPoint()
    }

    override fun getScore(): String {
        val result: TennisResult = Deuce(
            this, GameServer(
                this, GameReceiver(
                    this, AdvantageServer(
                        this, AdvantageReceiver(
                            this, DefaultResult(this)
                        )
                    )
                )
            )
        ).result
        return result.format()
    }

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

internal interface ResultProvider {
    val result: TennisResult
}

internal class Deuce(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.isDeuce()) TennisResult("Deuce", "") else nextResult.result

}

internal class GameServer(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.serverHasWon()) TennisResult("Win for " + game.server.name, "") else nextResult.result

}

internal class GameReceiver(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.receiverHasWon()) TennisResult("Win for " + game.receiver.name, "") else nextResult.result

}

internal class AdvantageServer(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.serverHasAdvantage()) TennisResult("Advantage " + game.server.name, "") else nextResult.result

}

internal class AdvantageReceiver(private val game: TennisGame1, private val nextResult: ResultProvider) :
    ResultProvider {
    override val result: TennisResult
        get() = if (game.receiverHasAdvantage()) TennisResult(
            "Advantage " + game.receiver.name,
            ""
        ) else nextResult.result

}

internal class DefaultResult(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = TennisResult(
            scores[game.server.points], scores[game.receiver.points]
        )

    internal companion object {
        private val scores = arrayOf("Love", "Fifteen", "Thirty", "Forty")
    }
}
