package org.getscol.gscol

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform