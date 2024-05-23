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
        var score = ""
        var tempScore = 0
        if (isDraw()) {
            score = when (scorePlayer1) {
                0 -> "Love-All"
                1 -> "Fifteen-All"
                2 -> "Thirty-All"
                else -> "Deuce"
            }
        } else if (scorePlayer1 >= 4 || scorePLayer2 >= 4) {
            val minusResult = scorePlayer1 - scorePLayer2
            score = if (minusResult == 1)
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
                    score += "-"
                    tempScore = scorePLayer2
                }
                when (tempScore) {
                    0 -> score += "Love"
                    1 -> score += "Fifteen"
                    2 -> score += "Thirty"
                    3 -> score += "Forty"
                }
            }
        }
        return score
    }

    private fun isDraw() = scorePlayer1 == scorePLayer2
}
