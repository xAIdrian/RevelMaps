package com.reveltransit.takehome.helper

import android.content.Context
import androidx.annotation.RawRes
import com.reveltransit.takehome.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(
    private val context: Context
) {
    fun getRawResourceAsString(@RawRes rawId: Int) =
        context.resources.openRawResource(R.raw.upstream_vehicles).bufferedReader().readText()
}