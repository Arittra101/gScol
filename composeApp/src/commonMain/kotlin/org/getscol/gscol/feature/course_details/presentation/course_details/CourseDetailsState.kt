package org.getscol.gscol.feature.course_details.presentation.course_details

import org.getscol.gscol.feature.academic_form.di.academicModule
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails

data class CourseDetailsState(
    val courseDetails: CourseDetails? = null,
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEligible: Boolean = false,
    val alreadyApplied: Boolean = false,
    val isLogin: Boolean = false
){
    fun buttonShouldEnable(): Boolean {
        return if (!isLogin) true
        else if (isEligible && !alreadyApplied) true
        else false
    }

    fun shouldRedirectToLogin() : Boolean {
        return isLogin.not()
    }

    fun applyBtnTextMsg(): String {
        return if (!isLogin || (isEligible && !alreadyApplied)) "Apply Now"
        else if (!isEligible) "You are not eligible"
        else "Already Applied"
    }
}
