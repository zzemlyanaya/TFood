/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 22.01.2021, 17:14
 */

package ru.zzemlyanaya.tfood.model

import java.io.Serializable

data class Day (
    val _id: Int = 0,
    var userId: Int = 0,
    val date: String = "",
    var kkal: Int = 0,
    var prots: Float = 0F,
    var fats: Float = 0F,
    var carbs: Float = 0F,
    var breakfast: Array<Product> = emptyArray(),
    var lunch: Array<Product> = emptyArray(),
    var dinner: Array<Product> = emptyArray(),
    var snack: Array<Product> = emptyArray(),
    var activities: Array<Product> = emptyArray(),
    var water: Int = 0,
    var vitamins: Vitamins,
    var minerals: Minerals
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Day

        if (_id != other._id) return false
        if (userId != other.userId) return false
        if (date != other.date) return false
        if (kkal != other.kkal) return false
        if (prots != other.prots) return false
        if (fats != other.fats) return false
        if (carbs != other.carbs) return false
        if (!breakfast.contentEquals(other.breakfast)) return false
        if (!lunch.contentEquals(other.lunch)) return false
        if (!dinner.contentEquals(other.dinner)) return false
        if (!snack.contentEquals(other.snack)) return false
        if (!activities.contentEquals(other.activities)) return false
        if (water != other.water) return false
        if (vitamins != other.vitamins) return false
        if (minerals != other.minerals) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id
        result = 31 * result + userId
        result = 31 * result + date.hashCode()
        result = 31 * result + kkal
        result = 31 * result + prots.hashCode()
        result = 31 * result + fats.hashCode()
        result = 31 * result + carbs.hashCode()
        result = 31 * result + breakfast.contentHashCode()
        result = 31 * result + lunch.contentHashCode()
        result = 31 * result + dinner.contentHashCode()
        result = 31 * result + snack.contentHashCode()
        result = 31 * result + activities.contentHashCode()
        result = 31 * result + water
        result = 31 * result + vitamins.hashCode()
        result = 31 * result + minerals.hashCode()
        return result
    }
}

data class Minerals(
    var Ca: Double,
    var P: Double,
    var Mg: Double,
    var K: Double,
    var Na: Double,
    var Fe: Double,
    var Zn: Double,
    var I: Double,
    var Se: Double,
    var F: Double
) : Serializable

data class Vitamins(
    var D: Double,
    var E: Double,
    var K: Double,
    var A: Double,
    var C: Double,
    var B1: Double,
    var B2: Double,
    var B3: Double,
    var B5: Double,
    var B6: Double,
    var B9: Double,
    var B12: Double,
    var H: Double
) : Serializable