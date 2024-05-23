class Player(
    val name: String
) {
    var points: Int = 0
        private set
    fun winPoint() = run { this.points +=1 }
    fun hasBeaten(other: Player) = this.points >= 4 && this.points - other.points >= 2
    fun hasAdvantageOver(other: Player) = this.points >= 4 && this.points - other.points == 1

}