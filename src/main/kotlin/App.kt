fun main() {
    var triangle = Triangle(1.0, 2.0, 1.0);
    var circle = Circle(5.0, .0, .0);
    println(triangle.getTriangleType());
    println(circle.isPointInsideCircle(4.0, 1.0));
}