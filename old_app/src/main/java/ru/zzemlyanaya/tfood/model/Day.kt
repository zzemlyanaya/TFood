/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.model

import android.annotation.SuppressLint
import com.google.gson.Gson
import java.io.Serializable

data class Day(
    val id: String,
    var user: String,
    val date: String,
    var kcal: Int,
    var kcalNeed: Int,
    var prots: Float,
    var protsNeed: Float,
    var fats: Float,
    var fatsNeed: Float,
    var carbs: Float,
    var carbsNeed: Float,
    var breakfast: Array<ProductEntity>,
    var breakfastKcal: Int,
    var lunch: Array<ProductEntity>,
    var lunchKcal: Int,
    var dinner: Array<ProductEntity>,
    var dinnerKcal: Int = 0,
    var snack: Array<ProductEntity>,
    var snackKcal: Int,
    var activity: Array<ActivityResponse>,
    var water: Int,
    var waterNeed: Int,
    var vitamins: Vitamins,
    var minerals: Minerals,
    var rating: Float
)

data class Minerals(
    var ca: Float = 0f,
    var p: Float = 0f,
    var mg: Float = 0f,
    var na: Float = 0f,
    var fe: Float = 0f,
    var zn: Float = 0f,
    var i: Float = 0f,
    var se: Float = 0f,
    var f: Float = 0f
) : Serializable {

    constructor(string: String) : this() {
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
            minerals.ca = map.getOrDefault("Ca", 0f)
            minerals.mg = map.getOrDefault("Mg", 0f)
            minerals.p = map.getOrDefault("P", 0f)
            minerals.na = map.getOrDefault("Na", 0f)
            minerals.fe = map.getOrDefault("Fe", 0f)
            minerals.zn = map.getOrDefault("Zn", 0f)
            minerals.i = map.getOrDefault("I", 0f)
            minerals.se = map.getOrDefault("Se", 0f)
            minerals.f = map.getOrDefault("F", 0f)
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
            } catch (e: Exception) {
            }
        }
        return if (list.size > 0) list.joinToString(", ") else "-"
    }
}

data class Vitamins(
    var d: Float = 0f,
    var e: Float = 0f,
    var k: Float = 0f,
    var a: Float = 0f,
    var c: Float = 0f,
    var b1: Float = 0f,
    var b2: Float = 0f,
    var b3: Float = 0f,
    var b5: Float = 0f,
    var b6: Float = 0f,
    var b9: Float = 0f,
    var b12: Float = 0f,
    var h: Float = 0f
) : Serializable {
    companion object {
        fun fromString(string: String): Vitamins {
            val gson = Gson()
            return gson.fromJson(string, Vitamins::class.java)
        }

        fun fromMap(map: Map<String, Float>): Vitamins {
            val vitamins = Vitamins()
            vitamins.d = map.getOrDefault("D", 0f)
            vitamins.e = map.getOrDefault("E", 0f)
            vitamins.k = map.getOrDefault("K", 0f)
            vitamins.a = map.getOrDefault("A", 0f)
            vitamins.c = map.getOrDefault("C", 0f)
            vitamins.b1 = map.getOrDefault("B1", 0f)
            vitamins.b2 = map.getOrDefault("B2", 0f)
            vitamins.b3 = map.getOrDefault("B3", 0f)
            vitamins.b5 = map.getOrDefault("B5", 0f)
            vitamins.b6 = map.getOrDefault("B6", 0f)
            vitamins.b9 = map.getOrDefault("B9", 0f)
            vitamins.b12 = map.getOrDefault("B12", 0f)
            vitamins.h = map.getOrDefault("H", 0f)
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
            } catch (e: Exception) {
            }
        }
        return if (list.size > 0) list.joinToString(", ") else "-"
    }
}