package converter

fun main() {
    step1()
}

fun step1() {
    println("\nEnter two numbers in format: {source base} {target base} (To quit type /exit)")
    val action = readln()
    when  {
        action != "/exit" -> {
            val sourceTarget = action.split(" ")
            if(sourceTarget.count() == 2) {
                val source = sourceTarget[0].toInt()
                val target = sourceTarget[1].toInt()
                step2(source, target)
            }
        }
        else -> System.exit(0)
    }
}

fun step2(source: Int, target: Int) {

    println("Enter number in base $source to convert to base $target (To go back type /back)")
    val input = readln()
    var decimal = "0"
    var result: String = ""

    when (input) {
        "/back" -> step1()
        else -> {
            if (input.contains(".")) {
                val inputDec = input.split(".")
                val first = inputDec[0].toBigInteger(source).toString()

                val s = toDecimal(inputDec[1], source)
                val second = toTarget(s, target)
                result = first.toBigInteger().toString(target) + "." + second
            }
            else{
                decimal = input.toBigInteger(source).toString()
                result = decimal.toBigInteger().toString(target)
            }
        }
    }

    println("Conversion result: $result\n")
    step2(source, target)
}

fun toDecimal (fraction: String, base: Int): String {
    var sum = 0.0

    var exp = 1
    for (n in fraction) {
        val dec = "$n".toInt(base)
        val res = (dec * (1.toDouble() / (Math.pow(base.toDouble(), exp.toDouble()))))
        sum += res
        exp++
    }
    return sum.toString().replace("0.", "")
}


fun toTarget (input: String, target: Int): String {
    var str: String = ""
    var multiplicator = "0.$input"
    while (str.length < 5) {
        val result = (multiplicator.toDouble() * target).toString().split(".")
        str += result[0].toBigInteger().toString(target)
        multiplicator = "0." + result[1]
    }
    return str
}