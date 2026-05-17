package com.mats.game

import org.junit.Test
import org.junit.Assert.*

class GameLogicTest {

    @Test
    fun testClickIncreasesBalance() {
        val game = GameLogic()
        game.handleMainClick()
        assertEquals(1, game.balance)
    }

    @Test
    fun testLevelUp() {
        val game = GameLogic()
        // Default clicksToNextLevel is 10
        for (i in 1..10) {
            game.handleMainClick()
        }
        assertEquals(2, game.level)
        assertEquals(0, game.currentLevelClicks)
    }

    @Test
    fun testBuyUpgradeClick() {
        val game = GameLogic()
        game.balance = 10
        assertTrue(game.buyUpgradeClick())
        assertEquals(0, game.balance)
        assertEquals(2, game.clickValue)
    }

    @Test
    fun testPassiveIncomeWithCigarettes() {
        val game = GameLogic()
        game.autoclickerValue = 10
        game.cigarettesCount = 1 // +5%
        assertEquals(10, game.calculatePassiveIncome()) // 10 * 1.05 = 10.5 -> 10 Long

        game.cigarettesCount = 20 // +100%
        assertEquals(20, game.calculatePassiveIncome()) // 10 * 2.0 = 20
    }
}
