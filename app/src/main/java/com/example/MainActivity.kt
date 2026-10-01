package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.AppTopHeader
import com.example.ui.screens.aitutor.AiTutorScreen
import com.example.ui.screens.architecture.ArchitectureStudioScreen
import com.example.ui.screens.certifications.CertificationScreen
import com.example.ui.screens.home.HomeDashboardScreen
import com.example.ui.screens.interview.InterviewScreen
import com.example.ui.screens.labs.LabsScreen
import com.example.ui.screens.learn.LearnScreen
import com.example.ui.screens.learn.LessonDetailScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.CertificateScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.projects.ProjectsScreen
import com.example.ui.screens.resources.CheatSheetsScreen
import com.example.ui.screens.resources.FlashcardsScreen
import com.example.ui.screens.services.ServiceComparisonScreen
import com.example.ui.screens.services.ServiceDetailScreen
import com.example.ui.screens.services.ServiceExplorerScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.terminal.AwsCliTerminalScreen
import com.example.ui.screens.troubleshooting.TroubleshootingScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnAWSTheme {
                MainApp()
            }
        }
    }
}

data class BottomNavItem(
    val screen: AppScreen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainApp(viewModel: MainViewModel = viewModel()) {
    val userProfile by viewModel.userProfile.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    var showOnboarding by remember(userProfile.isOnboarded) {
        mutableStateOf(!userProfile.isOnboarded)
    }

    // Hardware and gesture BackHandler
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateBack()
    }

    if (showOnboarding) {
        OnboardingScreen(
            viewModel = viewModel,
            onComplete = { showOnboarding = false }
        )
        return
    }

    val bottomNavItems = listOf(
        BottomNavItem(AppScreen.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(AppScreen.LEARN, "Learn", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
        BottomNavItem(AppScreen.LABS, "Labs", Icons.Filled.Science, Icons.Outlined.Science),
        BottomNavItem(AppScreen.PROJECTS, "Projects", Icons.Filled.AccountTree, Icons.Outlined.AccountTree),
        BottomNavItem(AppScreen.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    val isRootBottomScreen = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.LEARN,
        AppScreen.LABS,
        AppScreen.PROJECTS,
        AppScreen.PROFILE
    )

    Scaffold(
        containerColor = CloudNavyDark,
        topBar = {
            if (isRootBottomScreen) {
                AppTopHeader(
                    title = "Learn AWS",
                    subtitle = "by Awiskar Acharya",
                    streakDays = userProfile.currentStreakDays,
                    onSearchClick = { viewModel.navigateTo(AppScreen.SERVICE_EXPLORER) },
                    onSettingsClick = { viewModel.navigateTo(AppScreen.SETTINGS) }
                )
            }
        },
        bottomBar = {
            if (isRootBottomScreen) {
                NavigationBar(
                    containerColor = CloudSurfaceDark,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentScreen == item.screen
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(item.screen) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CloudNavyDark,
                                unselectedIconColor = TextSecondary,
                                selectedTextColor = AwsOrange,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = AwsOrange
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeDashboardScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                AppScreen.LEARN -> LearnScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                AppScreen.LABS -> LabsScreen(viewModel = viewModel)
                AppScreen.PROJECTS -> ProjectsScreen(viewModel = viewModel)
                AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })

                // Sub-screens
                AppScreen.LESSON_DETAIL -> LessonDetailScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                AppScreen.SERVICE_EXPLORER -> ServiceExplorerScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                AppScreen.SERVICE_DETAIL -> ServiceDetailScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                AppScreen.SERVICE_COMPARISON -> ServiceComparisonScreen(onBack = { viewModel.navigateBack() })
                AppScreen.ARCHITECTURE_STUDIO -> ArchitectureStudioScreen()
                AppScreen.CLI_TERMINAL -> AwsCliTerminalScreen(viewModel = viewModel)
                AppScreen.AI_TUTOR -> AiTutorScreen(viewModel = viewModel)
                AppScreen.TROUBLESHOOTING -> TroubleshootingScreen(viewModel = viewModel)
                AppScreen.CERTIFICATION_CENTER -> CertificationScreen(onBack = { viewModel.navigateBack() })
                AppScreen.INTERVIEW_CENTER -> InterviewScreen(onBack = { viewModel.navigateBack() })
                AppScreen.CHEAT_SHEETS -> CheatSheetsScreen(onBack = { viewModel.navigateBack() })
                AppScreen.FLASHCARDS -> FlashcardsScreen(onBack = { viewModel.navigateBack() })
                AppScreen.NOTES -> CheatSheetsScreen(onBack = { viewModel.navigateBack() })
                AppScreen.SETTINGS -> SettingsScreen(onBack = { viewModel.navigateBack() })
                AppScreen.CERTIFICATE_VIEW -> CertificateScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            }
        }
    }
}
