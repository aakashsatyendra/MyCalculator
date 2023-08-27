package com.myapp.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private var tvInput: TextView? = null
    var lastNumeric:Boolean = false
    var lastOperator:Boolean = false
    var lastDot:Boolean = false
    var operationDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput = findViewById(R.id.tvInput)
    }

    fun onCancel(view: View){
        var newException = tvInput?.text
        newException=newException?.substring(0,newException.length-1)
        tvInput?.text = newException
    }

    fun onDigit(view: View){
        if (!operationDone){
            tvInput?.append((view as Button).text)
        }else{
            tvInput?.text = ""
            operationDone = false
            tvInput?.append((view as Button).text)
        }
        lastNumeric = true
        lastDot = false
    }

    fun onClear(view: View){
        tvInput?.text = ""
    }

    fun removeDotAfterZero(result: String):String{
        var value = result
        if(value.endsWith(".0")){
            value = value.substring(0,value.length-2)
        }
        return value
    }

    fun onEval(view: View){
        evaluater()
    }

    private fun evaluater(){
        if (lastNumeric){
            var tvValue = tvInput?.text.toString()
            var prefix = ""
            try {
                if (tvValue.startsWith("-")){
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }
                if(tvValue.contains("-")){
                    val splitValue = tvValue.split("-")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    tvInput?.text = removeDotAfterZero((one.toDouble() - two.toDouble()).toString())
                }else if(tvValue.contains("+")){
                    val splitValue = tvValue.split("+")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    tvInput?.text = removeDotAfterZero((one.toDouble() + two.toDouble()).toString())
                }else if(tvValue.contains("/")){
                    val splitValue = tvValue.split("/")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    tvInput?.text = removeDotAfterZero((one.toDouble() / two.toDouble()).toString())
                }else if(tvValue.contains("*")){
                    val splitValue = tvValue.split("*")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    tvInput?.text = removeDotAfterZero((one.toDouble() * two.toDouble()).toString())
                }
                operationDone = true
            } catch (e: ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    // runs when we click (.) button, checks if expression have any (.), if not then add a (.)
    fun onDedimalPoint(view: View){
        tvInput?.text?.let{
            var express = tvInput?.text
            var secondPartOfExpress = express

            if(express?.contains("+")==true){
                var splitExpress = express.split("+")
                secondPartOfExpress = splitExpress[1]
            }else if(express?.contains("-")==true){
                var splitExpress = express.split("-")
                if(express.startsWith("-")){
                    secondPartOfExpress = splitExpress[2]
                }else{
                    secondPartOfExpress = splitExpress[1]
                }
            }else if(express?.contains("/")==true){
                var splitExpress = express.split("/")
                secondPartOfExpress = splitExpress[1]
            }else if(express?.contains("*")==true){
                var splitExpress = express.split("*")
                secondPartOfExpress = splitExpress[1]
            }
            var hasDot = secondPartOfExpress?.contains(".")
            var isNotEmptyStr = secondPartOfExpress?.isNotEmpty()
            if (isNotEmptyStr == true){
                if(lastNumeric && !lastDot){
                    if(hasDot==false){
                        tvInput?.append(".")
                        lastNumeric = false
                        lastDot = true
                    }
                }
            }
        }
    }

    // runs when a operator button is clicked
    fun onOperator(view: View){
        tvInput?.text?.let {

            // when the the first operation when input doesn't have any operator (44) then add operator
            if(lastNumeric && !isOperatorAdded(it.toString())){
                var checkIfNegExpression = tvInput?.text?.startsWith("-")
                if(checkIfNegExpression==true){
                    evaluater()
                }
                tvInput?.append((view as Button).text)
                lastNumeric = false
                lastDot = false
                operationDone = false
            // when we already have an expression (3+4) then evaluate it and then add operator
            }else if(lastNumeric && isOperatorAdded(it.toString())){
                evaluater()
                tvInput?.append((view as Button).text)
                lastNumeric = false
                lastDot = false
                operationDone = false
            }else if(lastOperator){
                var newExpression = tvInput?.text
                newExpression = newExpression?.substring(0,newExpression.length-1)
                tvInput?.text = newExpression
                tvInput?.append((view as Button).text)
            }
            lastOperator = true
        }
    }

    // Checks if operator is added in expression or not (2+3)
    // neglects if expression starts with -
    fun isOperatorAdded(value: String): Boolean{
        return if (value.startsWith("-")){
            false
        }else{
            value.contains("/") || value.contains("*") || value.contains("+") || value.contains("-")
        }
    }
}
