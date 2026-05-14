package org.getscol.gscol.feature.consultant.data.local_data_source

import kotlinx.coroutines.delay
import org.getscol.gscol.feature.consultant.domain.model.Certification
import org.getscol.gscol.feature.consultant.domain.model.ConsultantModel


/**
 * LocalConsultantDataSource
 *
 * Acts as a drop-in replacement for a API call.
 * The shape of [getConsultants] and [getConsultantById] mirrors what a
 * real ApiService would return (suspend functions, Result wrapper).
 * When you're ready to switch to a live API, replace only this class —
 * the Repository, ViewModel, and UI stay untouched.
 */
object LocalConsultantDataSource {

    private val consultants: List<ConsultantModel> = listOf(
        ConsultantModel(
            id = "1",
            name = "Md. Raihan Ul Islam",
            image = "raihan_ul_islam",
            title = "Education Consultant",
            organization = "SCOL",
            initials = "MR",
            isCertified = true,
            email = "raihan.getscol@gmail.com",
            phone = "+8801403024105",
            bookingUrl = "https://calendar.app.google/xWXicqPn8E1BrqhJ6",
            officeHours = "Sat–Thu, 10:00 AM – 6:00 PM",
            education = "BSc in EEE, East West University",
            bio = "Md. Raihan specializes in guiding students through the application process for top-tier universities in the UK and Australia. He is fluent in Bangla, Hindi, and English, enabling him to communicate effectively with students from diverse backgrounds and provide personalized support throughout their academic journey.",
            languages = listOf("Bangla", "Hindi", "English"),
            destinations = listOf("UK", "Australia"),
            certification = Certification(
                issuedBy = "British Council",
                title = "UK Agent & Counsellor Training Certificate",
                issuedDate = "09 April 2026",
                role = "Agent",
                certificateCode = "104930"
            )
        ),
        ConsultantModel(
            id = "2",
            name = "Sheikh Fardeen Ishaque",
            image = "fardeen_ishaque",
            title = "Education Consultant",
            organization = "SCOL",
            initials = "SF",
            isCertified = true,
            email = "fardeen.getscol@gmail.com",
            phone = "+8801845238996",
            bookingUrl = "https://calendar.app.google/EdNE9EM26DhAeonQ7",
            officeHours = "Sat–Thu, 10:00 AM – 6:00 PM",
            education = "BSc in CSE, Ahsanullah University of Science & Technology",
            bio = "Sheikh Fardeen Ishaque is a British Council certified expert specializing in guiding students through the application process for top-tier universities in the UK, Australia, and New Zealand. He is fluent in English, Bangla, and Hindi, enabling him to communicate effectively with students from diverse backgrounds and provide personalized support throughout their academic journey.",
            languages = listOf("English", "Bangla", "Hindi"),
            destinations = listOf("UK", "Australia", "New Zealand"),
            certification = Certification(
                issuedBy = "British Council",
                title = "UK Agent & Counsellor Training Certificate",
                issuedDate = "04 April 2026",
                role = "Agent",
                certificateCode = "103315"
            )
        ),
        ConsultantModel(
            id = "3",
            name = "Md. Shafayat Jamil",
            image = "shafayat_jamil",
            title = "Founder & CEO",
            organization = "SCOL",
            initials = "MS",
            isCertified = true,
            email = "contact@getscol.com",
            phone = "+8801872111917",
            bookingUrl = "https://calendar.app.google/2XvfNP6W26JbXGjP8",
            officeHours = "Sat–Thu, 10:00 AM – 6:00 PM",
            education = "BSc in CSE, Ahsanullah University of Science & Technology",
            bio = "Md. Shafayat Jamil is the Founder & CEO of SCOL and a British Council certified expert specializing in student admissions for top-tier universities in the UK, Australia, and New Zealand. Passionate about global education and student success, he guides students through university admissions, visas, and career pathways abroad.",
            languages = listOf("Bangla", "Hindi", "English"),
            destinations = listOf("UK", "Australia", "New Zealand"),
            certification = Certification(
                issuedBy = "British Council",
                title = "UK Agent & Counsellor Training Certificate",
                issuedDate = "07 February 2026",
                role = "Agent",
                certificateCode = "96243"
            )
        )
    )

    /** Mimics GET /consultants */
    suspend fun getConsultants(): List<ConsultantModel> {
        //Simulate network delay
        delay(500)
        return consultants
    }

    /** Mimics GET /consultants/{id} */
    suspend fun getConsultantById(id: String): ConsultantModel? {
        //Simulate network delay
        delay(500)
        return consultants.find { it.id == id }
    }
}