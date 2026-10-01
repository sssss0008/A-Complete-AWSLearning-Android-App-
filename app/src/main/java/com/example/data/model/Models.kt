package com.example.data.model

data class UserProfile(
    val name: String = "Awiskar Acharya",
    val title: String = "Aspiring Solutions Architect",
    val experienceLevel: String = "AWS Developer",
    val currentAwsKnowledge: String = "Intermediate",
    val learningGoals: List<String> = listOf("Become Solutions Architect", "Build Real Projects", "Prepare for Certification"),
    val dailyGoalMinutes: Int = 30,
    val minutesStudiedToday: Int = 22,
    val currentStreakDays: Int = 7,
    val completedLessonIds: Set<String> = setOf("cf-01", "cf-02", "cf-03", "aws-01", "ec2-01", "s3-01"),
    val completedLabIds: Set<String> = setOf("lab-ec2", "lab-s3"),
    val completedProjectIds: Set<String> = setOf("proj-01"),
    val completedChallengeIds: Set<String> = setOf("ch-01", "ch-02"),
    val bookmarkedLessonIds: Set<String> = setOf("vpc-01", "iam-01"),
    val bookmarkedServiceIds: Set<String> = setOf("ec2", "s3", "vpc", "lambda"),
    val isOnboarded: Boolean = true,
    val selectedRegion: String = "us-east-1 (N. Virginia)"
)

enum class ServiceCategory(val displayName: String) {
    COMPUTE("Compute"),
    STORAGE("Storage"),
    DATABASE("Database"),
    NETWORKING("Networking & Content Delivery"),
    SECURITY("Security, Identity & Compliance"),
    SERVERLESS("Serverless"),
    CONTAINERS("Containers"),
    DEVOPS("DevOps & CI/CD"),
    IAC("Infrastructure as Code"),
    ANALYTICS("Analytics"),
    MANAGEMENT("Management & Governance"),
    AI_ML("Machine Learning & AI")
}

data class AWSService(
    val id: String,
    val name: String,
    val shortName: String,
    val category: ServiceCategory,
    val summary: String,
    val beginnerExplanation: String,
    val whyItExists: String,
    val keyConcepts: List<String>,
    val commonUseCases: List<String>,
    val relatedServiceIds: List<String>,
    val difficulty: String = "Beginner", // Beginner, Intermediate, Advanced
    val sampleCli: String = "",
    val sampleIaC: String = "",
    val commonMistakes: List<String> = emptyList()
)

data class Lesson(
    val id: String,
    val moduleId: String,
    val title: String,
    val estimatedMinutes: Int,
    val difficulty: String,
    val summary: String,
    val sections: List<LessonSection>,
    val quizQuestion: QuizQuestion? = null
)

data class LessonSection(
    val heading: String,
    val body: String,
    val codeSnippet: String? = null,
    val codeLanguage: String? = null,
    val keyTakeaway: String? = null,
    val diagramType: String? = null // e.g. "INFRA_GLOBAL", "VPC_FLOW", "EC2_LIFECYCLE", "IAM_EVAL"
)

data class LearningModule(
    val id: String,
    val levelNumber: Int,
    val title: String,
    val subtitle: String,
    val category: ServiceCategory,
    val lessons: List<Lesson>,
    val labId: String? = null
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val category: String
)

data class Flashcard(
    val id: String,
    val frontQuestion: String,
    val backAnswer: String,
    val category: String,
    val isMastered: Boolean = false
)

data class TroubleshootingIncident(
    val id: String,
    val title: String,
    val severity: String, // P1 - Critical, P2 - High, P3 - Medium
    val symptom: String,
    val architectureContext: String,
    val simulatedLogs: List<String>,
    val simulatedMetrics: Map<String, String>,
    val options: List<IncidentOption>,
    val explanation: String
)

data class IncidentOption(
    val id: String,
    val description: String,
    val isCorrect: Boolean,
    val diagnosticReason: String
)

data class CloudProject(
    val id: String,
    val title: String,
    val level: String, // Beginner, Intermediate, Advanced
    val estimatedHours: String,
    val objective: String,
    val architectureFlow: String,
    val tasks: List<ProjectTask>,
    val codePreview: String,
    val architectureNotes: String,
    val badgeName: String
)

data class ProjectTask(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)

data class InterviewQuestion(
    val id: String,
    val topic: String,
    val difficulty: String,
    val question: String,
    val keyTalkingPoints: List<String>,
    val sampleAnswer: String,
    val architecturalInsight: String
)

data class CheatSheet(
    val id: String,
    val title: String,
    val category: String,
    val quickReference: List<Pair<String, String>>,
    val commonCommands: List<Pair<String, String>>,
    val gotchas: List<String>
)

data class NoteItem(
    val id: String,
    val title: String,
    val targetType: String, // Lesson, Service, Lab, Architecture
    val content: String,
    val timestamp: String
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean,
    val dateUnlocked: String? = null
)
