package org.getscol.gscol.feature.application.domain.model.response

data class ApplicationListResponse(
    val applications: List<ApplicationInfo>
)

data class ApplicationInfo(
    val applicationId: String? = null,
    val universityLogoUrl: String? = null,
    val courseName: String? = null,
    val universityName: String? = null,
    val currentStage: Stage? = null

)

data class Stage(
    val stageCode: String? = null,
    val applicationStageName: String? = null,
)



