class Player(
    var points: Int = 0,
    val name: String
) {

    fun hasBeaten(other: Player) = this.points >= 4 && this.points - other.points >= 2
    fun hasAdvantageOver(other: Player) = this.points >= 4 && this.points - other.points == 1

}