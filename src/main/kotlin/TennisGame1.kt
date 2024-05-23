class TennisGame1(private val player1Name: String, private val player2Name: String) : TennisGame {

    private val player1: Player = Player(name = player1Name)
    private val player2: Player = Player(name = player2Name)

    override fun wonPoint(playerName: String) {
        listOf(player1, player2)
            .first() { it.name == playerName }.points += 1
    }

    override fun getScore(): String {
        var scoreString = ""
        if (isTie()) {
            scoreString = getScoreNew()
        } else if (player1.points >= 4 || player2.points >= 4) {
            val minusResult = player1.points - player2.points
            scoreString = if (minusResult == 1)
                "Advantage player1"
            else if (minusResult == -1)
                "Advantage player2"
            else if (minusResult >= 2)
                "Win for player1"
            else
                "Win for player2"
        } else {
            scoreString = "${pointsToString(player1.points)}-${pointsToString(player2.points)}"
        }
        return scoreString
    }

    fun getScoreNew(): String = when {
        isTie() -> pointsToTieString(player1.points)
        player1.hasWon(player2) -> "Win for player1"
        player1.hasAdvantage(player2) -> "Advantage player1"
        else -> ""
    }


    private fun pointsToString(points: Int) = when (points) {
        0 -> "Love"
        1 -> "Fifteen"
        2 -> "Thirty"
        3 -> "Forty"
        else -> ""
    }

    private fun pointsToTieString(points: Int) = when {
        points < 3 -> "${pointsToString(points)}-All"
        else -> "Deuce"
    }

    private fun isTie() = player1.points == player2.points
}
