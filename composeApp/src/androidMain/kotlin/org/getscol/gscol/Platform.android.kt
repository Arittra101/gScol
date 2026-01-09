package org.getscol.gscol

import android.os.Build
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val transitionAnimationTime: Int = 200
}

actual fun getPlatform(): Platform = AndroidPlatform()
val transitionAnimationTime = getPlatform().transitionAnimationTime

/*actual fun platformEnterTransition() = EnterTransition.None
actual fun platformExitTransition() = ExitTransition.None
actual fun platformPopEnterTransition() = EnterTransition.None
actual fun platformPopExitTransition() = ExitTransition.None*/

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


/*
You can use diff type of transaction

actual fun platformEnterTransition() = slideInHorizontally { it }
actual fun platformExitTransition() = slideOutHorizontally { -it }
actual fun platformPopEnterTransition() = slideInHorizontally { -it }
actual fun platformPopExitTransition() = slideOutHorizontally { it }

actual fun platformEnterTransition() = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(300)
)

actual fun platformExitTransition() = slideOutHorizontally(
    targetOffsetX = { -it / 3 },
    animationSpec = tween(300)
) + fadeOut(animationSpec = tween(300))

actual fun platformPopEnterTransition() = slideInHorizontally(
    initialOffsetX = { -it / 3 },
    animationSpec = tween(300)
) + fadeIn(animationSpec = tween(300))

actual fun platformPopExitTransition() = slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(300)
)*/
