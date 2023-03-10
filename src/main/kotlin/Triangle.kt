class Triangle(private var sizeA: Double, private var sizeB: Double, private var sizeC: Double) {
    fun getTriangleType(): String {
        return when {
            this.sizeA == this.sizeB && this.sizeA == this.sizeC -> "Равносторонний";
            this.sizeA == this.sizeB || this.sizeB == this.sizeC || this.sizeC == this.sizeA -> "Равнобедренный";
            else -> "Обыкновенный";
        };
    }
}