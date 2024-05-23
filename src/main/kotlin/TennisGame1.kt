class TennisGame1(val serverName: String, val receiverName: String) : TennisGame {

    internal var serverScore = 0
    internal var receiverScore = 0
    internal val server = Player(serverName)
    internal val receiver = Player(receiverName)

    override fun wonPoint(playerName: String) {
        if (serverName == playerName) serverScore += 1 else receiverScore += 1
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

    internal fun receiverHasAdvantage(): Boolean {
        return receiverScore >= 4 && receiverScore - serverScore == 1
    }

    internal fun serverHasAdvantage(): Boolean {
        return serverScore >= 4 && serverScore - receiverScore == 1
    }

    internal fun receiverHasWon(): Boolean {
        return receiverScore >= 4 && receiverScore - serverScore >= 2
    }

    internal fun serverHasWon(): Boolean {
        return serverScore >= 4 && serverScore - receiverScore >= 2
    }

    internal fun isDeuce(): Boolean {
        return serverScore >= 3 && receiverScore >= 3 && serverScore == receiverScore
    }
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
        get() = if (game.serverHasWon()) TennisResult("Win for " + game.serverName, "") else nextResult.result

}

internal class GameReceiver(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.receiverHasWon()) TennisResult("Win for " + game.receiverName, "") else nextResult.result

}

internal class AdvantageServer(private val game: TennisGame1, private val nextResult: ResultProvider) : ResultProvider {
    override val result: TennisResult
        get() = if (game.serverHasAdvantage()) TennisResult("Advantage " + game.serverName, "") else nextResult.result

}

internal class AdvantageReceiver(private val game: TennisGame1, private val nextResult: ResultProvider) :
    ResultProvider {
    override val result: TennisResult
        get() = if (game.receiverHasAdvantage()) TennisResult("Advantage " + game.receiverName, "") else nextResult.result

}

internal class DefaultResult(private val game: TennisGame1) : ResultProvider {
    override val result: TennisResult
        get() = TennisResult(
            scores[game.serverScore], scores[game.receiverScore]
        )

    internal companion object {
        private val scores = arrayOf("Love", "Fifteen", "Thirty", "Forty")
    }
}
