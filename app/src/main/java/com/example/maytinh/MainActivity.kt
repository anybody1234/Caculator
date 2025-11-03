package com.example.maytinh

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.maytinh.R

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView

    private var currentOperand: String = "0"
    private var previousOperand: String = ""
    private var currentOperation: String = ""
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        updateDisplay()
        setClickListeners()
    }

    private fun setClickListeners() {
        val numberButtonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtonIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onNumberClick(it) }
        }

        val opButtonIds = listOf(R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv)
        opButtonIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onOperationClick(it) }
        }

        findViewById<Button>(R.id.btnEq).setOnClickListener { onEqualsClick() }
        findViewById<Button>(R.id.btnC).setOnClickListener { onClearClick() }
        findViewById<Button>(R.id.btnCE).setOnClickListener { onClearEntryClick() }
        findViewById<Button>(R.id.btnBS).setOnClickListener { onBackspaceClick() }
        findViewById<Button>(R.id.btnSign).setOnClickListener { onSignClick() }
    }

    private fun onNumberClick(view: View) {
        if (isNewOperation) {
            currentOperand = ""
            isNewOperation = false
        }

        val number = (view as Button).text.toString()

        if (currentOperand == "0") {
            currentOperand = number
        } else {
            currentOperand += number
        }
        updateDisplay()
    }

    private fun onOperationClick(view: View) {
        if (!isNewOperation && previousOperand.isNotEmpty() && currentOperation.isNotEmpty()) {
            calculate()
        }

        previousOperand = currentOperand
        currentOperation = (view as Button).text.toString()
        isNewOperation = true
    }

    private fun onEqualsClick() {
        if (previousOperand.isNotEmpty() && currentOperation.isNotEmpty() && !isNewOperation) {
            calculate()
            isNewOperation = true
            currentOperation = ""
            previousOperand = ""
        }
    }

    private fun onClearClick() {
        currentOperand = "0"
        previousOperand = ""
        currentOperation = ""
        isNewOperation = true
        updateDisplay()
    }

    private fun onClearEntryClick() {
        currentOperand = "0"
        isNewOperation = true
        updateDisplay()
    }

    private fun onBackspaceClick() {
        if (currentOperand.length > 1) {
            currentOperand = currentOperand.dropLast(1)
        } else {
            currentOperand = "0"
        }
        updateDisplay()
    }

    private fun calculate() {
        val prev = previousOperand.toLongOrNull() ?: return
        val current = currentOperand.toLongOrNull() ?: return
        val result: Long

        when (currentOperation) {
            "+" -> result = prev + current
            "-" -> result = prev - current
            "x" -> result = prev * current
            "/" -> {
                if (current != 0L) {
                    result = prev / current
                } else {
                    currentOperand = "Error"
                    updateDisplay()
                    isNewOperation = true
                    currentOperation = ""
                    previousOperand = ""
                    return
                }
            }
            else -> return
        }
        currentOperand = result.toString()
        updateDisplay()
    }

    private fun updateDisplay() {
        tvResult.text = currentOperand
    }

    private fun onSignClick() {
        if (currentOperand != "0" && currentOperand != "Error") {
            currentOperand = if (currentOperand.startsWith("-")) {
                currentOperand.substring(1)
            } else {
                "-$currentOperand"
            }
            updateDisplay()
        }
    }
}
