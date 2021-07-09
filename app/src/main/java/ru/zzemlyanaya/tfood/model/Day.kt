/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.model

import com.google.gson.Gson
import java.io.Serializable

data class Day (
        val _id: String = "",
        var userId: String = "",
        val date: String = "",
        var kkal: Int = 0,
        var prots: Float = 0F,
        var fats: Float = 0F,
        var carbs: Float = 0F,
        var breakfast: Array<ProductShort> = emptyArray(),
        var breakfastKkal: Int = 0,
        var lunch: Array<ProductShort> = emptyArray(),
        var lunchKkal: Int = 0,
        var dinner: Array<ProductShort> = emptyArray(),
        var dinnerKkal: Int = 0,
        var snack: Array<ProductShort> = emptyArray(),
        var snackKkal: Int = 0,
        var activities: Array<Activities> = emptyArray(),
        var water: Int = 0,
        var vitamins: Vitamins = Vitamins(),
        var minerals: Minerals = Minerals(),
        var rating: Float = 0f
) {

        override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Day

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
        var result = _id.hashCode()
        result = 31 * result + userId.hashCode()
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
    var Ca: Float = 0f,
    var P: Float = 0f,
    var Mg: Float = 0f,
    var Na: Float = 0f,
    var Fe: Float = 0f,
    var Zn: Float = 0f,
    var I: Float = 0f,
    var Se: Float = 0f,
    var F: Float = 0f
) : Serializable {

    constructor(string: String): this() {
        val gson = Gson()
        val res = gson.fromJson(string, Minerals::class.java)
        for (i in this.javaClass.fields)
            i.setFloat(this, res.javaClass.getField(i.name).getFloat(res))
    }

    companion object {
        fun fromString(string: String): Minerals {
            val gson = Gson()
            return gson.fromJson(string, Minerals::class.java)
        }

        fun fromMap(map: Map<String, Float>): Minerals {
            val minerals = Minerals()
            minerals.Ca = map.getOrDefault("Ca", 0f)
            minerals.Mg = map.getOrDefault("Mg", 0f)
            minerals.P= map.getOrDefault("P", 0f)
            minerals.Na = map.getOrDefault("Na", 0f)
            minerals.Fe = map.getOrDefault("Fe", 0f)
            minerals.Zn = map.getOrDefault("Zn", 0f)
            minerals.I = map.getOrDefault("I", 0f)
            minerals.Se = map.getOrDefault("Se", 0f)
            minerals.F = map.getOrDefault("F", 0f)
            return minerals
        }
    }

    override fun toString(): String {
        val list = ArrayList<String>()
        for (i in this.javaClass.declaredFields) {
            try {
                val j = i.getFloat(this)
                if (j != 0f)
                    list.add(i.name)
            } catch (e: Exception) {  }
        }
        return if (list.size > 0) list.joinToString(", ") else "-"
    }
}

data class Vitamins(
    var D: Float = 0f,
    var E: Float = 0f,
    var K: Float = 0f,
    var A: Float = 0f,
    var C: Float = 0f,
    var B1: Float = 0f,
    var B2: Float = 0f,
    var B3: Float = 0f,
    var B5: Float = 0f,
    var B6: Float = 0f,
    var B9: Float = 0f,
    var B12: Float = 0f,
    var H: Float = 0f
) : Serializable {
    companion object {
        fun fromString(string: String): Vitamins {
            val gson = Gson()
            return gson.fromJson(string, Vitamins::class.java)
        }

        fun fromMap(map: Map<String, Float>): Vitamins {
            val vitamins = Vitamins()
            vitamins.D = map.getOrDefault("D", 0f)
            vitamins.E = map.getOrDefault("E", 0f)
            vitamins.K = map.getOrDefault("K", 0f)
            vitamins.A = map.getOrDefault("A", 0f)
            vitamins.C = map.getOrDefault("C", 0f)
            vitamins.B1 = map.getOrDefault("B1", 0f)
            vitamins.B2 = map.getOrDefault("B2", 0f)
            vitamins.B3 = map.getOrDefault("B3", 0f)
            vitamins.B5 = map.getOrDefault("B5", 0f)
            vitamins.B6 = map.getOrDefault("B6", 0f)
            vitamins.B9 = map.getOrDefault("B9", 0f)
            vitamins.B12 = map.getOrDefault("B12", 0f)
            vitamins.H = map.getOrDefault("H", 0f)
            return vitamins
        }
    }

    override fun toString(): String {
        val list = ArrayList<String>()
        for (i in this.javaClass.declaredFields) {
            try {
                val j = i.getFloat(this)
                if (j != 0f)
                    list.add(i.name)
            } catch (e: Exception) {  }
        }
        return if (list.size > 0) list.joinToString(", ") else "-"
    }
}