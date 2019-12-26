package com.venkatesh.forecast.data.provider

import com.venkatesh.forecast.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}