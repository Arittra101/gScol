package org.getscol.gscol.navigation

import kotlinx.serialization.Serializable
import org.getscol.gscol.feature.legal.presentation.LegalDocumentType

sealed interface Route {

    @Serializable
    object HomeRoute : Route

    @Serializable
    object ApplicationList : Route

    @Serializable
    object FavouriteRoute : Route

    @Serializable
    object Consultant : Route

    @Serializable
    data class ConsultantDetails(val consultantId: String) : Route

    @Serializable
    object Profile : Route

    @Serializable
    object EditProfile : Route

    @Serializable
    object Login : Route

    @Serializable
    object SignUp : Route

    @Serializable
    data class OtpVerification(val otp: String? = null) : Route

    @Serializable
    object ForgotPassword : Route

    @Serializable
    object ResetPassword : Route

    @Serializable
    object Search : Route

    @Serializable
    data class SearchResults(
        val searchText: String,
        val advancedParamsJson: String? = null
    ) : Route

    @Serializable
    object AdvancedSearch : Route

    @Serializable
    object AcademicForm : Route

    @Serializable
    data class CourseDetails(val courseId: String) : Route

    @Serializable
    data class CourseVideoPlayer(val title: String, val videoUrl: String) : Route

    @Serializable
    data class WebViewRoute(val title: String, val url: String) : Route

    @Serializable
    data class LegalDocument(val documentType: LegalDocumentType) : Route

    @Serializable
    object Splash : Route

    @Serializable
    object InEligibleScreen : Route

    @Serializable
    object UploadScreen : Route

    @Serializable
    class ApplicationFormRoute(val courseDetails: String) : Route

    @Serializable
    class ApplicationStatusTrackerRoute(val applicationId: String): Route

    @Serializable
    class Application(val applicationId: String): Route
}