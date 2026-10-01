package com.example.ui.screens.troubleshooting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AwsDataRepository
import com.example.ui.MainViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun TroubleshootingScreen(viewModel: MainViewModel) {
    val incidentId by viewModel.selectedIncidentId.collectAsState()
    val incident = AwsDataRepository.troubleshootingIncidents.find { it.id == incidentId }
        ?: AwsDataRepository.troubleshootingIncidents.first()

    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    var isVerified by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudNavyDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Cloud Troubleshooting Incident",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Investigate telemetry, isolate root causes, and restore service",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                StatusBadge(status = incident.severity)
            }
        }

        // Incident Overview Card
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudError.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(incident.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(incident.symptom, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CloudNavyDark
                    ) {
                        Text(
                            text = "Topology: ${incident.architectureContext}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CloudBlue,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        // Telemetry Logs & Metrics
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = TerminalBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("CloudWatch Ingestion Logs", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    incident.simulatedLogs.forEach { log ->
                        Text(
                            text = log,
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace, fontSize = 11.sp),
                            color = if (log.contains("504") || log.contains("ERROR") || log.contains("failed")) CloudError else TextSecondary,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Root Cause Diagnostic Options
        item {
            Text("Diagnose Root Cause & Remediation", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        items(incident.options) { opt ->
            val isSelected = selectedOptionId == opt.id
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when {
                    isVerified && opt.isCorrect -> CloudSuccess.copy(alpha = 0.15f)
                    isVerified && isSelected && !opt.isCorrect -> CloudError.copy(alpha = 0.15f)
                    isSelected -> AwsOrange.copy(alpha = 0.15f)
                    else -> CloudCardDark
                },
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    when {
                        isVerified && opt.isCorrect -> CloudSuccess
                        isVerified && isSelected && !opt.isCorrect -> CloudError
                        isSelected -> AwsOrange
                        else -> CloudBorderDark
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isVerified) { selectedOptionId = opt.id }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = opt.description,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = isSelected,
                            onClick = { if (!isVerified) selectedOptionId = opt.id },
                            colors = RadioButtonDefaults.colors(selectedColor = AwsOrange)
                        )
                    }

                    if (isVerified && (isSelected || opt.isCorrect)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = opt.diagnosticReason,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (opt.isCorrect) CloudSuccess else CloudError,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Action Buttons
        item {
            if (!isVerified) {
                Button(
                    onClick = { isVerified = true },
                    enabled = selectedOptionId != null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Build, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Apply Remediation & Verify", fontWeight = FontWeight.Bold)
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CloudNavyDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudSuccess),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CloudSuccess)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("INCIDENT RESOLVED", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = CloudSuccess)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(incident.explanation, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
            }
        }
    }
}
