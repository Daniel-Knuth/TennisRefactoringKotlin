class TennisGame1(private val player1Name: String, private val player2Name: String) : TennisGame {

    private var scorePlayer1: Int = 0
    private var scorePLayer2: Int = 0

    private val player1: Player = Player(name = player1Name)
    private val player2: Player = Player(name = player2Name)

    override fun wonPoint(playerName: String) {
        if (playerName == player1.name) {
            scorePlayer1 += 1
            player1.points += 1
        } else if (playerName == player2.name) {
            scorePLayer2 += 1
            player2.points += 1
        }

    }

    override fun getScore(): String {
        var scoreString = ""
        if (isTie()) {
            scoreString = pointsToTieString(scorePlayer1)
        } else if (scorePlayer1 >= 4 || scorePLayer2 >= 4) {
            val minusResult = scorePlayer1 - scorePLayer2
            scoreString = if (minusResult == 1)
                "Advantage player1"
            else if (minusResult == -1)
                "Advantage player2"
            else if (minusResult >= 2)
                "Win for player1"
            else
                "Win for player2"
        } else {
            scoreString = "${pointsToString(scorePlayer1)}-${pointsToString(scorePLayer2)}"
        }
        return scoreString
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

    private fun isTie() = scorePlayer1 == scorePLayer2
}
