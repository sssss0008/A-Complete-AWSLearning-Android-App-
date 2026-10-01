package com.example.ui.screens.aitutor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@Composable
fun AiTutorScreen(viewModel: MainViewModel) {
    val messages by viewModel.tutorMessages.collectAsState()
    val currentMode by viewModel.tutorMode.collectAsState()
    var inputQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val modes = listOf("Beginner", "Technical", "Interview", "Certification", "Debugging", "Architecture")

    val promptSuggestions = listOf(
        "Explain VPC simply",
        "What is EC2?",
        "Why do we use subnets?",
        "Explain IAM roles vs users",
        "Help me understand Lambda",
        "Quiz me on S3 storage tiers"
    )

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size.coerceAtLeast(1) - 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudNavyDark)
            .padding(14.dp)
    ) {
        // Mode Selector
        Text(
            text = "AI AWS Cloud Tutor Mode",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(modes) { m ->
                val isSelected = currentMode == m
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) AwsOrange else CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) AwsOrange else CloudBorderDark),
                    modifier = Modifier.clickable { viewModel.setTutorMode(m) }
                ) {
                    Text(
                        text = m,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) CloudNavyDark else TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Message Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "user"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(AwsOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = null, tint = CloudNavyDark, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (isUser) 14.dp else 2.dp,
                            bottomEnd = if (isUser) 2.dp else 14.dp
                        ),
                        color = if (isUser) AwsOrange else CloudCardDark,
                        border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark) else null,
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isUser) CloudNavyDark else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Suggested Prompt Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(promptSuggestions) { p ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.clickable {
                        viewModel.sendTutorQuestion(p)
                    }
                ) {
                    Text(
                        text = p,
                        style = MaterialTheme.typography.labelSmall,
                        color = CloudBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Query Input Field
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(CloudCardDark, RoundedCornerShape(12.dp))
                .border(1.dp, CloudBorderDark, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = inputQuery,
                onValueChange = { inputQuery = it },
                placeholder = { Text("Ask AWS Tutor anything...", color = TextMuted, fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
            IconButton(
                onClick = {
                    if (inputQuery.isNotBlank()) {
                        viewModel.sendTutorQuestion(inputQuery)
                        inputQuery = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = AwsOrange
                )
            }
        }
    }
}
