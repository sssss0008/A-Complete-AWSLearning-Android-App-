package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AwsDataRepository
import com.example.ui.AppScreen
import com.example.ui.LabType
import com.example.ui.MainViewModel
import com.example.ui.components.CreatorProfileBanner
import com.example.ui.components.MetricStatCard
import com.example.ui.theme.*

@Composable
fun HomeDashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudNavyDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Creator Banner
        item {
            CreatorProfileBanner()
        }

        // Greeting Header
        item {
            Column {
                Text(
                    text = "Welcome back, ${userProfile.name}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Continue your AWS cloud engineering journey",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Continue Learning Hero Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, AwsOrange.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    AwsOrange.copy(alpha = 0.12f),
                                    CloudNavyDark.copy(alpha = 0.6f)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AwsOrange.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "IN PROGRESS • LEVEL 2",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AwsOrangeLight,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "42% Complete",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "AWS Global Infrastructure & IAM",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Next: Least-Privilege Policies & Cross-Account Roles",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { 0.42f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = AwsOrange,
                            trackColor = CloudBorderDark
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Est. 15 mins remaining",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )

                            Button(
                                onClick = { viewModel.openLesson("aws-01") },
                                colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Continue Learning",
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricStatCard(
                    title = "Learning Streak",
                    value = "${userProfile.currentStreakDays} Days",
                    subtitle = "Personal record!",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = AwsOrange,
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    title = "Daily Goal",
                    value = "${userProfile.minutesStudiedToday} / ${userProfile.dailyGoalMinutes}m",
                    subtitle = "73% completed",
                    icon = Icons.Default.Timer,
                    iconTint = CloudBlue,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricStatCard(
                    title = "Labs Completed",
                    value = "${userProfile.completedLabIds.size} Labs",
                    subtitle = "Hands-on cloud",
                    icon = Icons.Default.Science,
                    iconTint = CloudPurple,
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    title = "Projects Built",
                    value = "${userProfile.completedProjectIds.size} Projects",
                    subtitle = "Production architectures",
                    icon = Icons.Default.AccountTree,
                    iconTint = CloudSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Today's AWS Mission
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = null,
                                tint = CloudWarning,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Today's AWS Mission",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "3 of 4 Done",
                            style = MaterialTheme.typography.labelSmall,
                            color = CloudWarning
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    MissionTaskItem("Review Multi-Tier VPC Architecture", true)
                    MissionTaskItem("Launch simulated EC2 instance with SG rules", true)
                    MissionTaskItem("Configure S3 bucket lifecycle rules", true)
                    MissionTaskItem("Solve 1 Cloud Troubleshooting Incident", false) {
                        viewModel.openIncident("inc-01")
                    }
                }
            }
        }

        // Interactive Cloud Tools Hub
        item {
            Text(
                text = "Interactive Cloud Engineering Hub",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ToolLauncherCard(
                        title = "Architecture Studio",
                        subtitle = "Visual Cloud Canvas",
                        icon = Icons.Default.AccountTree,
                        badge = "STUDIO",
                        accentColor = CloudBlue,
                        onClick = { onNavigate(AppScreen.ARCHITECTURE_STUDIO) }
                    )
                }
                item {
                    ToolLauncherCard(
                        title = "AWS CLI Terminal",
                        subtitle = "Interactive Bash & CLI",
                        icon = Icons.Default.Terminal,
                        badge = "CLI",
                        accentColor = CloudSuccess,
                        onClick = { onNavigate(AppScreen.CLI_TERMINAL) }
                    )
                }
                item {
                    ToolLauncherCard(
                        title = "AI AWS Tutor",
                        subtitle = "6 Interactive Modes",
                        icon = Icons.Default.Psychology,
                        badge = "AI",
                        accentColor = AwsOrange,
                        onClick = { onNavigate(AppScreen.AI_TUTOR) }
                    )
                }
                item {
                    ToolLauncherCard(
                        title = "Troubleshooting Lab",
                        subtitle = "Debug Real Outages",
                        icon = Icons.Default.BugReport,
                        badge = "INCIDENT",
                        accentColor = CloudError,
                        onClick = { onNavigate(AppScreen.TROUBLESHOOTING) }
                    )
                }
                item {
                    ToolLauncherCard(
                        title = "Certifications",
                        subtitle = "CLF-C02 & SAA-C03",
                        icon = Icons.Default.MilitaryTech,
                        badge = "EXAM",
                        accentColor = CloudPurple,
                        onClick = { onNavigate(AppScreen.CERTIFICATION_CENTER) }
                    )
                }
                item {
                    ToolLauncherCard(
                        title = "Interview Prep",
                        subtitle = "Architectural Scenarios",
                        icon = Icons.Default.RecordVoiceOver,
                        badge = "CAREER",
                        accentColor = CloudBlueGlow,
                        onClick = { onNavigate(AppScreen.INTERVIEW_CENTER) }
                    )
                }
            }
        }

        // Recommended Services for You
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Recommended for You",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                TextButton(onClick = { onNavigate(AppScreen.SERVICE_EXPLORER) }) {
                    Text("View All", color = AwsOrange)
                }
            }
        }

        items(AwsDataRepository.services.take(4)) { svc ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openService(svc.id) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CloudNavyDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = svc.shortName.take(3),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AwsOrange
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = svc.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = svc.summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 2
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ToolLauncherCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badge: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CloudCardDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        modifier = Modifier
            .width(170.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = accentColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun MissionTaskItem(
    title: String,
    isCompleted: Boolean,
    onClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isCompleted) CloudSuccess else TextMuted,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = if (isCompleted) TextSecondary else TextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (onClick != null && !isCompleted) {
            Text(
                text = "START",
                style = MaterialTheme.typography.labelSmall,
                color = AwsOrange,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
