package com.example.ui.screens.interview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.repository.AwsDataRepository
import com.example.ui.theme.*

@Composable
fun InterviewScreen(onBack: () -> Unit) {
    var expandedQuestionId by remember { mutableStateOf<String?>(null) }

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
                Text(
                    text = "AWS Cloud Interview Center",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Technical Cloud Engineering Questions",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Curated senior solutions architect and DevOps interview questions with model answers",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            items(AwsDataRepository.interviewQuestions) { q ->
                val isExpanded = expandedQuestionId == q.id
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CloudBlue.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${q.topic} • ${q.difficulty}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CloudBlue,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = q.question,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { expandedQuestionId = if (isExpanded) null else q.id }
                        ) {
                            Text(if (isExpanded) "Hide Model Answer ▲" else "View Model Answer & Talking Points ▼", color = AwsOrange)
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Key Talking Points",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AwsOrange
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            q.keyTalkingPoints.forEach { pt ->
                                Text("• $pt", style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(vertical = 2.dp))
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CloudNavyDark,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Sample Candidate Answer", style = MaterialTheme.typography.labelSmall, color = CloudSuccess, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(q.sampleAnswer, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
