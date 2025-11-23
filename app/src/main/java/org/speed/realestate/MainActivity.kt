package org.speed.realestate

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.flowlayout.FlowRow
import org.speed.realestate.add.AddReadEstateSheetContent
import org.speed.realestate.constant.AppDestinations
import org.speed.realestate.data.RealEstate
import org.speed.realestate.data.Tag
import org.speed.realestate.screens.main.RealEstateMainScreen
import org.speed.realestate.screens.result.RealEstateResultScreen
import org.speed.realestate.ui.RealEstateViewModelProvider
import org.speed.realestate.ui.theme.detailBackgroundColor
import org.speed.realestate.ui.theme.detailPriceTextColor
import org.speed.realestate.ui.theme.detailRedColor
import org.speed.realestate.ui.theme.priceBoxColor
import org.speed.realestate.ui.viewmodel.RealEstateViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                val viewModel: RealEstateViewModel = viewModel(
                    factory = RealEstateViewModelProvider.Factory
                )

                NavHost(
                    navController = navController,
                    startDestination = AppDestinations.MAIN_SCREEN
                ) {
                    composable(route = AppDestinations.MAIN_SCREEN) {
                        RealEstateMainScreen(
                            viewModel = viewModel,
                            onSearchBarClick = {
                                navController.navigate(AppDestinations.RESULT_SCREEN)
                            }
                        )
                    }

                    composable(route = AppDestinations.RESULT_SCREEN) {
                        RealEstateResultScreen(
                            viewModel = viewModel,
                            onBackClick = {
                                navController.popBackStack()
                            })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateDetail(
    realEstate: RealEstate,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
    ) {
        RealEstateDetailContent(
            realEstate = realEstate,
            onDismiss = onDismiss,
            onDelete = onDelete,
            onUpdate = onUpdate,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RealEstateDetailContent(
    realEstate: RealEstate,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(detailRedColor)
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 아파트명
                Text(
                    text = realEstate.name,
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                // 거래유형
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = realEstate.tradeType,
                        color = detailRedColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 타입
            if (realEstate.type != null) {
                Text(
                    text = "${realEstate.type} 타입",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 동 호수
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            Column {
                Text(
                    text = "동",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${realEstate.dong}동",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "호수",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = "${realEstate.ho}호", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 가격
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(priceBoxColor, RoundedCornerShape(12.dp))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "$", fontSize = 26.sp, color = detailPriceTextColor)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "가격", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = realEstate.price ?: "가격정보없음",
                        fontSize = 20.sp, color = detailPriceTextColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 옵션
        if (!realEstate.option.isNullOrEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "옵션",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(mainAxisSpacing = 10.dp, crossAxisSpacing = 10.dp) {
                    realEstate.option.split(",").map { it.trim() }.forEach {
                        Box(
                            modifier = Modifier
                                .background(detailBackgroundColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = it)
                        }
                    }
                }
            }
        }

        // 특이사항
        if (realEstate.description != null) {
            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "특이사항",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = realEstate.description, fontSize = 16.sp)
            }
        }

        // 집주인 연락처
        if (realEstate.phoneNumber != null) {
            val context = LocalContext.current

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "집주인 연락처",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = realEstate.phoneNumber,
                    fontSize = 20.sp,
                    modifier = Modifier.clickable {
                        val intent =
                            Intent(Intent.ACTION_DIAL, "tel:${realEstate.phoneNumber}".toUri())
                        context.startActivity(intent)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
        ) {
            // 삭제버튼
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier
                    .weight(1f),
                border = BorderStroke(1.dp, detailRedColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = detailRedColor
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "삭제",
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.width(5.dp))

            // 수정버튼
            OutlinedButton(
                onClick = onUpdate,
                modifier = Modifier
                    .weight(1f),
                border = BorderStroke(1.dp, detailRedColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = detailRedColor
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "수정",
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.width(5.dp))

            // 닫기버튼
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f),
                border = BorderStroke(1.dp, detailRedColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = detailRedColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(text = "닫기", fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateRealEstateSheet(
    tags: List<Tag>,
    realEstate: RealEstate?,
    onSubmit: (RealEstate) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetStat = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetStat,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
    ) {
        AddReadEstateSheetContent(
            tags = tags,
            realEState = realEstate,
            onSubmit = onSubmit,
            onDismiss = onDismiss,
        )
    }
}