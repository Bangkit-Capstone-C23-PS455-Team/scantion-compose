package com.bangkit.scantion.model

import android.net.Uri

data class SavedImage (val uri: Uri, val label: String, val confidence: Float)