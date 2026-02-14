package org.getscol.gscol.feature.academic_form.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.ConfirmationBottomSheet
import org.getscol.gscol.navigation.Navigator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.academic_form
import scol.composeapp.generated.resources.academic_form_bottom_sheet_msg


@Composable
fun AcademicFormRoute(navigator: Navigator, viewModel: AcademicViewmodel = koinViewModel()) {

    val uiState by viewModel.academicUiState.collectAsState()
    AcademicScreen(navigator,uiState,onAction = viewModel::onAction)

    /* val state = viewModel.academicUiState.collectAsState()
     val uiState = state.value  //this is alternative for learning purposes
     val uiState = viewModel.academicUiState.collectAsState().value
     */
}


@Composable
fun AcademicScreen(
    navigator: Navigator,
    uiState: AcademicUiState,
    onAction: (AcademicFormAction) -> Unit
) {
    BaseScreen(title = stringResource(Res.string.academic_form), showLoader = uiState.showLoader) {
        EligibilityScreen(uiState,onAction)
    }
}


@Composable
@Preview
fun EligibilityScreen(
    uiState: AcademicUiState = AcademicUiState(),
    onAction: (AcademicFormAction) -> Unit
) {

    var countryExpanded by remember { mutableStateOf(false) }
    var programExpanded by remember { mutableStateOf(false) }
    var testTypeExpand by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "GPA / CGPA",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // GPA Section - Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GpaInputField(
                label = "SSC",
                value = uiState.ssc?.gpa ?: "",
                onValueChange = { onAction(AcademicFormAction.OnSscChange(it)) },
                modifier = Modifier.weight(1f),
              /*  readOnly = uiState.ssc?.gpa != null*/
            )

            GpaInputField(
                label = "HSC",
                value = uiState.hsc?.gpa ?: "",
                onValueChange = { onAction(AcademicFormAction.OnHscChange(it)) },
                modifier = Modifier.weight(1f),
                /*readOnly = uiState.hsc?.gpa != null*/
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // GPA Section - Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GpaInputField(
                label = "Bachelor's",
                value = uiState.bsc?.gpa ?: "",
                onValueChange = { onAction(AcademicFormAction.OnBscChange(it))},
                modifier = Modifier.weight(1f),
             /*   readOnly = uiState.bsc?.gpa != null*/
            )

            GpaInputField(
                label = "Master's",
                value = uiState.msc?.gpa ?: "",
                onValueChange = { onAction(AcademicFormAction.OnMscChange(it))},
                modifier = Modifier.weight(1f),
/*                readOnly = uiState.msc?.gpa != null*/
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        GpaInputField(
            label = "Last institute name",
            value = uiState.lastInstitute.orEmpty(),
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            KeyboardType.Text,
            readOnly = true
        )

        Spacer(modifier = Modifier.height(35.dp))

        // English Test Scores Section
        Text(
            text = "English Test Scores",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Test Type",
            fontSize = 16.sp,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.height(6.dp))

        MaterialDropdown(
            selectedValue = uiState.selectedTestType?.testName.orEmpty(),
            placeholder = "Select Test Type",
            options = uiState.testTypeList?.toListDropDownUiModel2() ?: emptyList(),
            expanded = testTypeExpand,
            onExpandedChange = { testTypeExpand= it },
            onOptionSelected = {
                onAction(AcademicFormAction.OnTestTypeChange(it.id))
            }
        )

        if (uiState.selectedTestType != null) {
            Spacer(modifier = Modifier.height(6.dp))

            if(uiState.selectedTestType.testName == "PTE"){
                GpaInputField(
                    label = "Score",
                    value = (uiState.selectedTestType.overallScore?: 0.0).toString(),
                    onValueChange = {

                    },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = uiState.selectedTestType.overallScore != null
                )
                Spacer(modifier = Modifier.height(17.dp))
            }

            else {
                // English Test Scores - Row 1 there need a condition
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GpaInputField(
                        label = "Speaking",
                        value = (uiState.selectedTestType.sections?.getOrNull(2)?.score ?: ""),                        onValueChange = {
                            val testSectionId = uiState.selectedTestType.sections?.getOrNull(2)?.id
                            if (testSectionId != null) {
                                onAction(
                                    AcademicFormAction.OnTestScoreChange(testSectionId, it)
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                      //  readOnly = uiState.selectedTestType.sections?.getOrNull(2)?.score != null
                    )

                    GpaInputField(
                        label = "Listening",
                        value = (uiState.selectedTestType.sections?.getOrNull(0)?.score ?: ""),
                        onValueChange = {
                            val testSectionId = uiState.selectedTestType.sections?.getOrNull(0)?.id
                            if (testSectionId != null) {
                                onAction(
                                    AcademicFormAction.OnTestScoreChange(testSectionId, it)
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                       // readOnly = uiState.selectedTestType.sections?.getOrNull(0) != null
                    )
                }

                Spacer(modifier = Modifier.height(17.dp))

                // English Test Scores - Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GpaInputField(
                        label = "Reading",
                        value = (uiState.selectedTestType.sections?.getOrNull(1)?.score ?: ""),
                        onValueChange = {
                            val testSectionId = uiState.selectedTestType.sections?.getOrNull(1)?.id
                            if (testSectionId != null) {
                                onAction(
                                    AcademicFormAction.OnTestScoreChange(testSectionId, it)
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
//                        readOnly = uiState.selectedTestType.sections?.getOrNull(1) != null
                    )

                    GpaInputField(
                        label = "Writing",
                        value = (uiState.selectedTestType.sections?.getOrNull(3)?.score ?: 0.0).toString(),
                        onValueChange = {
                            val testSectionId = uiState.selectedTestType.sections?.getOrNull(3)?.id
                            if (testSectionId != null) {
                                onAction(
                                    AcademicFormAction.OnTestScoreChange(testSectionId, it)
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                       // readOnly = uiState.selectedTestType.sections?.getOrNull(3) != null
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(12.dp))

        // Country Dropdown - CONVERTED TO MATERIAL STYLE
        Text(
            text = "Country",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        MaterialDropdown(
            selectedValue = uiState.selectedCountryPreference?.name.orEmpty(),
            placeholder = "Select Country",
            options = uiState.programmeCountryList?.toListDropDownUiModel() ?: emptyList(),
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = it },
            onOptionSelected = {
                onAction(AcademicFormAction.OnCountryPrefChange(it.itemName,it.id))
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Program Interest Dropdown - CONVERTED TO MATERIAL STYLE
        Text(
            text = "Program Interest",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        MaterialDropdown(
            selectedValue = uiState.selectedProgrammePreference?.name.orEmpty(),
            placeholder = "Select Programme Interest",
            options = uiState.programmePreferenceList?.toListDropDownUiModel() ?: emptyList(),
            expanded = programExpanded,
            onExpandedChange = { programExpanded = it },
            onOptionSelected = {
                onAction(AcademicFormAction.OnProgrammePrefChange(it.itemName,it.id))
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Check Eligibility Button
        Button(
            onClick = {
                showBottomSheet = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D3838)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Check Eligibility",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
    ConfirmationBottomSheet(
        showBottomSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        title = "Are You Sure?",
        message = stringResource(Res.string.academic_form_bottom_sheet_msg),
        confirmButtonText = "Yes, Submit",
        cancelButtonText = "Go Back",
        onConfirm = {
            // Handle submission logic
            println("Confirmed!")
        }
    )
}

// NEW MATERIAL DROPDOWN COMPONENT
@Composable
fun MaterialDropdown(
    selectedValue: String,
    placeholder: String,
    options: List<DropDownUiModel>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (DropDownUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Dropdown Trigger Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFD9E8ED))
                .clickable { onExpandedChange(true) }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedValue.ifEmpty { placeholder },
                    fontSize = 16.sp,
                    color = if (selectedValue.isEmpty()) Color.DarkGray else Color.Black
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = Color.Gray
                )
            }
        }

        // Material Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.itemName,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun GpaInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    readOnly: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD9E8ED),
                focusedContainerColor = Color(0xFFD9E8ED),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 15.sp
            )
        )
    }
}