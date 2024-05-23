class Player(
    var points: Int = 0,
    val name: String
) {

    fun hasWon(other: Player) = this.points >= 4 && this.points - other.points >= 2
    fun hasAdvantage(other: Player) = this.points >= 4 && this.points - other.points < 2

}