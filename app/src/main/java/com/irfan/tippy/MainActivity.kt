package com.irfan.tippy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator


private const val TAG = "MainActivity"
private const val INIT_TIP_PERCENT = 10


class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmt: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmt: TextView
    private lateinit var tvTotalAmt: TextView
    private lateinit var tvTipDesc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etBaseAmt = findViewById(R.id.etBaseAmt)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipAmt = findViewById(R.id.tvTipAmt)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTotalAmt = findViewById(R.id.tvTotalAmt)
        tvTipDesc = findViewById(R.id.tvTipDesc)

        seekBarTip.progress = INIT_TIP_PERCENT
        tvTipPercentLabel.text = "$INIT_TIP_PERCENT%"
        updateTipDesc(INIT_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercentLabel.text = "$p1%"
                compTipTotal()
                updateTipDesc(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        etBaseAmt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged: $p0")
                compTipTotal()
            }

        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateTipDesc(p1: Int) {
        val tipDesc = when (p1) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDesc.text = tipDesc

        var clr = ArgbEvaluator().evaluate(
            p1.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.worst_tip),
            ContextCompat.getColor(this, R.color.best_tip)
        ) as Int
        tvTipDesc.setTextColor(clr)
    }

    private fun compTipTotal() {
        if (etBaseAmt.text.isEmpty()) {
            tvTipAmt.text = ""
            tvTotalAmt.text = ""
            return
        }
        val baseAmt = etBaseAmt.text.toString().toDouble()
        val tipP = seekBarTip.progress

        val tipA = baseAmt * tipP / 100
        val totalA = baseAmt + tipA

        tvTipAmt.text = "%.2f".format(tipA)
        tvTotalAmt.text = "%.2f".format(totalA)
    }
}