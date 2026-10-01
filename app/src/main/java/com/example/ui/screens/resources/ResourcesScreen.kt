package com.example.ui.screens.resources

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AwsDataRepository
import com.example.ui.theme.*

@Composable
fun CheatSheetsScreen(onBack: () -> Unit) {
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
                Text("AWS Quick Cheat Sheets", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
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
            items(AwsDataRepository.cheatSheets) { sheet ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(sheet.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AwsOrange)
                        Text(sheet.category, style = MaterialTheme.typography.labelSmall, color = CloudBlue)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Quick Reference", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        sheet.quickReference.forEach { (k, v) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(k, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(v, style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace), color = TextPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Common Commands", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        sheet.commonCommands.forEach { (cmdName, cmdCode) ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(cmdName, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(cmdCode, style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace), color = CloudBlue)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardsScreen(onBack: () -> Unit) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val cards = AwsDataRepository.flashcards
    val currentCard = cards[currentIndex]

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
                Text("AWS Flashcards (${currentIndex + 1}/${cards.size})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
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
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isFlipped) CloudSuccess else AwsOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clickable { isFlipped = !isFlipped }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isFlipped) CloudSuccess.copy(alpha = 0.2f) else AwsOrange.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (isFlipped) "ANSWER • ${currentCard.category}" else "QUESTION (TAP TO FLIP)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isFlipped) CloudSuccess else AwsOrange,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (isFlipped) currentCard.backAnswer else currentCard.frontQuestion,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = {
                        isFlipped = false
                        currentIndex = if (currentIndex > 0) currentIndex - 1 else cards.size - 1
                    }
                ) {
                    Text("Previous")
                }
                Button(
                    onClick = {
                        isFlipped = false
                        currentIndex = if (currentIndex < cards.size - 1) currentIndex + 1 else 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                ) {
                    Text("Next Card", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
