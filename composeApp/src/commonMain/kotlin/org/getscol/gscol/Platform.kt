package org.getscol.gscol

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition

interface Platform {
    val name: String
    val transitionAnimationTime: Int
}

expect fun getPlatform(): Platform
expect fun platformEnterTransition(): EnterTransition
expect fun platformExitTransition(): ExitTransition
expect fun platformPopEnterTransition(): EnterTransition
expect fun platformPopExitTransition(): ExitTransition