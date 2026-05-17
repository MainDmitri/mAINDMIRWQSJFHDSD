package com.mats.game

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var game: GameLogic

    // UI elements
    private lateinit var tvBalance: TextView
    private lateinit var tvLevel: TextView
    private lateinit var btnClick: Button
    private lateinit var tvUpgradeClickLabel: TextView
    private lateinit var tvUpgradeClickPrice: TextView
    private lateinit var tvAutoclickerLabel: TextView
    private lateinit var tvAutoclickerPrice: TextView
    private lateinit var tvCigarettesLabel: TextView
    private lateinit var tvCigarettesPrice: TextView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MatsGamePrefs", Context.MODE_PRIVATE)
        game = GameLogic()
        loadProgress()

        initUI()
        updateUI()
        startPassiveIncome()
    }

    private fun initUI() {
        tvBalance = findViewById(R.id.tvBalance)
        tvLevel = findViewById(R.id.tvLevel)
        btnClick = findViewById(R.id.btnClick)
        tvUpgradeClickLabel = findViewById(R.id.tvUpgradeClickLabel)
        tvUpgradeClickPrice = findViewById(R.id.tvUpgradeClickPrice)
        tvAutoclickerLabel = findViewById(R.id.tvAutoclickerLabel)
        tvAutoclickerPrice = findViewById(R.id.tvAutoclickerPrice)
        tvCigarettesLabel = findViewById(R.id.tvCigarettesLabel)
        tvCigarettesPrice = findViewById(R.id.tvCigarettesPrice)

        btnClick.setOnClickListener {
            val oldLevel = game.level
            game.handleMainClick()
            if (game.level > oldLevel) {
                Toast.makeText(this, getString(R.string.level_up_msg), Toast.LENGTH_SHORT).show()
            }
            updateUI()
            saveProgress()
        }

        findViewById<LinearLayout>(R.id.layoutUpgradeClick).setOnClickListener {
            if (game.buyUpgradeClick()) {
                updateUI()
                saveProgress()
            }
        }

        findViewById<LinearLayout>(R.id.layoutAutoclicker).setOnClickListener {
            if (game.buyAutoclicker()) {
                updateUI()
                saveProgress()
            }
        }

        findViewById<LinearLayout>(R.id.layoutCigarettes).setOnClickListener {
            if (game.buyCigarettes()) {
                updateUI()
                saveProgress()
            }
        }
    }

    private fun updateUI() {
        tvBalance.text = getString(R.string.balance_label, game.balance)
        tvLevel.text = getString(R.string.level_label, game.level)

        tvUpgradeClickLabel.text = getString(R.string.upgrade_click_label, 1)
        tvUpgradeClickPrice.text = getString(R.string.upgrade_click_price, game.upgradeClickPrice)

        tvAutoclickerLabel.text = getString(R.string.autoclicker_label, 1)
        tvAutoclickerPrice.text = getString(R.string.autoclicker_price, game.autoclickerPrice)

        tvCigarettesLabel.text = getString(R.string.cigarettes_label)
        tvCigarettesPrice.text = getString(R.string.cigarettes_price, game.cigarettesPrice)
    }

    private fun startPassiveIncome() {
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                val income = game.calculatePassiveIncome()
                if (income > 0) {
                    game.balance += income
                    updateUI()
                    saveProgress()
                }
            }
        }
    }

    private fun saveProgress() {
        val editor = sharedPreferences.edit()
        editor.putLong("balance", game.balance)
        editor.putInt("level", game.level)
        editor.putLong("clicksToNextLevel", game.clicksToNextLevel)
        editor.putLong("currentLevelClicks", game.currentLevelClicks)
        editor.putLong("clickValue", game.clickValue)
        editor.putLong("upgradeClickPrice", game.upgradeClickPrice)
        editor.putLong("autoclickerValue", game.autoclickerValue)
        editor.putLong("autoclickerPrice", game.autoclickerPrice)
        editor.putInt("cigarettesCount", game.cigarettesCount)
        editor.putLong("cigarettesPrice", game.cigarettesPrice)
        editor.apply()
    }

    private fun loadProgress() {
        game.balance = sharedPreferences.getLong("balance", 0)
        game.level = sharedPreferences.getInt("level", 1)
        game.clicksToNextLevel = sharedPreferences.getLong("clicksToNextLevel", 10)
        game.currentLevelClicks = sharedPreferences.getLong("currentLevelClicks", 0)
        game.clickValue = sharedPreferences.getLong("clickValue", 1)
        game.upgradeClickPrice = sharedPreferences.getLong("upgradeClickPrice", 10)
        game.autoclickerValue = sharedPreferences.getLong("autoclickerValue", 0)
        game.autoclickerPrice = sharedPreferences.getLong("autoclickerPrice", 50)
        game.cigarettesCount = sharedPreferences.getInt("cigarettesCount", 0)
        game.cigarettesPrice = sharedPreferences.getLong("cigarettesPrice", 100)
    }
}
