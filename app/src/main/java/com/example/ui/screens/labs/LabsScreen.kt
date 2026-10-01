package com.example.ui.screens.labs

import androidx.compose.animation.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LabType
import com.example.ui.MainViewModel
import com.example.ui.components.CodeSnippetCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun LabsScreen(
    viewModel: MainViewModel
) {
    val selectedLab by viewModel.selectedLabType.collectAsState()

    val labsList = listOf(
        LabType.EC2 to "EC2 Simulator",
        LabType.S3 to "S3 Storage Lab",
        LabType.VPC to "VPC Architecture",
        LabType.IAM to "IAM Policy Builder",
        LabType.DATABASE_DECISION to "Database Decision",
        LabType.DEVOPS_PIPELINE to "CI/CD Pipeline",
        LabType.COST_OPTIMIZATION to "Cost Optimizer"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudNavyDark)
    ) {
        // Lab Selector Tab Row
        ScrollableTabRow(
            selectedTabIndex = labsList.indexOfFirst { it.first == selectedLab }.coerceAtLeast(0),
            containerColor = CloudSurfaceDark,
            contentColor = AwsOrange,
            edgePadding = 16.dp,
            indicator = {},
            divider = {}
        ) {
            labsList.forEach { (type, label) ->
                val isSelected = selectedLab == type
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) AwsOrange else CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) AwsOrange else CloudBorderDark
                    ),
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 4.dp)
                        .clickable { viewModel.openLab(type) }
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) CloudNavyDark else TextPrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Active Lab Workspace
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedLab) {
                LabType.EC2 -> Ec2SimulatorView(viewModel)
                LabType.S3 -> S3SimulatorView(viewModel)
                LabType.VPC -> VpcArchitectureLabView()
                LabType.IAM -> IamPolicyBuilderView(viewModel)
                LabType.DATABASE_DECISION -> DatabaseDecisionLabView()
                LabType.DEVOPS_PIPELINE -> DevOpsPipelineView(viewModel)
                LabType.COST_OPTIMIZATION -> CostOptimizationView()
            }
        }
    }
}

