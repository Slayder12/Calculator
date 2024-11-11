package com.example.calculator

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarMain: Toolbar
    private lateinit var inputET: EditText
    private lateinit var outputTV: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputET = findViewById(R.id.inputET)
        outputTV = findViewById(R.id.outputTV)
        toolbarMain = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        toolbarMain.setTitleTextColor(Color.WHITE)


        val dotBTN = findViewById<Button>(R.id.dotBTN)
        val bracketOpenBTN = findViewById<Button>(R.id.bracketOpenBTN)
        val bracketCloseBTN = findViewById<Button>(R.id.bracketCloseBTN)
        val plusBTN = findViewById<Button>(R.id.plusBTN)
        val minusBTN = findViewById<Button>(R.id.minusBTN)
        val multiplyBTN = findViewById<Button>(R.id.multiplyBTN)
        val divideBTN = findViewById<Button>(R.id.divideBTN)
        val zeroBTN = findViewById<Button>(R.id.zeroBTN)
        val oneBTN = findViewById<Button>(R.id.oneBTN)
        val twoBTN = findViewById<Button>(R.id.twoBTN)
        val threeBTN = findViewById<Button>(R.id.threeBTN)
        val fourBTN = findViewById<Button>(R.id.fourBTN)
        val fiveBTN = findViewById<Button>(R.id.fiveBTN)
        val sixBTN = findViewById<Button>(R.id.sixBTN)
        val sevenBTN = findViewById<Button>(R.id.sevenBTN)
        val eightBTN = findViewById<Button>(R.id.eightBTN)
        val nineBTN = findViewById<Button>(R.id.nineBTN)
        val equalBTN = findViewById<Button>(R.id.equalBTN)
        val resetBTN = findViewById<Button>(R.id.resetBTN)

        inputET.showSoftInputOnFocus = false

        inputET.setOnTouchListener { v, event ->
            if (inputET.text.toString().isNotEmpty()) {
                val drawableRight = inputET.compoundDrawables[2]
                if (drawableRight != null && event.action == MotionEvent.ACTION_UP) {
                    if (event.x >= inputET.width - inputET.paddingRight - drawableRight.bounds.width()) {
                        inputET.text.delete(inputET.text.length - 1, inputET.text.length)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        val buttons = listOf<Button>(
            plusBTN, minusBTN, multiplyBTN, divideBTN, dotBTN, bracketOpenBTN, bracketCloseBTN,
            zeroBTN, oneBTN, twoBTN, threeBTN, fourBTN,
            fiveBTN, sixBTN, sevenBTN, eightBTN, nineBTN
        )
        for (button in buttons) {
            button.setOnClickListener {
                val currentText = inputET.text.toString()
                inputET.setText(currentText + button.text)
                inputET.setSelection(inputET.text.length)


            }
        }

        resetBTN.setOnClickListener {
            inputET.setText("")
            outputTV.text = ""
        }

        equalBTN.setOnClickListener {
            val expression = inputET.text.toString()
            if (!Operation(this).isValid(expression)) return@setOnClickListener
            val result: String = Operation(this).calculate(expression)
            inputET.setText(Operation(this).input(expression))
            outputTV.text = result
            inputET.setSelection(inputET.text.length)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exitMenuMain -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}