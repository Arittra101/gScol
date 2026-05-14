package org.getscol.gscol.feature.consultant.domain.model

data class Certification(
    val issuedBy: String,
    val title: String,
    val issuedDate: String,
    val role: String,
    val certificateCode: String
)

data class ConsultantModel(
    val id: String,
    val name: String,
    val image: String,
    val title: String,
    val organization: String,
    val initials: String,
    val isCertified: Boolean,
    val email: String,
    val phone: String,
    /** Calendly or other scheduling URL for this consultant. */
    val bookingUrl: String,
    val officeHours: String,
    val education: String,
    val bio: String,
    val languages: List<String>,
    val destinations: List<String>,
    val certification: Certification
)