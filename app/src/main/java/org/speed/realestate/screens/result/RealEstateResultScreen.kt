package org.speed.realestate.screens.result

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.speed.realestate.data.RealEstate
import org.speed.realestate.ui.theme.backgroundColor
import org.speed.realestate.ui.viewmodel.RealEstateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RealEstateResultScreen(
    viewModel: RealEstateViewModel,
    onBackClick: () -> Unit,
) {
    val realEstates by viewModel.filteredRealEstates.collectAsState()

    Scaffold(
        topBar = { RealEstateListPageTopBar(onBackClick = onBackClick) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(paddingValues),
                contentPadding = PaddingValues(12.dp),
            ) {

                item {
                    Text(
                        text = "총 ${realEstates.size}개의 매물이 있습니다.",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.width(5.dp))
                }

                items(realEstates.size) { realEstate ->
                    RealEstateSimpleCard(
                        realEstate = realEstates[realEstate],
                        onCardClick = {}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateListPageTopBar(
    onBackClick: () -> Unit,
) {
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
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로 가기",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red
        )
    )
}

@Composable
fun RealEstateSimpleCard(
    realEstate: RealEstate,
    onCardClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onCardClick
    ) {
        Column(modifier = Modifier.padding(17.dp)) {

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = realEstate.name,
                        fontSize = 20.sp,
                        color = Color(0xFF212121),
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = realEstate.tradeType,
                            fontSize = 17.sp,
                            color = Color(0xFF1565C0)
                        )
                    }
                }

                if (realEstate.type != null) {
                    Text(
                        text = "${realEstate.type} 타입",
                        fontSize = 15.sp,
                        color = Color(0xFF616161)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF455A64)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "${realEstate.dong}동", fontSize = 15.sp, color = Color(0xFF455A64))
                Text(text = "  •  ", fontSize = 15.sp, color = Color(0xFFB0BEC5))
                Text(text = "${realEstate.ho}호", fontSize = 15.sp, color = Color(0xFF455A64))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1F1), RoundedCornerShape(8.dp))
                    .padding(vertical = 10.dp, horizontal = 15.dp)
            ) {
                Text(
                    text = realEstate.price ?: "가격정보없음",
                    fontSize = 20.sp,
                    color = Color(0xFFB71C1C)
                )

            }
        }
    }
}