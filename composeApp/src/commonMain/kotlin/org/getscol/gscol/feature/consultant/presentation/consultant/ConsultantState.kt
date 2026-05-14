package org.getscol.gscol.feature.consultant.presentation.consultant

import org.getscol.gscol.feature.consultant.domain.model.ConsultantModel

data class ConsultantState(
    val isLoading: Boolean = false,
    val items: List<ConsultantModel> = emptyList(),
    val errorMessage: String? = null,
)

