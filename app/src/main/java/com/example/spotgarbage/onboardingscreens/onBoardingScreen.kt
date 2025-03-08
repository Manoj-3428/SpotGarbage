package com.example.spotgarbage.onboardingscreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.spotgarbage.R
import com.example.spotgarbage.ui.theme.primary_dark
import com.google.firebase.auth.FirebaseAuth

data class OnboardingPage(val image: Int, val title: String, val description: String)
val pages = listOf(
    OnboardingPage(R.drawable.locate, "Grant Permissions", "Enable camera and location access to report garbage effectively."),
    OnboardingPage(R.drawable.pro, "Create Your Profile", "Join the community and start making a difference."),
    OnboardingPage(R.drawable.upload, "Scan & Report Garbage", "Use your camera to detect and report waste in real time."),
    OnboardingPage(R.drawable.onboard, "Together for a Cleaner Society", "We will process your report within 2 days. Let's build a cleaner future together!")
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState { pages.size }
    val coroutineScope = rememberCoroutineScope()
    val auth= FirebaseAuth.getInstance()
    if(auth.currentUser==null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val currentPage = pages[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = currentPage.image),
                        contentDescription = null,
                        modifier = Modifier.size(280.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = currentPage.title,
                        fontSize = 24.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentPage.description,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(pages.size) { index ->
                    val color =
                        if (index == pagerState.currentPage) primary_dark else Color.LightGray
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(color)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = if (pagerState.currentPage == pages.size - 1) Arrangement.Center else Arrangement.SpaceBetween
            ) {
                if (pagerState.currentPage < pages.size - 1) {
                    TextButton(onClick = { navController.navigate("login"){popUpTo(0)} }) {
                        Text("Skip", fontSize = 18.sp, color = Color.Gray)
                    }
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage < pages.size - 1) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else {
                                navController.navigate("login")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primary_dark),
                    shape = RoundedCornerShape(5.dp),
                    modifier = if (pagerState.currentPage == pages.size - 1) Modifier.width(250.dp) else Modifier.wrapContentSize()
                ) {
                    Text(
                        if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
                        color = Color.White
                    )
                }
            }
        }
    }
    else{
        navController.navigate("login")
    }
}