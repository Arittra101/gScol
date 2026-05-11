import org.getscol.gscol.feature.application.data.dto.request.ApplicationCreateRequestBodyDto
import org.getscol.gscol.feature.application.data.dto.request.IntakeRequestDto
import org.getscol.gscol.feature.application.domain.model.request.ApplicationCreateRequestBody
import org.getscol.gscol.feature.application.domain.model.request.IntakeRequest

fun ApplicationCreateRequestBody.toDto(): ApplicationCreateRequestBodyDto {
    return ApplicationCreateRequestBodyDto(
        universityId = universityId,
        courseId = courseId,
        intake = intake?.toDto()
    )
}

fun IntakeRequest.toDto(): IntakeRequestDto {
    return IntakeRequestDto(
        intakeMonth = intakeMonth,
        intakeYear = intakeYear
    )
}