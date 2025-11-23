package org.speed.realestate.add

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.speed.realestate.constant.CategoryItems
import org.speed.realestate.data.Tag
import kotlin.text.trim

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTagBottomSheet(onAdd: (List<Tag>) -> Unit, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
    ) {
        AddTagBottomSheetContent(onAdd = onAdd)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTagBottomSheetContent(onAdd: (List<Tag>) -> Unit) {
    var value by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val categoryList = listOf(
        CategoryItems.NAME,
        CategoryItems.TYPE,
        CategoryItems.DONG,
        CategoryItems.OPTION
    )
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "태그추가",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.width(15.dp))

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = selectedCategory ?: "카테고리 선택",
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                label = { Text("카테고리") },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                categoryList.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = value ?: "",
            onValueChange = { value = it },
            placeholder = { Text(",를 사용해 연속 입력가능") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = onClick@{
                val tags: List<Tag>

                if (value.isNullOrEmpty()) {
                    Toast.makeText(context, "태그를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@onClick
                } else if (selectedCategory.isNullOrBlank()) {
                    Toast.makeText(context, "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return@onClick
                } else {
                    tags = value!!.trim()
                        .split(",")
                        .map {
                            Tag(
                                category = selectedCategory!!.trim(),
                                value = it.trim()
                            )
                        }
                }

                onAdd(tags)

                Toast.makeText(context, "태그가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                value = null
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
        ) {
            Text(
                text = "추가",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}