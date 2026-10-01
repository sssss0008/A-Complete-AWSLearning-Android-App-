package com.example.ui.screens.certifications

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun CertificationScreen(onBack: () -> Unit) {
    var selectedCert by remember { mutableStateOf("CLF-C02") }

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
                    text = "AWS Certification Preparation Center",
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
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = selectedCert == "CLF-C02",
                        onClick = { selectedCert = "CLF-C02" },
                        label = { Text("Cloud Practitioner (CLF-C02)") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AwsOrange, selectedLabelColor = CloudNavyDark)
                    )
                    FilterChip(
                        selected = selectedCert == "SAA-C03",
                        onClick = { selectedCert = "SAA-C03" },
                        label = { Text("Solutions Architect (SAA-C03)") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AwsOrange, selectedLabelColor = CloudNavyDark)
                    )
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AwsOrange.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (selectedCert == "CLF-C02") "AWS Certified Cloud Practitioner" else "AWS Certified Solutions Architect - Associate",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (selectedCert == "CLF-C02")
                                "Validates overall understanding of the AWS Cloud platform, basic security, and cloud economics."
                            else
                                "Validates knowledge of designing high-availability, cost-effective, fault-tolerant, and scalable distributed systems on AWS.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            item {
                Text("Official Exam Domains & Weighting", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            val domains = if (selectedCert == "CLF-C02") {
                listOf(
                    "Domain 1: Cloud Concepts" to "24% Weighting",
                    "Domain 2: Security & Compliance" to "30% Weighting",
                    "Domain 3: Cloud Technology & Services" to "34% Weighting",
                    "Domain 4: Billing, Pricing & Support" to "12% Weighting"
                )
            } else {
                listOf(
                    "Domain 1: Design Secure Architectures" to "30% Weighting",
                    "Domain 2: Design Resilient Architectures" to "26% Weighting",
                    "Domain 3: Design High-Performing Architectures" to "24% Weighting",
                    "Domain 4: Design Cost-Optimized Architectures" to "20% Weighting"
                )
            }

            items(domains.size) { idx ->
                val (domain, weight) = domains[idx]
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(domain, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(weight, style = MaterialTheme.typography.labelSmall, color = CloudBlue)
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudSurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudSuccess.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, tint = CloudSuccess)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Exam Preparation Roadmap Status", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = CloudSuccess)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You have completed 6 foundational lessons and 2 interactive labs required for this certification. Keep up the streak to be exam-ready!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}
