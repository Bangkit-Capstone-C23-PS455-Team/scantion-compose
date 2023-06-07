package com.bangkit.scantion.model

import androidx.compose.ui.graphics.vector.ImageVector

class ProfileItems (
    val menuName: String,
    val desc: String,
    val icon: ImageVector,
    val action: () -> Unit
)