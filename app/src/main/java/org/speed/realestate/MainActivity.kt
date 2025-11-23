package org.speed.realestate

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.speed.realestate.constant.CategoryItems
import org.speed.realestate.data.RealEstate
import org.speed.realestate.data.Tag
import org.speed.realestate.main.BottomSheetTag
import org.speed.realestate.main.RealEstateMainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                RealEstateMainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReadEstateSheet(
    tags: List<Tag>,
    onSubmit: (RealEstate) -> Unit,
    onDismiss: () -> Unit,
    onAdd: (List<Tag>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
    ) {
        AddReadEstateSheetContent(
            tags = tags,
            onSubmit = onSubmit,
            onDismiss = onDismiss,
            onAdd = onAdd
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReadEstateSheetContent(
    tags: List<Tag>,
    realEState: RealEstate? = null,
    onSubmit: (RealEstate) -> Unit,
    onDismiss: () -> Unit,
    onAdd: ((List<Tag>) -> Unit)? = null,
) {
    val context = LocalContext.current

    var addTagBottomSheetState by remember { mutableStateOf(false) }

    var name: String? by remember {
        if (realEState != null) mutableStateOf(realEState.name)
        else mutableStateOf(null)
    }
    var dong: String? by remember {
        if (realEState != null) mutableStateOf(realEState.dong)
        else mutableStateOf(null)
    }
    var tradeType: String? by remember {
        if (realEState != null) mutableStateOf(realEState.tradeType)
        else mutableStateOf(null)
    }
    val option: MutableList<String> = remember {
        if (realEState != null && realEState.option != null)
            mutableStateListOf(realEState.option)
        else mutableStateListOf()
    }
    var ho: String? by remember {
        if (realEState != null) mutableStateOf(realEState.ho)
        else mutableStateOf(null)
    }
    var price: String? by remember {
        if (realEState != null && realEState.price != null)
            mutableStateOf(realEState.price)
        else mutableStateOf(null)
    }
    var type: String? by remember {
        if (realEState != null && realEState.type != null)
            mutableStateOf(realEState.type)
        else mutableStateOf(null)
    }
    var phoneNumber: String? by remember {
        if (realEState != null && realEState.phoneNumber != null)
            mutableStateOf(realEState.phoneNumber)
        else mutableStateOf(null)
    }
    var description: String? by remember {
        if (realEState != null && realEState.description != null)
            mutableStateOf(realEState.description)
        else mutableStateOf(null)
    }

    val tradeTypeOptions = listOf("매매", "전세", "월세")

    val apartmentNameList =
        remember(tags) { tags.filter { it.category == CategoryItems.NAME }.map { it.value } }
    val typeList =
        remember(tags) { tags.filter { it.category == CategoryItems.TYPE }.map { it.value } }
    val dongList =
        remember(tags) { tags.filter { it.category == CategoryItems.DONG }.map { it.value } }
    val optionList =
        remember(tags) { tags.filter { it.category == CategoryItems.OPTION }.map { it.value } }

    var selectTagValueState by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (realEState == null){
                Text("매물 등록", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            else {
                Text("매물 수정", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (onAdd != null) {
                OutlinedButton(
                    onClick = {
                        addTagBottomSheetState = true
                    },
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = "태그 추가",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        StyledSelectButton(
            label = CategoryItems.NAME,
            value = name ?: CategoryItems.NAME,
            onClick = {
                category = CategoryItems.NAME
                selectTagValueState = true
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        //매물 정보
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier
                    .weight(2f)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                StyledSelectButton(
                    label = CategoryItems.TYPE,
                    value = type ?: CategoryItems.TYPE,
                    onClick = {
                        category = CategoryItems.TYPE
                        selectTagValueState = true
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                StyledSelectButton(
                    label = CategoryItems.DONG,
                    value = dong ?: CategoryItems.DONG,
                    onClick = {
                        category = CategoryItems.DONG
                        selectTagValueState = true
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.width(8.dp))

            OutlinedTextField(
                value = ho ?: "",
                onValueChange = { ho = it },
                label = { Text("호") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(8.dp))

        // 거래 유형
        var tradeTypeExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = tradeTypeExpanded,
            onExpandedChange = { tradeTypeExpanded = !tradeTypeExpanded }) {

            OutlinedTextField(
                value = tradeType ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("거래유형") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = tradeTypeExpanded,
                onDismissRequest = { tradeTypeExpanded = false }) {
                tradeTypeOptions.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            tradeType = type
                            tradeTypeExpanded = false
                        }
                    )
                }
            }

        }

        Spacer(Modifier.height(12.dp))

        // 가격
        OutlinedTextField(
            value = price ?: "",
            onValueChange = { price = it },
            label = { Text("가격 (원)") },
            placeholder = { Text("예: 500000000") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        //옵션
        StyledSelectButton(
            label = CategoryItems.OPTION,
            value = if (option.isEmpty()) CategoryItems.OPTION else option.joinToString(", "),
            onClick = {
                category = CategoryItems.OPTION
                selectTagValueState = true
            },
        )

        Spacer(Modifier.height(12.dp))

        //집주인 전화번호
        OutlinedTextField(
            value = phoneNumber ?: "",
            onValueChange = { phoneNumber = it },
            label = { Text("전화번호") },
            placeholder = { Text("-없이 숫자만 입력") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // 특이사항
        OutlinedTextField(
            value = description ?: "",
            onValueChange = { description = it },
            label = { Text("특이사항") },
            placeholder = { Text("매물에 대한 상세 설명을 입력하세요") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5
        )

        Spacer(Modifier.height(20.dp))

        // 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = onDismiss) {
                Text("취소")
            }
            Button(
                onClick = {
                    if (name.isNullOrEmpty()) {
                        Toast.makeText(context, "아파트명을 선택해주세요.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (type.isNullOrEmpty()) {
                        Toast.makeText(context, "타입을 선택해주세요.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (dong.isNullOrEmpty()) {
                        Toast.makeText(context, "동을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (ho.isNullOrEmpty()) {
                        Toast.makeText(context, "호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val newRealEstate = RealEstate(
                        id = realEState?.id ?: 0,
                        name = name!!,
                        dong = dong!!,
                        ho = ho!!,
                        price = price,
                        option = option.joinToString(", "),
                        type = type,
                        tradeType = tradeType!!,
                        phoneNumber = phoneNumber,
                        description = description
                    )
                    onSubmit(newRealEstate)
                    Toast.makeText(context, "매물이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                if (onAdd == null)
                    Text("수정하기", color = Color.White)
                else
                    Text("등록하기", color = Color.White)
            }
        }
    }

    if (addTagBottomSheetState && onAdd != null) {
        AddTagBottomSheet(
            onAdd = { onAdd(it) },
            onDismiss = { addTagBottomSheetState = false })
    }

    if (selectTagValueState) {
        SelectTagValue(
            list = when (category) {
                CategoryItems.NAME -> apartmentNameList
                CategoryItems.TYPE -> typeList
                CategoryItems.DONG -> dongList
                else -> optionList
            },
            onValue = {
                when (category) {
                    CategoryItems.NAME -> name = it
                    CategoryItems.TYPE -> type = it
                    CategoryItems.DONG -> dong = it
                    else -> option.add(it)
                }
            },
            onDismiss = {
                selectTagValueState = false
            }
        )
    }
}
@Composable
fun StyledSelectButton(
    label: String,
    value: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value ?: label,
                color = if (value != null) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTagValue(
    list: List<String>,
    onValue: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
    ) {
        SelectTagValueContent(
            list = list,
            onValue = onValue,
            onDismiss = onDismiss,
        )
    }
}

@Composable
fun SelectTagValueContent(
    list: List<String>,
    onValue: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
        ) {
            items(list) { value ->
                BottomSheetTag(
                    name = value,
                    select = false,
                    onClick = {
                        onValue(value)
                        onDismiss()
                    }
                )
            }
        }
    }
}
