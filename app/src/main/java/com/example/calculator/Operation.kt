package com.example.calculator

import android.content.Context
import android.widget.Toast
import java.util.Stack

class Operation(private val context: Context) {
    // Проверяет валидность введенного выражения
    fun isValid(input: String): Boolean {
        if (input.isEmpty()) {
            Toast.makeText(context, "Введите выражение", Toast.LENGTH_SHORT).show()
            return false
        }
        val regex = Regex("^[0-9.()+\\-*/=]*$")
        if (!regex.matches(input)) {
            Toast.makeText(context, "Некорректное выражение", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Удаляет лишние символы равенства и точки
    private fun removeDuplicateEqualsAndDots(input: String): String {
        if (input.isEmpty()) return "="
        val result = StringBuilder()
        var equalsCount = 0
        var dotCount = 0
        var previousChar: Char? = null

        for (char in input) {
            when (char) {
                '=' -> {
                    if (previousChar != '=') {
                        equalsCount++
                    }
                }
                '.' -> {
                    if (dotCount == 0) {
                        result.append(char)
                        dotCount++
                    }
                }
                else -> {
                    result.append(char)
                    dotCount = 0
                }
            }
            previousChar = char
        }
        repeat(equalsCount) {
            result.append('=')
        }
        if (equalsCount == 0) {
            result.append('=')
        }
        return result.toString()
    }
    fun input(input: String): String {
        return removeDuplicateEqualsAndDots(input)
    }

    // Удаляет пробелы из строки и возвращает результат вычисления
    fun calculate(expression: String): String {
        val doubleResult: Double?
        var result: String = ""
        val cleanedExpression = expression.replace(" ", "").replace("=", "")
        val postfix = infixToPostfix(cleanedExpression)

        try {
            doubleResult = evaluatePostfix(postfix)
            result = doubleResult.toString()
            if (doubleResult.isInfinite()) {
                Toast.makeText(context, "Нельзя делить на ноль", Toast.LENGTH_SHORT).show()
                result = ""
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка в выражении! " + e, Toast.LENGTH_SHORT).show()
        }

        return result
    }

    // Преобразует инфиксное выражение в постфиксное
    private fun infixToPostfix(expression: String): List<String> {
        val result = mutableListOf<String>()
        val stack = Stack<Char>()
        var i = 0

        while (i < expression.length) {
            val c = expression[i]

            if (c.isDigit() || c == '.') {
                var number = ""
                while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
                    number += expression[i]
                    i++
                }
                result.add(number)
                i-- // уменьшаем на 1, так как внешний цикл увеличит
            } else if (c == '(') {
                stack.push(c)
            } else if (c == ')') {
                while (stack.isNotEmpty() && stack.peek() != '(') {
                    result.add(stack.pop().toString())
                }
                stack.pop()
            } else if (isOperator(c)) {
                while (stack.isNotEmpty() && precedence(c) <= precedence(stack.peek())) {
                    result.add(stack.pop().toString())
                }
                stack.push(c)
            }
            i++
        }

        while (stack.isNotEmpty()) {
            result.add(stack.pop().toString())
        }

        return result
    }

    // Проверяет, является ли символ оператором
    private fun isOperator(c: Char): Boolean {
        return c == '+' || c == '-' || c == '*' || c == '/'
    }

    // Определяет приоритет операций
    private fun precedence(c: Char): Int {
        return when (c) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> -1
        }
    }

    // Вычисляет значение постфиксного выражения
    private fun evaluatePostfix(postfix: List<String>): Double {
        val stack = Stack<Double>()

        for (token in postfix) {
            if (token.toDoubleOrNull() != null) {
                stack.push(token.toDouble())
            } else if (isOperator(token[0])) {
                val b = stack.pop()
                val a = stack.pop()
                stack.push(applyOperation(token[0], a, b))
            }
        }

        return stack.pop()
    }

    // Выполняет арифметическую операцию
    private fun applyOperation(op: Char, a: Double, b: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> throw IllegalArgumentException("Неподдерживаемая операция $op")
        }
    }
}
