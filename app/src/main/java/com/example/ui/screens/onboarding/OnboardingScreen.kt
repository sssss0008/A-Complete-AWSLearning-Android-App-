package com.example.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    viewModel: MainViewModel,
    onComplete: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var name by remember { mutableStateOf("Awiskar") }
    var experience by remember { mutableStateOf("Developer") }
    var awsKnowledge by remember { mutableStateOf("Beginner") }
    val selectedGoals = remember { mutableStateListOf("Become Solutions Architect", "Build Real Projects", "Prepare for Certification") }
    var dailyMinutes by remember { mutableIntStateOf(30) }

    val expOptions = listOf(
        "Complete Beginner",
        "Some Cloud Knowledge",
        "AWS Beginner",
        "Developer",
        "IT Professional",
        "DevOps Beginner",
        "Experienced Cloud Engineer"
    )

    val awsKnowledgeOptions = listOf("Nothing yet", "Beginner", "Intermediate", "Advanced")

    val goalOptions = listOf(
        "Learn Cloud Computing",
        "Become AWS Developer",
        "Become Cloud Engineer",
        "Become DevOps Engineer",
        "Become Solutions Architect",
        "Build Real Projects",
        "Prepare for Certification",
        "Prepare for Interviews",
        "Learn AWS for Work",
        "Learn AWS for University"
    )

    val dailyOptions = listOf(10, 20, 30, 45, 60)

    Scaffold(
        containerColor = CloudNavyDark,
        topBar = {
            if (step > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { step-- }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                    LinearProgressIndicator(
                        progress = { step / 7f },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AwsOrange,
                        trackColor = CloudCardDark
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "$step / 7",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            when (step) {
                1 -> {
                    // Screen 1 - Welcome
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(listOf(AwsOrange.copy(alpha = 0.3f), Color.Transparent))),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Brush.linearGradient(listOf(AwsOrange, AwsOrangeDark))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudQueue,
                                    contentDescription = null,
                                    tint = CloudNavyDark,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "Welcome to Learn AWS",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Build your cloud skills from the fundamentals to advanced AWS architecture. 100% free cloud engineering ecosystem created by Awiskar Acharya.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(36.dp))

                        Button(
                            onClick = { step = 2 },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                        ) {
                            Text(
                                text = "Start Learning",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                viewModel.completeOnboarding(name, experience, awsKnowledge, selectedGoals, dailyMinutes)
                                onComplete()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark)
                        ) {
                            Text(
                                text = "Explore AWS (Skip Setup)",
                                color = TextSecondary,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }

                2 -> {
                    // Screen 2 - Name
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "What should we call you?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your name will appear on your personalized roadmap and AWS completion certificate.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Enter your name", color = TextMuted) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AwsOrange,
                                unfocusedBorderColor = CloudBorderDark,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = CloudCardDark,
                                unfocusedContainerColor = CloudCardDark
                            )
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { step = 3 },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                        ) {
                            Text("Continue", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                3 -> {
                    // Screen 3 - Experience
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "What is your background?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "We will adapt our visual explanations and architecture depth.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(expOptions) { opt ->
                                val isSelected = experience == opt
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) AwsOrange.copy(alpha = 0.15f) else CloudCardDark,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) AwsOrange else CloudBorderDark
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { experience = opt }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = opt,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) AwsOrange else TextPrimary
                                        )
                                        if (isSelected) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AwsOrange)
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { step = 4 },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                        ) {
                            Text("Continue", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                4 -> {
                    // Screen 4 - AWS Experience
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "How much AWS do you already know?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Be honest — we support everything from day 1 beginners to veteran engineers.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                            awsKnowledgeOptions.forEach { opt ->
                                val isSelected = awsKnowledge == opt
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) CloudBlue.copy(alpha = 0.15f) else CloudCardDark,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) CloudBlue else CloudBorderDark
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { awsKnowledge = opt }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = opt,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) CloudBlue else TextPrimary
                                        )
                                        if (isSelected) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CloudBlue)
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { step = 5 },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                        ) {
                            Text("Continue", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                5 -> {
                    // Screen 5 - Learning Goal (Multi-select)
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "What are your primary goals?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Select all that apply to customize your learning missions.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(goalOptions) { opt ->
                                val isSelected = selectedGoals.contains(opt)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) AwsOrange.copy(alpha = 0.15f) else CloudCardDark,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) AwsOrange else CloudBorderDark
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (isSelected) selectedGoals.remove(opt) else selectedGoals.add(opt)
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = opt,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (isSelected) AwsOrange else TextPrimary
                                        )
                                        Checkbox(
                                            checked = isSelected,
                                            onCheckedChange = { checked ->
                                                if (checked) selectedGoals.add(opt) else selectedGoals.remove(opt)
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = AwsOrange)
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { step = 6 },
                            enabled = selectedGoals.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                        ) {
                            Text("Continue", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                6 -> {
                    // Screen 6 - Daily Learning Goal
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Set a daily learning commitment",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Consistent daily practice produces real cloud architects.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                            dailyOptions.forEach { mins ->
                                val isSelected = dailyMinutes == mins
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) CloudBlue.copy(alpha = 0.15f) else CloudCardDark,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) CloudBlue else CloudBorderDark
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { dailyMinutes = mins }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = if (mins == 60) "1 hour / day" else "$mins minutes / day",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) CloudBlue else TextPrimary
                                        )
                                        if (isSelected) {
                                            Icon(Icons.Default.Timer, contentDescription = null, tint = CloudBlue)
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { step = 7 },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                        ) {
                            Text("Generate My Roadmap", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                7 -> {
                    // Screen 7 - Personalized Roadmap
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Your Personalized AWS Roadmap",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Tailored for $name ($experience) • Goal: ${selectedGoals.firstOrNull() ?: "Solutions Architect"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AwsOrange
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        val roadmapPhases = listOf(
                            "Level 1: Cloud Distributed Fundamentals" to "IaaS, PaaS, SaaS, HA vs Fault Tolerance, Cloud Economics",
                            "Level 2: AWS Global Infrastructure & IAM" to "Regions, AZs, PoPs, Least-Privilege Policies, Roles",
                            "Level 3: Core Compute Academy" to "EC2 Instances, Security Groups, AMIs, Auto Scaling",
                            "Level 4: High-Durability Storage Lab" to "S3 Buckets, Storage Classes, Versioning, Lifecycle Rules",
                            "Level 5: Database Engineering" to "RDS Multi-AZ, Read Replicas, DynamoDB Key-Value Modeling",
                            "Level 6: Production VPC & Networking" to "Subnets, CIDR, Route Tables, NAT & Internet Gateways",
                            "Level 7: Serverless & Event-Driven Systems" to "Lambda, API Gateway, EventBridge, Step Functions",
                            "Level 8: DevOps & Infrastructure as Code" to "CI/CD Pipelines, CloudFormation, CloudWatch Observability"
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(roadmapPhases) { (phase, details) ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = CloudCardDark,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(CloudBlue.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = CloudBlue,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = phase,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = details,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.completeOnboarding(name, experience, awsKnowledge, selectedGoals, dailyMinutes)
                                onComplete()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                        ) {
                            Text("Launch Learning Workspace", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}
