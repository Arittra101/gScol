package org.getscol.gscol.feature.consultant.presentation.consultant

sealed interface ConsultantAction {
    data object Retry : ConsultantAction
    data class SelectConsultant(val consultantId: String) : ConsultantAction
    data class BookSession(val consultantId: String) : ConsultantAction
}

