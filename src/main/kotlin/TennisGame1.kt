class TennisGame1(private val player1Name: String, private val player2Name: String) : TennisGame {

    private val player1: Player = Player(name = player1Name)
    private val player2: Player = Player(name = player2Name)

    override fun wonPoint(playerName: String) {
        listOf(player1, player2)
            .first() { it.name == playerName }.points += 1
    }

    override fun getScore(): String = when {
        isTie() -> pointsToTieString(player1.points)
        hasWinner() -> "Win for ${winner()?.name}"
        hasAdvantageOwner() -> "Advantage ${advantageOwner()?.name}"
        else -> "${pointsToString(player1.points)}-${pointsToString(player2.points)}"
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

    private fun hasWinner() = winner() != null

    private fun winner(): Player? = when {
        (player1.hasBeaten(player2)) -> player1
        (player2.hasBeaten(player1)) -> player2
        else -> null
    }

    private fun hasAdvantageOwner() = advantageOwner() != null

    private fun advantageOwner() = when {
        player1.hasAdvantageOver(player2) -> player1
        player2.hasAdvantageOver(player1) -> player2
        else -> null
    }
}
