package com.example.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.repository.AwsDataRepository
import com.example.ui.theme.*

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var offlineCacheEnabled by remember { mutableStateOf(true) }
    var dailyReminderEnabled by remember { mutableStateOf(true) }
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
                Text("Settings & System Preferences", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
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
                Text("Learning Preferences", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Offline Mode & Content Caching", style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text("Cache all lessons, cheat sheets, and flashcards locally", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            Switch(
                                checked = offlineCacheEnabled,
                                onCheckedChange = { offlineCacheEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = AwsOrange, checkedTrackColor = AwsOrangeDark)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Daily Learning Goal Notifications", style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text("Gentle reminders to maintain your AWS learning streak", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            Switch(
                                checked = dailyReminderEnabled,
                                onCheckedChange = { dailyReminderEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = AwsOrange, checkedTrackColor = AwsOrangeDark)
                            )
                        }
                    }
                }
            }

            item {
                Text("About Application", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Learn AWS by Awiskar Acharya", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AwsOrange)
                        Text(AwsDataRepository.appTagline, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Version: 1.0.0 (Production Release)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Text("License: 100% Free Educational Platform", style = MaterialTheme.typography.labelSmall, color = CloudSuccess)

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AwsDataRepository.creatorLinkedIn))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Connect with Awiskar on LinkedIn", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
