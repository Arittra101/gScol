package org.getscol.gscol

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSCalendarUnitYear
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val transitionAnimationTime: Int = 500
    override val platformName: String = "IOS"
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun currentYear(): Int = (NSCalendar.currentCalendar().component(NSCalendarUnitYear, NSDate())).toInt()
val transitionAnimationTime = getPlatform().transitionAnimationTime

actual fun platformEnterTransition() = fadeIn(
    animationSpec = tween(durationMillis = transitionAnimationTime, easing = FastOutSlowInEasing)
)

actual fun platformExitTransition() = fadeOut(
    animationSpec = tween(durationMillis = transitionAnimationTime, easing = FastOutSlowInEasing)
)

actual fun platformPopEnterTransition() = fadeIn(
    animationSpec = tween(durationMillis = transitionAnimationTime, easing = FastOutSlowInEasing)
)

actual fun platformPopExitTransition() = fadeOut(
    animationSpec = tween(durationMillis = transitionAnimationTime, easing = FastOutSlowInEasing)
)
