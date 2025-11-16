package org.getscol.gscol

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val transitionAnimationTime: Int = 700
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun platformEnterTransition() = EnterTransition.None
actual fun platformExitTransition() = ExitTransition.None
actual fun platformPopEnterTransition() = EnterTransition.None
actual fun platformPopExitTransition() = ExitTransition.None
