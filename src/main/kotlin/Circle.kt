import kotlin.math.pow
import kotlin.math.sqrt

class Circle(private var radius: Double, private var circleX: Double, private var circleY: Double) {

    fun isPointInsideCircle(x: Double, y: Double): Boolean {
        val distance = sqrt((x - this.circleX).pow(2) + (y - this.circleY).pow(2));
        return distance <= radius;
    }
}