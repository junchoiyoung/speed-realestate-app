package org.speed.realestate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.speed.realestate.ui.RealEstateViewModelProvider
import org.speed.realestate.ui.theme.backgroundColor
import org.speed.realestate.ui.viewmodel.RealEstateViewModel

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
fun RealEstateMainScreen(
) {
    val viewModel: RealEstateViewModel = viewModel(
        factory = RealEstateViewModelProvider.Factory
    )

    val realEstateNames by viewModel.realEstateNames.collectAsState()
    val realEstateTypes by viewModel.realEstateTypes.collectAsState()
    val realEstateDong by viewModel.realEstateDongs.collectAsState()
    val realEstateOptions by viewModel.realEstateOptions.collectAsState()
    val realEstateTradeTypes = listOf("매매", "전세", "월세")

    val optionTags = remember(realEstateOptions) {
        realEstateOptions
            .flatMap { optionString -> optionString.split(",").map { it.trim() } }
            .distinct()
            .sorted()
    }

    var title by remember { mutableStateOf("") }

    var showBottomSheet by remember { mutableStateOf(false) }

    val selectedTags = rememberSaveable {
        mutableStateOf<Map<String, String>>(emptyMap())
    }

    Scaffold(
        topBar = {
            RealEstateTopBar()
        },
        modifier = Modifier.background(backgroundColor),

        ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(backgroundColor),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SelectedTagsCard(
                    selectedTags = selectedTags.value,
                    onTagCardClick = {
                        title = it
                        showBottomSheet = true
                    }
                )
            }
        }
    }

    if (showBottomSheet) {

        val previouslySelectedString = selectedTags.value[title].orEmpty()

        val initialSelectionTag = if (previouslySelectedString.isBlank()) {
            emptyList()
        } else {
            previouslySelectedString.split(",").map { it.trim() }
        }

        BottomSheet(
            title = title,
            list = when (title) {
                CategoryItems.NAME -> {
                    realEstateNames
                }

                CategoryItems.TRADETYPE -> {
                    realEstateTradeTypes
                }

                CategoryItems.TYPE -> {
                    realEstateTypes
                }

                CategoryItems.DONG -> {
                    realEstateDong
                }

                else -> {
                    optionTags
                }
            },
            initialSelectionTag = initialSelectionTag,
            onDismiss = {
                showBottomSheet = false
            },
            onGetTags = { tags ->
                val tagList = tags.filterNotNull()

                val tagsString = tagList.joinToString(", ")

                val currentMap = selectedTags.value.toMutableMap()
                currentMap[title] = tagsString
                selectedTags.value = currentMap.toMap()

                viewModel.updateFilterTags(selectedTags.value.toMap())
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "SPEED",
                fontWeight = FontWeight.Bold,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = 30.sp,
                color = Color.White
            )
        },
        actions = {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "등록 아이콘",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("등록", fontSize = 14.sp)
            }
            Spacer(Modifier.width(12.dp))
        },
        // 배경색을 흰색으로 지정
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red
        )
    )
}

@Composable
fun SelectedTagsCard(
    selectedTags: Map<String, String>,
    onTagCardClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            TagGroupItem(
                CategoryItems.NAME,
                selectedTags[CategoryItems.NAME],
                onTagCardClick = onTagCardClick
            )
            TagGroupItem(
                CategoryItems.TRADETYPE,
                selectedTags[CategoryItems.TRADETYPE],
                onTagCardClick = onTagCardClick
            )
            TagGroupItem(
                CategoryItems.TYPE,
                selectedTags[CategoryItems.TYPE],
                onTagCardClick = onTagCardClick
            )
            TagGroupItem(
                CategoryItems.DONG,
                selectedTags[CategoryItems.DONG],
                onTagCardClick = onTagCardClick
            )
            TagGroupItem(
                CategoryItems.OPTION,
                selectedTags[CategoryItems.OPTION],
                isLast = true,
                onTagCardClick = onTagCardClick
            )
        }
    }
}

@Composable
fun TagGroupItem(
    title: String,
    selectedTag: String? = null,
    isLast: Boolean = false,
    onTagCardClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onTagCardClick(title)
            }
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = Color.Black
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = if (selectedTag.isNullOrEmpty()) "눌러서 태그 선택" else selectedTag,
            fontSize = 14.sp,
            color = Color.Gray
        )
        if (!isLast) {
            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContent(
    title: String,
    list: List<String>,
    initialSelectionTag: List<String>,
    onDismiss: () -> Unit,
    onGetTags: (List<String?>) -> Unit
) {
    val tagList = remember(list) {
        list.toMutableStateList()
    }

    val selectedTags = remember {
        initialSelectionTag.toMutableStateList()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
        )

        Spacer(modifier = Modifier.height(15.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
        ) {
            items(tagList) { tag ->
                BottomSheetTag(
                    name = tag,
                    select = selectedTags.contains(tag),
                    onClick = {
                        if (selectedTags.contains(tag))
                            selectedTags.remove(tag)
                        else
                            selectedTags.add(tag)
                    }
                )
            }
        }

//        FlowRow(
//            maxItemsInEachRow = 2,
//            horizontalArrangement = Arrangement.spacedBy(10.dp),
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//
//        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onGetTags(selectedTags.toList())
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
            )
        ) {
            Text(
                "확인",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    title: String,
    list: List<String>,
    initialSelectionTag: List<String>,
    onDismiss: () -> Unit,
    onGetTags: (List<String?>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
    ) {
        BottomSheetContent(
            title = title,
            list = list,
            initialSelectionTag = initialSelectionTag,
            onDismiss = onDismiss,
            onGetTags = onGetTags
        )
    }
}

@Composable
fun BottomSheetTag(
    name: String,
    select: Boolean,
    onClick: () -> Unit = {},
) {
    val containerColor = if (select) Color(0xFFE8F0FF) else Color.White
    val contentColor = if (select) Color(0xFF2962FF) else Color(0xFF333333)
    val borderColor = if (select) Color(0xFF2962FF) else Color(0xFFD9D9D9)

    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .width(80.dp)
            .height(40.dp)
            .padding(horizontal = 5.dp),

        ) {
        Text(
            text = name,
            fontSize = 12.sp,
        )
    }
}


object CategoryItems {
    const val NAME = "아파트명"
    const val DONG = "동"
    const val TYPE = "타입"
    const val OPTION = "옵션"
    const val TRADETYPE = "거래유형"
}

