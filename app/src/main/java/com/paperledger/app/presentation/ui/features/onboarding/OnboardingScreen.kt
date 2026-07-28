package com.paperledger.app.presentation.ui.features.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.BorderStroke
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.paperledger.app.core.Routes
import com.paperledger.app.presentation.AuthViewModel
import com.paperledger.app.presentation.ui.features.trade.MT5_BLUE
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val title:String,
    val description:String,
    val imageUrl:String,
    val contentDescription:String
)

@Composable
fun OnboardingScreen(
    navController:NavController,
    authViewModel:AuthViewModel
) {
    val pages=listOf(
        OnboardingPageData(
            title="Welcome to PaperLedger",
            description="Your professional companion for stock and asset trading. Analyze charts, manage risk, and execute trades instantly.",
            imageUrl="https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&q=80",
            contentDescription="Candlestick trading chart"
        ),
        OnboardingPageData(
            title="Powered by Alpaca Broker",
            description="Benefit from Alpaca's robust, API-first brokerage. Enjoy commission-free trading, lightning-fast execution, and institutional-grade reliability.",
            imageUrl="https://images.unsplash.com/photo-1642543348745-03b1219733d9?w=800&q=80",
            contentDescription="Trading desk with multiple market monitors"
        ),
        OnboardingPageData(
            title="Start Your Journey",
            description="The markets are waiting. Manage your watchlist, optimize your portfolio, and master the art of trading. Enjoy the journey!",
            imageUrl="https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?w=800&q=80",
            contentDescription="Stock market ticker board"
        )
    )

    val pagerState=rememberPagerState(pageCount={pages.size})
    val scope=rememberCoroutineScope()

    Scaffold(
        bottomBar={
            Row(
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(horizontal=24.dp, vertical=32.dp),
                horizontalArrangement=Arrangement.SpaceBetween,
                verticalAlignment=Alignment.CenterVertically
            ) {
                PagerIndicator(
                    pagerCount=pages.size,
                    currentPage=pagerState.currentPage
                )

                Button(
                    onClick={
                        if (pagerState.currentPage < pages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            // Onboarding complete — go to sign-up and remove
                            // onboarding + splash from the back stack entirely
                            navController.navigate(Routes.SIGN_UP) {
                                popUpTo(Routes.SPLASH_SCREEN) { inclusive=true }
                                launchSingleTop=true
                            }
                        }
                    },
                    colors=ButtonDefaults.buttonColors(containerColor=MT5_BLUE),
                    contentPadding=PaddingValues(horizontal=24.dp, vertical=12.dp)
                ) {
                    Text(
                        text=if (pagerState.currentPage == pages.size - 1) "GET STARTED" else "NEXT",
                        fontWeight=FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state=pagerState,
            modifier=Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) { pageIndex ->
            OnboardingPageContent(pageData=pages[pageIndex])
        }
    }
}

@Composable
fun OnboardingPageContent(pageData:OnboardingPageData) {
    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(horizontal=32.dp, vertical=48.dp),
        horizontalAlignment=Alignment.CenterHorizontally,
        verticalArrangement=Arrangement.Center
    ) {
        Text(
            text=pageData.title,
            style=MaterialTheme.typography.headlineMedium,
            fontWeight=FontWeight.Black,
            textAlign=TextAlign.Center,
            color=MaterialTheme.colorScheme.onSurface,
            letterSpacing=1.sp
        )

        Spacer(modifier=Modifier.height(16.dp))

        Text(
            text=pageData.description,
            style=MaterialTheme.typography.bodyLarge,
            textAlign=TextAlign.Center,
            color=Color.Gray,
            modifier=Modifier.padding(horizontal=16.dp)
        )

        Spacer(modifier=Modifier.weight(1f))

        OnboardingImage(
            modifier=Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f),
            imageUrl=pageData.imageUrl,
            contentDescription=pageData.contentDescription
        )

        Spacer(modifier=Modifier.weight(1.2f))
    }
}

@Composable
fun PagerIndicator(pagerCount:Int, currentPage:Int) {
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
        repeat(pagerCount) { index ->
            val isSelected=index == currentPage
            Box(
                modifier=Modifier
                    .size(if (isSelected) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MT5_BLUE else Color.LightGray)
            )
        }
    }
}

@Composable
fun OnboardingImage(modifier:Modifier, imageUrl:String, contentDescription:String) {
    Surface(
        modifier=modifier,
        color=Color.Gray.copy(alpha=0.05f),
        shape=androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        border=BorderStroke(1.dp, Color.LightGray.copy(alpha=0.3f))
    ) {
        AsyncImage(
            model=imageUrl,
            contentDescription=contentDescription,
            contentScale=ContentScale.Crop,
            modifier=Modifier.fillMaxSize()
        )
    }
}