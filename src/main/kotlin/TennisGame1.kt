class TennisGame1(private val player1Name: String, private val player2Name: String) : TennisGame {

    private var scorePlayer1: Int = 0
    private var scorePLayer2: Int = 0

    override fun wonPoint(playerName: String) {
        if (playerName == player1Name)
            scorePlayer1 += 1
        else if (playerName == player2Name)
            scorePLayer2 += 1
    }

    override fun getScore(): String {
        var scoreString = ""
        var tempScore = 0
        if (isTie()) {
            scoreString = pointsToDrawString(scorePlayer1)
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
            for (i in 1..2) {
                if (i == 1)
                    tempScore = scorePlayer1
                else {
                    scoreString += "-"
                    tempScore = scorePLayer2
                }
                when (tempScore) {
                    0 -> scoreString += "Love"
                    1 -> scoreString += "Fifteen"
                    2 -> scoreString += "Thirty"
                    3 -> scoreString += "Forty"
                }
            }
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


    private fun pointsToDrawString(points: Int) = when {
        points < 3 -> "${pointsToString(points)}-All"
        else -> "Deuce"
    }

    private fun isTie() = scorePlayer1 == scorePLayer2
}
