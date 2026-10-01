package com.example.ui.screens.architecture

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class StudioNode(val id: String, val name: String, val category: String, val icon: ImageVector, val color: Color)

@Composable
fun ArchitectureStudioScreen() {
    val activeNodes = remember {
        mutableStateListOf(
            StudioNode("n1", "Route 53", "Network", Icons.Default.Public, CloudBlue),
            StudioNode("n2", "CloudFront CDN", "Network", Icons.Default.Language, CloudBlue),
            StudioNode("n3", "App Load Balancer", "Network", Icons.Default.CallSplit, AwsOrange),
            StudioNode("n4", "EC2 Auto Scaling", "Compute", Icons.Default.Memory, CloudPurple),
            StudioNode("n5", "Aurora Multi-AZ", "Database", Icons.Default.Storage, CloudSuccess)
        )
    }

    var selectedChallenge by remember { mutableStateOf("Highly Available Web Tier") }
    var showExplanationDialog by remember { mutableStateOf(false) }

    val paletteServices = listOf(
        StudioNode("p1", "S3 Storage", "Storage", Icons.Default.Folder, AwsOrange),
        StudioNode("p2", "AWS Lambda", "Serverless", Icons.Default.Bolt, AwsOrangeLight),
        StudioNode("p3", "DynamoDB", "Database", Icons.Default.Layers, CloudSuccess),
        StudioNode("p4", "AWS WAF", "Security", Icons.Default.Security, CloudError),
        StudioNode("p5", "SQS Queue", "Messaging", Icons.Default.ForwardToInbox, CloudBlueGlow)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudNavyDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Cloud Architecture Studio",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Interactive cloud design canvas. Build resilient, scalable architectures.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Active Preset Architecture Challenge
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Current Challenge: $selectedChallenge",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AwsOrange
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Design a system capable of handling 100,000 req/sec with automatic regional multi-AZ failover.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // Palette Tray
        item {
            Text(
                text = "Service Palette (Tap to add to canvas)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(paletteServices) { s ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CloudCardDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, s.color.copy(alpha = 0.4f)),
                        modifier = Modifier.clickable {
                            activeNodes.add(s.copy(id = "n_${System.currentTimeMillis()}"))
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(s.icon, contentDescription = null, tint = s.color, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(s.name, style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                        }
                    }
                }
            }
        }

        // Visual Architecture Canvas
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CloudSurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBlue.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Connected Architecture Topology (${activeNodes.size} nodes)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CloudBlue
                        )
                        IconButton(onClick = { activeNodes.clear() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset Canvas", tint = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    activeNodes.forEachIndexed { index, node ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CloudCardDark,
                            border = androidx.compose.foundation.BorderStroke(1.dp, node.color.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(node.color.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(node.icon, contentDescription = null, tint = node.color, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(node.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(node.category, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                    }
                                }

                                IconButton(onClick = { activeNodes.remove(node) }) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = TextMuted, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        if (index < activeNodes.size - 1) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = "Data Flow",
                                    tint = AwsOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { showExplanationDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Explain Architecture", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showExplanationDialog) {
        AlertDialog(
            onDismissRequest = { showExplanationDialog = false },
            title = {
                Text("Architectural Analysis & Data Flow", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Text(
                    text = "1. Global Users query Route 53 DNS with latency routing.\n" +
                            "2. CloudFront caches static media at edge locations, terminating TLS and mitigating DDoS attacks via AWS Shield.\n" +
                            "3. Dynamic API requests pass to the Application Load Balancer (ALB).\n" +
                            "4. ALB distributes traffic across stateless EC2 instances in an Auto Scaling Group across 2 Availability Zones.\n" +
                            "5. EC2 persists transactional data to an Amazon Aurora PostgreSQL Multi-AZ cluster with automatic storage autoscaling and read replicas.\n\n" +
                            "Reliability Score: 98/100 (Zero Single Points of Failure).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { showExplanationDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark)
                ) {
                    Text("Got It")
                }
            },
            containerColor = CloudCardDark
        )
    }
}
