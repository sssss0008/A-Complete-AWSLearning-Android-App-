package com.example.ui.screens.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.CloudProject
import com.example.data.repository.AwsDataRepository
import com.example.ui.MainViewModel
import com.example.ui.components.CodeSnippetCard
import com.example.ui.theme.*

@Composable
fun ProjectsScreen(viewModel: MainViewModel) {
    val selectedProjectId by viewModel.selectedProjectId.collectAsState()
    val activeProject = AwsDataRepository.projects.find { it.id == selectedProjectId }
        ?: AwsDataRepository.projects.first()

    var taskStates by remember(activeProject.id) {
        mutableStateOf(activeProject.tasks.associate { it.id to it.isCompleted }.toMutableMap())
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
                    text = "Cloud Project Builder",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Real-world production architectures with milestone task execution",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Project Selector Chips
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AwsDataRepository.projects.forEach { proj ->
                    val isSelected = proj.id == activeProject.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.openProject(proj.id) },
                        label = { Text("${proj.level}: ${proj.title.take(24)}...") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AwsOrange, selectedLabelColor = CloudNavyDark)
                    )
                }
            }
        }

        // Project Overview Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, AwsOrange.copy(alpha = 0.35f)),
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
                                text = "${activeProject.level.uppercase()} • ${activeProject.estimatedHours}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = CloudBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "Badge: ${activeProject.badgeName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AwsOrange
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = activeProject.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = activeProject.objective,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CloudNavyDark
                    ) {
                        Text(
                            text = "Topology: ${activeProject.architectureFlow}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CloudSuccess,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        // Tasks Checklist
        item {
            val completedCount = taskStates.values.count { it }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Project Milestones Checklist",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$completedCount / ${activeProject.tasks.size} Done",
                    style = MaterialTheme.typography.labelSmall,
                    color = AwsOrange
                )
            }
        }

        items(activeProject.tasks) { task ->
            val isChecked = taskStates[task.id] == true
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CloudCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isChecked) CloudSuccess.copy(alpha = 0.4f) else CloudBorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val updated = taskStates.toMutableMap()
                        updated[task.id] = !isChecked
                        taskStates = updated
                    }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            val updated = taskStates.toMutableMap()
                            updated[task.id] = checked
                            taskStates = updated
                        },
                        colors = CheckboxDefaults.colors(checkedColor = CloudSuccess)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isChecked) TextSecondary else TextPrimary
                        )
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // IaC Code Preview
        item {
            CodeSnippetCard(
                code = activeProject.codePreview,
                language = "terraform",
                title = "Project Infrastructure as Code Template"
            )
        }
    }
}
