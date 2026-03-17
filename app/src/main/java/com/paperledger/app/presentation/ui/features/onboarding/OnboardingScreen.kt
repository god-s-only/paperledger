package com.paperledger.app.presentation.ui.features.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.paperledger.app.core.Routes
import com.paperledger.app.presentation.ui.features.trade.MT5_BLUE
import kotlinx.coroutines.launch

// Data structure to hold the unique content for each page
data class OnboardingPageData(
    val title: String,
    val description: String,
    val content: @Composable (Modifier) -> Unit
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pages = listOf(
        OnboardingPageData(
            title = "Welcome to PaperLedger",
            description = "Your professional companion for stock and asset trading. Analyze charts, manage risk, and execute trades instantly.",
            content = { modifier ->
                PlaceholderImageComposable(modifier = modifier, text = "Stock Trading Welcome")
            }
        ),
        OnboardingPageData(
            title = "Powered by Alpaca Broker",
            description = "Benefit from Alpaca's robust, API-first brokerage. Enjoy commission-free trading, lightning-fast execution, and institutional-grade reliability.",
            content = { modifier ->
                PlaceholderImageComposable(modifier = modifier, text = "Alpaca Broker Integration")
            }
        ),
        OnboardingPageData(
            title = "Start Your Journey",
            description = "The markets are waiting. Manage your watchlist, optimize your portfolio, and master the art of trading. Enjoy the journey!",
            content = { modifier ->
                PlaceholderImageComposable(modifier = modifier, text = "Ready to Trade")
            }
        )
    )

    // Pager and Coroutine State
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 2. Pager Indicator (Dots)
                PagerIndicator(pagerCount = pages.size, currentPage = pagerState.currentPage)

                // 3. Dynamic Navigation Button (Next/Get Started)
                Button(
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            // Finish Onboarding and move to Auth or Chart
                            navController.navigate(Routes.CHART_SCREEN) {
                                popUpTo(Routes.ONBOARDING_SCREEN) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MT5_BLUE),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = if (pagerState.currentPage == pages.size - 1) "GET STARTED" else "NEXT",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        // 4. Main Pager Implementation
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) { pageIndex ->
            OnboardingPageContent(pageData = pages[pageIndex])
        }
    }
}

// 5. Reusable Component for a Single Page Layout
@Composable
fun OnboardingPageContent(pageData: OnboardingPageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Text at the top
        Text(
            text = pageData.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = pageData.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f)) // Push image to center vertically

        // 6. The Requested Image Composable Container
        pageData.content(
            Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f) // Keep it a consistent square
        )

        Spacer(modifier = Modifier.weight(1.2f)) // Ensure image doesn't hit the bottom buttons
    }
}

@Composable
fun PagerIndicator(pagerCount: Int, currentPage: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pagerCount) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .size(if (isSelected) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MT5_BLUE else Color.LightGray)
            )
        }
    }
}

@Composable
fun PlaceholderImageComposable(modifier: Modifier, text: String) {
    Surface(
        modifier = modifier,
        color = Color.Gray.copy(alpha = 0.05f),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = "[ IMAGE PLACEHOLDER ]\n$text",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}