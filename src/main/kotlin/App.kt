fun main() {
    var sizeA: Double;
    var sizeB: Double;
    var sizeC: Double;
    println("Введите стороны треугольника");
    sizeA = readLine()!!.toDouble();
    sizeB = readLine()!!.toDouble();
    sizeC = readLine()!!.toDouble();
    var triangle = Triangle(sizeA, sizeB, sizeC);
    println(triangle.getTriangleType());

    var radius: Double;
    var centerX: Double;
    var centerY: Double;
    var x0: Double;
    var y0: Double;
    println("Введите радиус и координату центра окружности X и Y");
    radius = readLine()!!.toDouble();
    centerX = readLine()!!.toDouble();
    centerY = readLine()!!.toDouble();
    var circle = Circle(radius, centerX, centerY);
    println("Введите координаты точки, которую вы хотите проверить на принадлежность к окружности");
    x0 = readLine()!!.toDouble();
    y0 = readLine()!!.toDouble();
    println(circle.isPointInsideCircle(x0, y0));
}