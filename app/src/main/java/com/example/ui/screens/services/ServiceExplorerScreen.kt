package com.example.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceCategory
import com.example.data.repository.AwsDataRepository
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.CodeSnippetCard
import com.example.ui.theme.*

@Composable
fun ServiceExplorerScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ServiceCategory?>(null) }

    val filteredServices = remember(searchQuery, selectedCategory) {
        AwsDataRepository.services.filter { svc ->
            val matchesCategory = selectedCategory == null || svc.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    svc.name.contains(searchQuery, ignoreCase = true) ||
                    svc.shortName.contains(searchQuery, ignoreCase = true) ||
                    svc.summary.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

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
                    text = "AWS Service Explorer",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Deep-dive encyclopedia of AWS services, APIs, and real-world architectures",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search services, e.g. EC2, S3, VPC...", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
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
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All (${AwsDataRepository.services.size})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AwsOrange, selectedLabelColor = CloudNavyDark)
                    )
                }
                items(ServiceCategory.entries) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = if (isSelected) null else cat },
                        label = { Text(cat.displayName) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AwsOrange, selectedLabelColor = CloudNavyDark)
                    )
                }
            }
        }

        // Comparison Banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CloudSurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBlue.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.SERVICE_COMPARISON) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CompareArrows, contentDescription = null, tint = CloudBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Service Comparison Matrix", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("EC2 vs Lambda, S3 vs EBS, RDS vs DynamoDB", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = CloudBlue)
                }
            }
        }

        // Service List Cards
        items(filteredServices) { svc ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openService(svc.id) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CloudNavyDark,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = svc.shortName.take(3),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AwsOrange
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = svc.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "${svc.category.displayName} • ${svc.difficulty}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CloudBlue
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = svc.summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 2
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceDetailScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val serviceId by viewModel.selectedServiceId.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val service = AwsDataRepository.services.find { it.id == serviceId } ?: AwsDataRepository.services.first()
    val isBookmarked = userProfile.bookmarkedServiceIds.contains(service.id)

    Scaffold(
        containerColor = CloudNavyDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = { viewModel.toggleServiceBookmark(service.id) }) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) AwsOrange else TextSecondary
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overview Hero
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AwsOrange.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = service.shortName,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AwsOrange
                        )
                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = service.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Beginner Explanation
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, tint = CloudBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("What it does (Beginner Explanation)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(service.beginnerExplanation, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                }
            }

            // Why it Exists
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.HistoryEdu, contentDescription = null, tint = AwsOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Why does it exist?", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(service.whyItExists, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                }
            }

            // Key Concepts
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Core Architectural Concepts", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        service.keyConcepts.forEach { concept ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("• ", color = AwsOrange, fontWeight = FontWeight.Bold)
                                Text(concept, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            // Code & CLI Reference
            if (service.sampleCli.isNotBlank()) {
                item {
                    CodeSnippetCard(
                        code = service.sampleCli,
                        language = "bash",
                        title = "AWS CLI Command Example"
                    )
                }
            }

            if (service.sampleIaC.isNotBlank()) {
                item {
                    CodeSnippetCard(
                        code = service.sampleIaC,
                        language = "yaml",
                        title = "Infrastructure as Code (CloudFormation / Terraform)"
                    )
                }
            }

            // Common Mistakes
            if (service.commonMistakes.isNotEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CloudSurfaceDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CloudError.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = CloudError)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Common Architectural Anti-Patterns", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = CloudError)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            service.commonMistakes.forEach { mistake ->
                                Text("⚠️ $mistake", style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(vertical = 2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceComparisonScreen(onBack: () -> Unit) {
    val comparisons = listOf(
        Triple("Amazon EC2 vs AWS Lambda", "Virtual Servers vs Serverless Functions", "EC2 gives full OS and kernel control, runs 24/7 with fixed pricing, and supports long-running stateful applications. Lambda is event-driven, scales automatically from zero, charges strictly per millisecond of compute, with a 15-minute maximum runtime limit."),
        Triple("Amazon S3 vs Amazon EBS", "Object Storage vs Block Storage", "S3 stores unbounded files/objects accessible globally via HTTP/HTTPS API with 11 9's durability. EBS provides raw high-performance disk block storage mounted directly to an individual EC2 instance over high-speed virtual network cables."),
        Triple("Amazon RDS vs Amazon DynamoDB", "Relational SQL vs NoSQL Key-Value", "RDS is best for complex relational schemas, foreign keys, and ACID financial reporting. DynamoDB delivers single-digit millisecond latency at any throughput scale for fast key-value lookups, user carts, and gaming leaderboards.")
    )

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
                Text("AWS Architectural Service Comparisons", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
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
            items(comparisons) { (title, subtitle, desc) ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AwsOrange)
                        Text(subtitle, style = MaterialTheme.typography.labelSmall, color = CloudBlue)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(desc, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                }
            }
        }
    }
}