// ---------------- 1. EC2 SIMULATOR ----------------
@Composable
fun Ec2SimulatorView(viewModel: MainViewModel) {
    val ec2State by viewModel.ec2State.collectAsState()

    val instanceTypes = listOf("t3.micro (2 vCPU, 1 GiB RAM)", "c6i.large (2 vCPU, 4 GiB RAM)", "r6i.xlarge (4 vCPU, 32 GiB RAM)", "g4dn.xlarge (4 vCPU, 16 GiB, T4 GPU)")
    val amiOptions = listOf("Amazon Linux 2023 (64-bit x86)", "Ubuntu 22.04 LTS (Jammy Jellyfish)", "Red Hat Enterprise Linux 9")

    LazyColumn(
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
                        text = "Amazon EC2 Interactive Laboratory",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Configure, provision, and control virtual servers in the cloud",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                StatusBadge(status = ec2State.status)
            }
        }

        // Configuration Card
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Instance Hardware & AMI",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AwsOrange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Instance Type", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    instanceTypes.forEach { type ->
                        val isSelected = ec2State.instanceType == type
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateEc2Config(type, ec2State.ami, ec2State.storageGb, ec2State.isPort22Open, ec2State.isPort80Open, ec2State.isPort443Open)
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    viewModel.updateEc2Config(type, ec2State.ami, ec2State.storageGb, ec2State.isPort22Open, ec2State.isPort80Open, ec2State.isPort443Open)
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = AwsOrange)
                            )
                            Text(type, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Operating System (AMI)", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    amiOptions.forEach { ami ->
                        val isSelected = ec2State.ami == ami
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateEc2Config(ec2State.instanceType, ami, ec2State.storageGb, ec2State.isPort22Open, ec2State.isPort80Open, ec2State.isPort443Open)
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    viewModel.updateEc2Config(ec2State.instanceType, ami, ec2State.storageGb, ec2State.isPort22Open, ec2State.isPort80Open, ec2State.isPort443Open)
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = AwsOrange)
                            )
                            Text(ami, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("2. Security Group Firewalls (Inbound Ports)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AwsOrange)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = ec2State.isPort22Open,
                            onClick = { viewModel.updateEc2Config(ec2State.instanceType, ec2State.ami, ec2State.storageGb, !ec2State.isPort22Open, ec2State.isPort80Open, ec2State.isPort443Open) },
                            label = { Text("Port 22 (SSH)") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AwsOrange, selectedLabelColor = CloudNavyDark)
                        )
                        FilterChip(
                            selected = ec2State.isPort80Open,
                            onClick = { viewModel.updateEc2Config(ec2State.instanceType, ec2State.ami, ec2State.storageGb, ec2State.isPort22Open, !ec2State.isPort80Open, ec2State.isPort443Open) },
                            label = { Text("Port 80 (HTTP)") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AwsOrange, selectedLabelColor = CloudNavyDark)
                        )
                        FilterChip(
                            selected = ec2State.isPort443Open,
                            onClick = { viewModel.updateEc2Config(ec2State.instanceType, ec2State.ami, ec2State.storageGb, ec2State.isPort22Open, ec2State.isPort80Open, !ec2State.isPort443Open) },
                            label = { Text("Port 443 (HTTPS)") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AwsOrange, selectedLabelColor = CloudNavyDark)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.launchEc2() },
                            enabled = ec2State.status == "Stopped",
                            colors = ButtonDefaults.buttonColors(containerColor = CloudSuccess),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Launch")
                        }

                        Button(
                            onClick = { viewModel.stopEc2() },
                            enabled = ec2State.status == "Running",
                            colors = ButtonDefaults.buttonColors(containerColor = CloudWarning),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Stop")
                        }

                        OutlinedButton(
                            onClick = { viewModel.restartEc2() },
                            enabled = ec2State.status == "Running",
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Restart")
                        }
                    }
                }
            }
        }

        // Live Console Terminal Output
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = TerminalBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "EC2 Instance Telemetry & Logs",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "IP: ${ec2State.publicIp}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CloudBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ec2State.terminalLogs.takeLast(6).forEach { log ->
                        Text(
                            text = log,
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = if (log.contains("RUNNING") || log.contains("OK")) TerminalGreen else TextSecondary,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

// ---------------- 2. S3 STORAGE SIMULATOR ----------------
@Composable
fun S3SimulatorView(viewModel: MainViewModel) {
    val s3State by viewModel.s3State.collectAsState()
    var newFileName by remember { mutableStateOf("") }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Column {
                Text(
                    text = "Amazon S3 Storage Simulator",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Infinite object storage with tiering, encryption, and real-time cost modeling",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Bucket Overview Card
        item {
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
                        Column {
                            Text("Bucket Name", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = "s3://${s3State.bucketName}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AwsOrange
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CloudBlue.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = s3State.region,
                                style = MaterialTheme.typography.labelSmall,
                                color = CloudBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Objects", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = "${s3State.objects.size}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Column {
                            Text("Estimated Cost", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = "$${s3State.totalCostPerMonth} / mo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CloudSuccess
                            )
                        }
                        Column {
                            Text("Versioning", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = if (s3State.versioningEnabled) "Enabled" else "Disabled",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (s3State.versioningEnabled) CloudSuccess else CloudWarning
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.toggleS3Versioning() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (s3State.versioningEnabled) "Disable Versioning" else "Enable Versioning", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Upload Object Section
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Upload Simulated Object", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newFileName,
                            onValueChange = { newFileName = it },
                            placeholder = { Text("e.g. videos/intro.mp4", color = TextMuted, fontSize = 13.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AwsOrange,
                                unfocusedBorderColor = CloudBorderDark,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        Button(
                            onClick = {
                                if (newFileName.isNotBlank()) {
                                    viewModel.addS3Object(newFileName, 1200, s3State.selectedStorageClass)
                                    newFileName = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Upload", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Bucket Objects List
        item {
            Text("Bucket Objects (${s3State.objects.size})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        items(s3State.objects) { obj ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = CloudBlue, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(obj.key, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text("${obj.sizeKb} KB • ${obj.storageClass} • ${obj.uploadedAt}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                    IconButton(onClick = { viewModel.deleteS3Object(obj.key) }) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = CloudError)
                    }
                }
            }
        }
    }
}

// ---------------- 3. VPC ARCHITECTURE LAB ----------------
@Composable
fun VpcArchitectureLabView() {
    var isSimulatingTraffic by remember { mutableStateOf(false) }
    var trafficLog by remember { mutableStateOf("Ready to simulate internet packet flow through VPC.") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column {
            Text(
                text = "VPC Visual Architecture & Traffic Flow",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Inspect packet traversal from Internet Gateway to isolated Database Subnets",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        // Architecture Visualizer Canvas
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = CloudCardDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, CloudBlue.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Tier 1: Public Internet & Gateway
                ArchitectureNodeBox(
                    title = "Public Internet (0.0.0.0/0)",
                    subtitle = "Clients worldwide accessing https://app.awiskar.com",
                    icon = Icons.Default.Public,
                    color = CloudBlue
                )

                TrafficArrow(isActive = isSimulatingTraffic)

                ArchitectureNodeBox(
                    title = "Internet Gateway (IGW) & Public Subnet (10.0.1.0/24)",
                    subtitle = "Application Load Balancer (ALB) terminates SSL & checks health",
                    icon = Icons.Default.FilterDrama,
                    color = AwsOrange
                )

                TrafficArrow(isActive = isSimulatingTraffic)

                ArchitectureNodeBox(
                    title = "NAT Gateway & Private App Subnet (10.0.2.0/24)",
                    subtitle = "EC2 Auto Scaling instances running containerized backend APIs",
                    icon = Icons.Default.Security,
                    color = CloudPurple
                )

                TrafficArrow(isActive = isSimulatingTraffic)

                ArchitectureNodeBox(
                    title = "Isolated Database Subnet (10.0.3.0/24)",
                    subtitle = "Multi-AZ RDS PostgreSQL (Zero Internet Access - SG Inbound 5432 only)",
                    icon = Icons.Default.Storage,
                    color = CloudSuccess
                )
            }
        }

        Button(
            onClick = {
                isSimulatingTraffic = true
                trafficLog = "Packet 192.0.2.1:443 -> IGW -> ALB (200 OK) -> EC2 (10.0.2.45:8080) -> RDS (10.0.3.12:5432) -> Response returned in 18ms!"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.PlayCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Simulate Production Traffic Flow", fontWeight = FontWeight.Bold)
        }

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = TerminalBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = trafficLog,
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                color = if (isSimulatingTraffic) TerminalGreen else TextSecondary,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun ArchitectureNodeBox(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = CloudNavyDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}

@Composable
fun TrafficArrow(isActive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = null,
            tint = if (isActive) AwsOrange else TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ---------------- 4. IAM POLICY LAB ----------------
@Composable
fun IamPolicyBuilderView(viewModel: MainViewModel) {
    val iamState by viewModel.iamState.collectAsState()

    val availableActions = listOf("s3:GetObject", "s3:PutObject", "s3:DeleteObject", "s3:ListBucket", "ec2:DescribeInstances", "ec2:StartInstances", "ec2:StopInstances")

    val generatedJson = """
        {
          "Version": "2012-10-17",
          "Statement": [
            {
              "Sid": "VisualPolicyStatement",
              "Effect": "${iamState.effect}",
              "Action": [
                ${iamState.selectedActions.joinToString(",\n        ") { "\"$it\"" }}
              ],
              "Resource": "${iamState.resourceArn}"${
                if (iamState.conditionMfa) ",\n      \"Condition\": {\n        \"Bool\": {\"aws:MultiFactorAuthPresent\": \"true\"}\n      }" else ""
            }
            }
          ]
        }
    """.trimIndent()

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Column {
                Text(
                    text = "IAM Visual Policy Builder",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Construct zero-trust AWS permission documents and preview live JSON",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("1. Effect", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(vertical = 6.dp)) {
                        FilterChip(
                            selected = iamState.effect == "Allow",
                            onClick = { viewModel.updateIamEffect("Allow") },
                            label = { Text("ALLOW") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CloudSuccess, selectedLabelColor = CloudNavyDark)
                        )
                        FilterChip(
                            selected = iamState.effect == "Deny",
                            onClick = { viewModel.updateIamEffect("Deny") },
                            label = { Text("EXPLICIT DENY") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CloudError, selectedLabelColor = TextPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("2. Permitted Actions", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Column {
                        availableActions.forEach { act ->
                            val isChecked = iamState.selectedActions.contains(act)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleIamAction(act) }
                                    .padding(vertical = 2.dp)
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { viewModel.toggleIamAction(act) },
                                    colors = CheckboxDefaults.colors(checkedColor = AwsOrange)
                                )
                                Text(act, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = iamState.conditionMfa,
                            onCheckedChange = { viewModel.toggleIamConditionMfa() },
                            colors = SwitchDefaults.colors(checkedThumbColor = AwsOrange, checkedTrackColor = AwsOrangeDark)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Enforce MFA (Multi-Factor Authentication)", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }
                }
            }
        }

        // Live JSON Policy Preview
        item {
            CodeSnippetCard(
                code = generatedJson,
                language = "json",
                title = "Generated IAM Policy Document"
            )
        }

        // Plain English Policy Explanation
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CloudNavyDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBlue.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Plain English Policy Translation", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = CloudBlue)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "This policy will ${iamState.effect.uppercase()} access to execute [${iamState.selectedActions.joinToString(", ")}] on bucket '${iamState.resourceArn}'. ${
                            if (iamState.conditionMfa) "Execution is strictly blocked unless the caller authenticated using a valid hardware or virtual MFA device." else "No MFA verification is mandated."
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

// ---------------- 5. DATABASE DECISION LAB ----------------
@Composable
fun DatabaseDecisionLabView() {
    var isRelational by remember { mutableStateOf(true) }
    var isExtremeScale by remember { mutableStateOf(false) }
    var needsSubMillisecond by remember { mutableStateOf(false) }
    var needsGraphRelations by remember { mutableStateOf(false) }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Column {
                Text(
                    text = "AWS Database Decision Matrix",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Select your data characteristics to see the optimal AWS database engine",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Workload Requirements", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AwsOrange)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Strict ACID Transactions & Complex SQL Joins", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        Switch(checked = isRelational, onCheckedChange = { isRelational = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Massive Scale (Millions of ops/sec, NoSQL)", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        Switch(checked = isExtremeScale, onCheckedChange = { isExtremeScale = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("In-Memory Caching (Sub-millisecond reads)", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        Switch(checked = needsSubMillisecond, onCheckedChange = { needsSubMillisecond = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Highly Connected Graph Relationships (Social/Fraud)", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        Switch(checked = needsGraphRelations, onCheckedChange = { needsGraphRelations = it })
                    }
                }
            }
        }

        // Recommendations
        item {
            val recommendation = when {
                needsGraphRelations -> "Amazon Neptune" to "Graph database optimized for billions of relationships (Gremlin/SPARQL/openCypher)"
                needsSubMillisecond -> "Amazon ElastiCache (Redis / Valkey)" to "In-memory datastore delivering sub-millisecond latency for session caching and real-time leaderboards"
                isExtremeScale && !isRelational -> "Amazon DynamoDB" to "Fully managed serverless NoSQL key-value & document database with single-digit millisecond response times"
                isRelational -> "Amazon Aurora (PostgreSQL / MySQL Compatible)" to "High-performance cloud-native relational database with 6-way replication across 3 AZs and automated storage scaling"
                else -> "Amazon DocumentDB" to "MongoDB-compatible fully managed JSON document database"
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CloudNavyDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudSuccess),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CloudSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("RECOMMENDED ENGINE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = CloudSuccess)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(recommendation.first, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(recommendation.second, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        }
    }
}

// ---------------- 6. DEVOPS CI/CD PIPELINE ----------------
@Composable
fun DevOpsPipelineView(viewModel: MainViewModel) {
    val devOpsState by viewModel.devOpsState.collectAsState()

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Column {
                Text(
                    text = "AWS CodePipeline & Blue/Green Deployment",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Simulate automated CI/CD stages with live canary traffic shifting",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Pipeline Stages Visualizer
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PipelineStageItem("Source (AWS CodeCommit / GitHub)", devOpsState.sourceStatus)
                    PipelineStageItem("Build (AWS CodeBuild - Docker)", devOpsState.buildStatus)
                    PipelineStageItem("Test (Automated Integration Tests)", devOpsState.testStatus)
                    PipelineStageItem("Deploy (AWS CodeDeploy - ECS Blue/Green)", devOpsState.deployStatus)
                }
            }
        }

        // Blue/Green Traffic Shifting Slider
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Canary Traffic Shift: Blue (Prod) vs Green (New)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Blue: ${devOpsState.blueTrafficPercent}%", color = CloudBlue, fontWeight = FontWeight.Bold)
                        Text("Green: ${devOpsState.greenTrafficPercent}%", color = CloudSuccess, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = devOpsState.greenTrafficPercent.toFloat(),
                        onValueChange = { viewModel.setTrafficShift(it.toInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(thumbColor = CloudSuccess, activeTrackColor = CloudSuccess)
                    )
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.triggerDevOpsPipeline() },
                enabled = !devOpsState.isPipelineRunning,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AwsOrange, contentColor = CloudNavyDark),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.RocketLaunch, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (devOpsState.isPipelineRunning) "Running Pipeline..." else "Trigger Deployment Pipeline", fontWeight = FontWeight.Bold)
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = TerminalBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Pipeline Execution Logs", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    devOpsState.logOutput.takeLast(5).forEach { log ->
                        Text(log, style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace), color = TerminalGreen)
                    }
                }
            }
        }
    }
}

@Composable
fun PipelineStageItem(name: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        StatusBadge(status = status)
    }
}

// ---------------- 7. COST OPTIMIZATION LAB ----------------
@Composable
fun CostOptimizationView() {
    var ebsCleaned by remember { mutableStateOf(false) }
    var ec2Downsized by remember { mutableStateOf(false) }

    val baseCost = 1842.50
    val ebsSavings = if (ebsCleaned) 210.0 else 0.0
    val ec2Savings = if (ec2Downsized) 430.0 else 0.0
    val currentCost = baseCost - ebsSavings - ec2Savings

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Column {
                Text(
                    text = "Cloud Cost Optimization & Billing Lab",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Identify idle instances, unattached EBS volumes, and rightsizing opportunities",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
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
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Simulated Monthly Cloud Bill", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${String.format("%.2f", currentCost)} / month",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = CloudSuccess
                    )
                    Text(
                        text = "Total Monthly Savings Achieved: $${String.format("%.2f", ebsSavings + ec2Savings)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AwsOrange
                    )
                }
            }
        }

        item {
            Text("Rightsizing Recommendations", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        item {
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Delete 6 Unattached EBS Volumes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Volumes left behind after terminated EC2 instances ($210/mo)", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Button(
                        onClick = { ebsCleaned = !ebsCleaned },
                        colors = ButtonDefaults.buttonColors(containerColor = if (ebsCleaned) CloudSuccess else AwsOrange)
                    ) {
                        Text(if (ebsCleaned) "Cleaned ✓" else "Delete")
                    }
                }
            }
        }

        item {
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Downsize Idle m6i.4xlarge to t4g.large", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Average CPU utilization < 3% over past 30 days ($430/mo)", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Button(
                        onClick = { ec2Downsized = !ec2Downsized },
                        colors = ButtonDefaults.buttonColors(containerColor = if (ec2Downsized) CloudSuccess else AwsOrange)
                    ) {
                        Text(if (ec2Downsized) "Optimized ✓" else "Downsize")
                    }
                }
            }
        }
    }
}
