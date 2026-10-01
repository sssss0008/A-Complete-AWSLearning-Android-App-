package com.example.ui.screens.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.Lesson
import com.example.data.repository.AwsDataRepository
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.CodeSnippetCard
import com.example.ui.theme.*

@Composable
fun LearnScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudNavyDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column {
                Text(
                    text = "AWS Interactive Curriculum",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "From Cloud Beginner to Advanced Solutions Architect",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        items(AwsDataRepository.modules) { module ->
            Surface(
                shape = RoundedCornerShape(16.dp),
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
                            color = AwsOrange.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "LEVEL ${module.levelNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AwsOrangeLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "${module.lessons.count { userProfile.completedLessonIds.contains(it.id) }} / ${module.lessons.size} Lessons",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = module.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Module Lessons
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        module.lessons.forEach { lesson ->
                            val isCompleted = userProfile.completedLessonIds.contains(lesson.id)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = CloudNavyDark,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isCompleted) CloudSuccess.copy(alpha = 0.4f) else CloudBorderDark
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.openLesson(lesson.id) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayCircleOutline,
                                            contentDescription = null,
                                            tint = if (isCompleted) CloudSuccess else AwsOrange,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = lesson.title,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = "${lesson.estimatedMinutes} mins • ${lesson.difficulty}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextMuted
                                            )
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonDetailScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val lessonId by viewModel.selectedLessonId.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val lesson = AwsDataRepository.modules.flatMap { it.lessons }.find { it.id == lessonId }
        ?: AwsDataRepository.modules.first().lessons.first()

    val isCompleted = userProfile.completedLessonIds.contains(lesson.id)
    val isBookmarked = userProfile.bookmarkedLessonIds.contains(lesson.id)

    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var hasAnsweredQuiz by remember { mutableStateOf(false) }

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
                        text = lesson.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Row {
                    IconButton(onClick = { viewModel.toggleLessonBookmark(lesson.id) }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) AwsOrange else TextSecondary
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = CloudSurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = onBack) {
                        Text("Previous")
                    }

                    Button(
                        onClick = {
                            viewModel.toggleLessonCompleted(lesson.id)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) CloudSuccess else AwsOrange,
                            contentColor = CloudNavyDark
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isCompleted) "Completed ✓" else "Mark Complete", fontWeight = FontWeight.Bold)
                    }
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
            // Header Info
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${lesson.estimatedMinutes} Minutes Study • ${lesson.difficulty}",
                        style = MaterialTheme.typography.labelMedium,
                        color = CloudBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lesson.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            // Sections
            items(lesson.sections) { sec ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = sec.heading,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AwsOrange
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = sec.body,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )

                        if (sec.codeSnippet != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            CodeSnippetCard(
                                code = sec.codeSnippet,
                                language = sec.codeLanguage ?: "text",
                                title = "Architectural Reference"
                            )
                        }

                        if (sec.keyTakeaway != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CloudNavyDark,
                                border = androidx.compose.foundation.BorderStroke(1.dp, CloudBlue.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = AwsOrange, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = sec.keyTakeaway,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Checkpoint Quiz
            if (lesson.quizQuestion != null) {
                val q = lesson.quizQuestion
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CloudSurfaceDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, AwsOrange.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Lesson Checkpoint Quiz",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AwsOrange
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = q.question,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            q.options.forEachIndexed { idx, opt ->
                                val isSelected = selectedAnswerIndex == idx
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = when {
                                        hasAnsweredQuiz && idx == q.correctIndex -> CloudSuccess.copy(alpha = 0.2f)
                                        hasAnsweredQuiz && isSelected && idx != q.correctIndex -> CloudError.copy(alpha = 0.2f)
                                        isSelected -> AwsOrange.copy(alpha = 0.15f)
                                        else -> CloudCardDark
                                    },
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        when {
                                            hasAnsweredQuiz && idx == q.correctIndex -> CloudSuccess
                                            hasAnsweredQuiz && isSelected && idx != q.correctIndex -> CloudError
                                            isSelected -> AwsOrange
                                            else -> CloudBorderDark
                                        }
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (!hasAnsweredQuiz) {
                                                selectedAnswerIndex = idx
                                                hasAnsweredQuiz = true
                                            }
                                        }
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = opt,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextPrimary,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }

                            if (hasAnsweredQuiz) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = q.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (selectedAnswerIndex == q.correctIndex) CloudSuccess else CloudWarning
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
