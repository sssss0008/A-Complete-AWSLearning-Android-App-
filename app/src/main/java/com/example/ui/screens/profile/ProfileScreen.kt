package com.example.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AwsDataRepository
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.CreatorProfileBanner
import com.example.ui.components.MetricStatCard
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudNavyDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Learner Profile Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(AwsOrange, CloudBlue))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile.name.take(2).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = CloudNavyDark
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = userProfile.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = userProfile.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "Level: ${userProfile.experienceLevel}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AwsOrange
                                )
                            }
                        }

                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = TextMuted)
                        }
                    }
                }
            }
        }

        // Creator Banner
        item {
            CreatorProfileBanner()
        }

        // Learning Statistics Row
        item {
            Text("Skill Mastery & Statistics", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricStatCard(
                    title = "Learning Streak",
                    value = "${userProfile.currentStreakDays} Days",
                    subtitle = "Streak on fire",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = AwsOrange,
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    title = "Daily Target",
                    value = "${userProfile.dailyGoalMinutes} mins",
                    subtitle = "Committed time",
                    icon = Icons.Default.Timer,
                    iconTint = CloudBlue,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricStatCard(
                    title = "Lessons Passed",
                    value = "${userProfile.completedLessonIds.size}",
                    subtitle = "Cloud knowledge",
                    icon = Icons.Default.MenuBook,
                    iconTint = CloudSuccess,
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    title = "Labs Completed",
                    value = "${userProfile.completedLabIds.size}",
                    subtitle = "Simulations run",
                    icon = Icons.Default.Science,
                    iconTint = CloudPurple,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Certificate Button
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudNavyDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudSuccess.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.CERTIFICATE_VIEW) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CloudSuccess.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = CloudSuccess)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "AWS Learning Journey Certificate",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "View & Share Your Completion Certificate",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = CloudSuccess)
                }
            }
        }

        // Creator LinkedIn Connect Card
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About the Creator",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AwsOrange
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Learn AWS was crafted by Awiskar Acharya to provide a 100% free, interactive cloud engineering ecosystem. Don't just memorize AWS services—learn how real cloud engineers think, build resilient architectures, and deploy the future.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    FilledTonalButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AwsDataRepository.creatorLinkedIn))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = CloudBlue.copy(alpha = 0.15f), contentColor = CloudBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Connect with Awiskar Acharya on LinkedIn", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        var editName by remember { mutableStateOf(userProfile.name) }
        var editTitle by remember { mutableStateOf(userProfile.title) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Profile", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Learner Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Title / Aspirations") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(editName, editTitle, userProfile.dailyGoalMinutes)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CloudCardDark
        )
    }
}

@Composable
fun CertificateScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = CloudNavyDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
                Text("Certificate of Completion", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Elegant Certificate Frame
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(2.dp, Brush.linearGradient(listOf(AwsOrange, CloudBlue))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(AwsOrange, AwsOrangeDark))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = CloudNavyDark, modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "LEARN AWS ECOSYSTEM",
                        style = MaterialTheme.typography.labelSmall,
                        color = AwsOrange,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "AWS Learning Journey Completed",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Proudly Presented to:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = CloudBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "For successfully completing interactive labs in EC2, S3, Multi-Tier VPC Networking, IAM Least Privilege, DevOps Pipelines, and Cloud Architecture.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Divider(color = CloudBorderDark)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "You've completed the AWS learning journey. Keep building, keep learning, and feel free to connect with Awiskar Acharya on LinkedIn.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "*Educational verification certificate — not an official AWS certification.",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AwsDataRepository.creatorLinkedIn))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
            ) {
                Icon(Icons.Default.School, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Connect with Awiskar Acharya on LinkedIn", fontWeight = FontWeight.Bold)
            }
        }
    }
}
