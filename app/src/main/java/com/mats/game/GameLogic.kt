package com.mats.game

class GameLogic(
    var balance: Long = 0,
    var level: Int = 1,
    var clicksToNextLevel: Long = 10,
    var currentLevelClicks: Long = 0,
    var clickValue: Long = 1,
    var upgradeClickPrice: Long = 10,
    var autoclickerValue: Long = 0,
    var autoclickerPrice: Long = 50,
    var cigarettesCount: Int = 0,
    var cigarettesPrice: Long = 100
) {
    fun handleMainClick() {
        balance += clickValue
        currentLevelClicks++
        checkLevelUp()
    }

    private fun checkLevelUp(): Boolean {
        if (currentLevelClicks >= clicksToNextLevel) {
            level++
            currentLevelClicks = 0
            clicksToNextLevel = (clicksToNextLevel * 1.5).toLong() + 10
            return true
        }
        return false
    }

    fun buyUpgradeClick(): Boolean {
        if (balance >= upgradeClickPrice) {
            balance -= upgradeClickPrice
            clickValue += 1
            upgradeClickPrice = (upgradeClickPrice * 1.8).toLong()
            return true
        }
        return false
    }

    fun buyAutoclicker(): Boolean {
        if (balance >= autoclickerPrice) {
            balance -= autoclickerPrice
            autoclickerValue += 1
            autoclickerPrice = (autoclickerPrice * 2.0).toLong()
            return true
        }
        return false
    }

    fun buyCigarettes(): Boolean {
        if (balance >= cigarettesPrice) {
            balance -= cigarettesPrice
            cigarettesCount += 1
            cigarettesPrice = (cigarettesPrice * 2.5).toLong()
            return true
        }
        return false
    }

    fun calculatePassiveIncome(): Long {
        if (autoclickerValue <= 0) return 0
        val multiplier = 1.0 + (cigarettesCount * 0.05)
        return (autoclickerValue * multiplier).toLong()
    }
}
