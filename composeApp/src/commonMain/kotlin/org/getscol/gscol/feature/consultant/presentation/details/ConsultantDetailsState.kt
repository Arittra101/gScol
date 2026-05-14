package org.getscol.gscol.feature.consultant.presentation.details

import org.getscol.gscol.feature.consultant.domain.model.ConsultantModel

data class ConsultantDetailsState(
    val isLoading: Boolean = false,
    val consultant: ConsultantModel? = null,
    val errorMessage: String? = null,
)

