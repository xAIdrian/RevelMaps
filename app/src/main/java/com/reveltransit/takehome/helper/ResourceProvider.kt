package com.reveltransit.takehome.helper

import android.content.Context
import androidx.annotation.RawRes
import javax.inject.Inject

class ResourceProvider @Inject constructor(
    private val context: Context
) {
    fun getRawResource(@RawRes rawId: Int) = context.resources.openRawResource(rawId)
}