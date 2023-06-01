package com.bangkit.scantion.util

import com.bangkit.scantion.R

class WalkthroughItems (
    val image: Int,
    val textFirst: Int,
    val textSecond: Int
    ) {
    companion object {
        fun getData(): List<WalkthroughItems> {
            return listOf(
                WalkthroughItems(
                    R.drawable.img_walkthrough_1,
                    R.string.walkthroughTitle1,
                    R.string.walkthroughText1
                ),
                WalkthroughItems(
                    R.drawable.img_walkthrough_2,
                    R.string.walkthroughTitle2,
                    R.string.walkthroughText2
                ),
                WalkthroughItems(
                    R.drawable.img_walkthrough_3,
                    R.string.walkthroughTitle3,
                    R.string.walkthroughText3
                ),
                WalkthroughItems(
                    R.drawable.img_walkthrough_4,
                    R.string.login_text,
                    R.string.register_text
                )
            )
        }
    }
}