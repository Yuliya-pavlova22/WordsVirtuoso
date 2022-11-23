package wordsvirtuoso

import java.io.File
import kotlin.random.Random
import kotlin.text.uppercase

// проверка файла  на наличие недопустимых слов
    fun checkFound(words: File): String {
        var rez = true
        var countInvalid = 0
        words.forEachLine {
            var str = it
            if (checkCount(str) && chekEngl(str) && checkDupl(str))
                rez = rez && true
            else {
                countInvalid++
                rez = rez && false
            }
        }
        return if (!rez) "Error: $countInvalid invalid words were found in the ${words.name} file."
        else ""
    }

    // Проверка включено ли каждое слово-кандидат в файл со всеми словами
    fun checkCandidat(words: File, candid: File): String {
        //var rez = true
        var countInvalid = 0
        val wordsInLower = words.readLines().map { it.lowercase() }

        candid.forEachLine {
            val cond = wordsInLower.contains(it.lowercase())

            if (!cond) {
                countInvalid++
            }

        }
        return if (countInvalid != 0) "Error: $countInvalid candidate words are not included in the ${words.name} file."
        else ""
    }

    public fun main(args: Array<String>) {
        var hints = arrayListOf<String>() // база для подсказок
        var useChar = arrayListOf<Char>() // использованные символы
        if (args.count() != 2) {
            println("Error: Wrong number of arguments.")
            return
        }

        var words = File(args[0])
        var candidates = File(args[1])
        if (!words.exists()) {
            println("Error: The words file ${words.name} doesn't exist.")
            return
        }

        if (!candidates.exists()) {
            println("Error: The candidate words file ${candidates.name} doesn't exist.")
            return
        }

        if (checkFound(words) != "")
            println(checkFound(words)) // TODO: return and remove the else block?
        else
            if (checkFound(candidates) != "") {
                println(checkFound(candidates)) // TODO: return and remove the else block?
            } else {
                if (checkCandidat(words, candidates) != "")
                    println(checkCandidat(words, candidates)) // TODO: return and remove the else block?
                else {
                    println("Words Virtuoso")
                    var countWords = candidates.readLines().lastIndex
                    var RANDOM = Random.nextInt(0, countWords + 1)
                    var secret = candidates.readLines()[RANDOM]
                    var theEnd = false
                    var countTries = 1 // начало отсчета ходов
                    var start = System.currentTimeMillis() // начало отсчета времени

                    while (!theEnd) {
                        println("Input a 5-letter word:")
                        var input = readln().lowercase()

                        if (input == "exit") {
                            println("The game is over.")
                            theEnd = true
                            return
                        }

                        if (!checkCount(input))
                            println("The input isn't a 5-letter word.")
                        else
                            if (!chekEngl(input))
                                println("One or more letters of the input aren't valid.")
                            else
                                if (!checkDupl(input))
                                    println("The input has duplicated letters.")
                                else
                                    if (!words.readText().contains(input))
                                        println("The input word isn't included in my words list.")
                                    else
                                        if (secret == input) {

                                            hints.forEach { println(it) }
                                            println(test(input, secret).joinToString(""))
                                            println("Correct!")
                                            var end = System.currentTimeMillis()
                                            val duration = end - start  // Milliseconds as a Long

                                            if (countTries == 1) {
                                                println("Amazing luck! The solution was found at once.")
                                            }
                                            else
                                            {
                                                println("The solution was found after ${countTries} tries in ${duration / 100} seconds. ")
                                            }
                                            theEnd = true
                                        } else {
                                            countTries += 1
                                            val hintsStr = test(input, secret)
                                            hints.add(hintsStr.joinToString(""))
                                           // println(hintsStr)
                                              hints.forEach { println(it) }

                                            // добавляем в список неиспользуемые символы
                                            input.forEach {
                                                var str2 = it.toString().uppercase()
                                                if (!secret.contains(it) && !useChar.contains(str2[0])){
                                                    useChar.add(str2[0])
                                                }

                                            }
                                            // Важно! окрашиваем всю строку, а не посимвольно
                                            useChar.sort()
                                            println("\u001B[48:5:14m${useChar.joinToString("")}\u001B[0m")
                                        }
                    }

                }
            }
    }

fun test(str: String, secret: String): ArrayList<String>  {
var rezult = arrayListOf<String>("_", "_","_", "_", "_")
    for (i in 0..4) {
        if (str[i] == secret[i]) {
            var ch = str[i].uppercase()
            rezult[i] = "\u001B[48:5:10m${ch.uppercase()}\u001B[0m"
        }
         else if (secret.contains(str[i])) {
            rezult[i] = "\u001B[48:5:11m${str[i].uppercase()}\u001B[0m"
        }
         else {
             rezult[i] = "\u001B[48:5:7m${str[i].uppercase()}\u001B[0m"
        }
    }
return  rezult
}


    // Проверка на количество The input isn't a 5-letter string.
    fun checkCount(fStr: String): Boolean {
        return fStr.length == 5
    }

    // Проверка на неанглийский алфавит The input has invalid characters.
    fun chekEngl(fStr: String): Boolean {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        var rezult = true
        fStr.forEach {
            rezult = rezult && it.lowercase() in alphabet
        }
        return rezult
    }

    // Проверка на дубликаты The input has duplicate letters.
    fun checkDupl(fStr: String): Boolean {
        var count = 0
        fStr.forEach {
            var index = fStr.indexOf(it)
            var lastIndex = fStr.lastIndexOf(it)
            var dif = lastIndex - index
            count += dif
        }
        return (count == 0)
    }
