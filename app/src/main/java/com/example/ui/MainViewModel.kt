package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AwsDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    LEARN,
    LABS,
    PROJECTS,
    PROFILE,
    // Sub-screens
    LESSON_DETAIL,
    SERVICE_EXPLORER,
    SERVICE_DETAIL,
    SERVICE_COMPARISON,
    ARCHITECTURE_STUDIO,
    CLI_TERMINAL,
    AI_TUTOR,
    TROUBLESHOOTING,
    CERTIFICATION_CENTER,
    INTERVIEW_CENTER,
    CHEAT_SHEETS,
    FLASHCARDS,
    NOTES,
    SETTINGS,
    CERTIFICATE_VIEW
}

enum class LabType {
    EC2,
    S3,
    VPC,
    IAM,
    DATABASE_DECISION,
    DEVOPS_PIPELINE,
    COST_OPTIMIZATION
}

// Lab Specific States
data class Ec2LabState(
    val instanceType: String = "t3.micro (2 vCPU, 1 GiB RAM)",
    val ami: String = "Amazon Linux 2023 (64-bit x86)",
    val storageGb: Int = 20,
    val isPort22Open: Boolean = false,
    val isPort80Open: Boolean = true,
    val isPort443Open: Boolean = true,
    val status: String = "Stopped", // Stopped, Pending, Running, Terminated
    val publicIp: String = "54.210.88.192",
    val privateIp: String = "10.0.1.45",
    val terminalLogs: List<String> = listOf("System initialized. Ready to launch instance.")
)

data class S3ObjectItem(val key: String, val sizeKb: Int, val storageClass: String, val uploadedAt: String)

data class S3LabState(
    val bucketName: String = "my-cloud-learning-bucket-2026",
    val region: String = "us-east-1",
    val versioningEnabled: Boolean = true,
    val encryptionType: String = "SSE-S3 (AES-256)",
    val selectedStorageClass: String = "Standard",
    val objects: List<S3ObjectItem> = listOf(
        S3ObjectItem("index.html", 4, "Standard", "Just now"),
        S3ObjectItem("styles/main.css", 12, "Standard", "2 mins ago"),
        S3ObjectItem("assets/hero-banner.webp", 450, "Standard", "5 mins ago"),
        S3ObjectItem("backups/database-dump-archive.tar.gz", 84000, "Glacier Flexible", "1 day ago")
    ),
    val totalCostPerMonth: Double = 0.85
)

data class IamPolicyLabState(
    val effect: String = "Allow",
    val service: String = "s3",
    val selectedActions: List<String> = listOf("s3:GetObject", "s3:ListBucket"),
    val resourceArn: String = "arn:aws:s3:::my-production-bucket/*",
    val conditionMfa: Boolean = true
)

data class DevOpsPipelineState(
    val isPipelineRunning: Boolean = false,
    val sourceStatus: String = "Succeeded",
    val buildStatus: String = "Succeeded",
    val testStatus: String = "Succeeded",
    val deployStatus: String = "Succeeded",
    val blueTrafficPercent: Int = 90,
    val greenTrafficPercent: Int = 10,
    val logOutput: List<String> = listOf("[Source] Commit 8f2a1b hooked from main branch", "[Build] Docker container built in 34s", "[Test] 48 unit & integration tests passed", "[Deploy] Canary shift: 10% traffic to Green environment")
)

data class TutorMessage(val sender: String, val text: String, val timestamp: String)

class MainViewModel : ViewModel() {

    // User Profile & State
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenBackStack = MutableStateFlow<List<AppScreen>>(listOf(AppScreen.HOME))
    val screenBackStack: StateFlow<List<AppScreen>> = _screenBackStack.asStateFlow()

    // Active Selection States
    private val _selectedLessonId = MutableStateFlow<String?>("cf-01")
    val selectedLessonId: StateFlow<String?> = _selectedLessonId.asStateFlow()

    private val _selectedServiceId = MutableStateFlow<String?>("ec2")
    val selectedServiceId: StateFlow<String?> = _selectedServiceId.asStateFlow()

    private val _selectedLabType = MutableStateFlow(LabType.EC2)
    val selectedLabType: StateFlow<LabType> = _selectedLabType.asStateFlow()

    private val _selectedIncidentId = MutableStateFlow<String?>("inc-01")
    val selectedIncidentId: StateFlow<String?> = _selectedIncidentId.asStateFlow()

    private val _selectedProjectId = MutableStateFlow<String?>("proj-01")
    val selectedProjectId: StateFlow<String?> = _selectedProjectId.asStateFlow()

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Lab Simulations
    private val _ec2State = MutableStateFlow(Ec2LabState())
    val ec2State: StateFlow<Ec2LabState> = _ec2State.asStateFlow()

    private val _s3State = MutableStateFlow(S3LabState())
    val s3State: StateFlow<S3LabState> = _s3State.asStateFlow()

    private val _iamState = MutableStateFlow(IamPolicyLabState())
    val iamState: StateFlow<IamPolicyLabState> = _iamState.asStateFlow()

    private val _devOpsState = MutableStateFlow(DevOpsPipelineState())
    val devOpsState: StateFlow<DevOpsPipelineState> = _devOpsState.asStateFlow()

    // CLI Terminal
    private val _terminalHistory = MutableStateFlow(
        listOf(
            "Welcome to the AWS CLI Learning Terminal [Version 2.15.30]",
            "Type 'help' or select a quick command below to begin simulated cloud operations.",
            "awiskar@cloud-box:~$ aws sts get-caller-identity",
            "{\n    \"UserId\": \"AROAEXAMPLE12345:awiskar\",\n    \"Account\": \"123456789012\",\n    \"Arn\": \"arn:aws:sts::123456789012:assumed-role/CloudEngineerRole/awiskar\"\n}"
        )
    )
    val terminalHistory: StateFlow<List<String>> = _terminalHistory.asStateFlow()

    // AI Tutor
    private val _tutorMessages = MutableStateFlow(
        listOf(
            TutorMessage("tutor", "Hello! I am your AI AWS Cloud Tutor. You can ask me to explain any AWS service, dissect architecture designs, explain CLI commands, or quiz you for certifications. What are you building today?", "Just now")
        )
    )
    val tutorMessages: StateFlow<List<TutorMessage>> = _tutorMessages.asStateFlow()

    private val _tutorMode = MutableStateFlow("Beginner") // Beginner, Technical, Interview, Certification, Debugging, Architecture
    val tutorMode: StateFlow<String> = _tutorMode.asStateFlow()

    // Navigation Methods
    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _screenBackStack.update { it + screen }
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        val stack = _screenBackStack.value
        if (stack.size > 1) {
            val newStack = stack.dropLast(1)
            _screenBackStack.value = newStack
            _currentScreen.value = newStack.last()
            return true
        }
        return false
    }

    fun openLesson(lessonId: String) {
        _selectedLessonId.value = lessonId
        navigateTo(AppScreen.LESSON_DETAIL)
    }

    fun openService(serviceId: String) {
        _selectedServiceId.value = serviceId
        navigateTo(AppScreen.SERVICE_DETAIL)
    }

    fun openLab(labType: LabType) {
        _selectedLabType.value = labType
        navigateTo(AppScreen.LABS)
    }

    fun openIncident(incidentId: String) {
        _selectedIncidentId.value = incidentId
        navigateTo(AppScreen.TROUBLESHOOTING)
    }

    fun openProject(projectId: String) {
        _selectedProjectId.value = projectId
        navigateTo(AppScreen.PROJECTS)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Profile Actions
    fun completeOnboarding(name: String, exp: String, awsExp: String, goals: List<String>, dailyMins: Int) {
        _userProfile.update {
            it.copy(
                name = name.ifBlank { "Cloud Learner" },
                experienceLevel = exp,
                currentAwsKnowledge = awsExp,
                learningGoals = goals,
                dailyGoalMinutes = dailyMins,
                isOnboarded = true
            )
        }
        navigateTo(AppScreen.HOME)
    }

    fun updateProfile(name: String, title: String, dailyMins: Int) {
        _userProfile.update {
            it.copy(
                name = name,
                title = title,
                dailyGoalMinutes = dailyMins
            )
        }
    }

    fun toggleLessonCompleted(lessonId: String) {
        _userProfile.update { current ->
            val set = current.completedLessonIds.toMutableSet()
            if (set.contains(lessonId)) set.remove(lessonId) else set.add(lessonId)
            current.copy(completedLessonIds = set)
        }
    }

    fun toggleLessonBookmark(lessonId: String) {
        _userProfile.update { current ->
            val set = current.bookmarkedLessonIds.toMutableSet()
            if (set.contains(lessonId)) set.remove(lessonId) else set.add(lessonId)
            current.copy(bookmarkedLessonIds = set)
        }
    }

    fun toggleServiceBookmark(serviceId: String) {
        _userProfile.update { current ->
            val set = current.bookmarkedServiceIds.toMutableSet()
            if (set.contains(serviceId)) set.remove(serviceId) else set.add(serviceId)
            current.copy(bookmarkedServiceIds = set)
        }
    }

    // EC2 Simulation Actions
    fun updateEc2Config(instanceType: String, ami: String, storageGb: Int, p22: Boolean, p80: Boolean, p443: Boolean) {
        _ec2State.update {
            it.copy(
                instanceType = instanceType,
                ami = ami,
                storageGb = storageGb,
                isPort22Open = p22,
                isPort80Open = p80,
                isPort443Open = p443
            )
        }
    }

    fun launchEc2() {
        _ec2State.update {
            it.copy(
                status = "Pending",
                terminalLogs = it.terminalLogs + "[AWS EC2 API] Allocation request received for ${it.instanceType}..." + "[AWS EC2 API] Provisioning ENI in subnet-6e7f829e... Assigned Private IP: ${it.privateIp}"
            )
        }
        viewModelScope.launch {
            delay(1500)
            _ec2State.update {
                it.copy(
                    status = "Running",
                    terminalLogs = it.terminalLogs + "[Cloud-Init] Bootstrapping instance... OK" + "[StatusCheck] 2/2 status checks passed. Instance state is RUNNING at ${it.publicIp}"
                )
            }
            _userProfile.update { it.copy(completedLabIds = it.completedLabIds + "lab-ec2") }
        }
    }

    fun stopEc2() {
        _ec2State.update {
            it.copy(
                status = "Stopping",
                terminalLogs = it.terminalLogs + "[AWS EC2 API] Sending shutdown signal..."
            )
        }
        viewModelScope.launch {
            delay(1000)
            _ec2State.update {
                it.copy(
                    status = "Stopped",
                    terminalLogs = it.terminalLogs + "[AWS EC2 API] Instance stopped. EBS volume retained."
                )
            }
        }
    }

    fun restartEc2() {
        stopEc2()
        viewModelScope.launch {
            delay(1200)
            launchEc2()
        }
    }

    // S3 Simulation Actions
    fun addS3Object(filename: String, sizeKb: Int, storageClass: String) {
        _s3State.update { current ->
            val newObjs = current.objects + S3ObjectItem(filename, sizeKb, storageClass, "Just now")
            val newCost = (current.totalCostPerMonth + (sizeKb / 1024.0 * 0.023)).coerceAtLeast(0.10)
            current.copy(objects = newObjs, totalCostPerMonth = String.format("%.2f", newCost).toDouble())
        }
    }

    fun deleteS3Object(key: String) {
        _s3State.update { current ->
            val newObjs = current.objects.filter { it.key != key }
            current.copy(objects = newObjs)
        }
    }

    fun toggleS3Versioning() {
        _s3State.update { it.copy(versioningEnabled = !it.versioningEnabled) }
    }

    fun setS3StorageClass(sc: String) {
        _s3State.update { it.copy(selectedStorageClass = sc) }
    }

    // IAM Simulation Actions
    fun updateIamEffect(effect: String) {
        _iamState.update { it.copy(effect = effect) }
    }

    fun toggleIamAction(action: String) {
        _iamState.update { current ->
            val list = current.selectedActions.toMutableList()
            if (list.contains(action)) list.remove(action) else list.add(action)
            current.copy(selectedActions = list)
        }
    }

    fun toggleIamConditionMfa() {
        _iamState.update { it.copy(conditionMfa = !it.conditionMfa) }
    }

    // DevOps Simulation Actions
    fun triggerDevOpsPipeline() {
        _devOpsState.update {
            it.copy(
                isPipelineRunning = true,
                sourceStatus = "In Progress",
                buildStatus = "Pending",
                testStatus = "Pending",
                deployStatus = "Pending",
                logOutput = it.logOutput + "[Pipeline] Triggered by manual deployment"
            )
        }
        viewModelScope.launch {
            delay(1000)
            _devOpsState.update { it.copy(sourceStatus = "Succeeded", buildStatus = "In Progress", logOutput = it.logOutput + "[Source] Downloaded repo artifact") }
            delay(1200)
            _devOpsState.update { it.copy(buildStatus = "Succeeded", testStatus = "In Progress", logOutput = it.logOutput + "[Build] Container build completed") }
            delay(1000)
            _devOpsState.update { it.copy(testStatus = "Succeeded", deployStatus = "In Progress", logOutput = it.logOutput + "[Test] All test suites green") }
            delay(1200)
            _devOpsState.update { it.copy(deployStatus = "Succeeded", isPipelineRunning = false, logOutput = it.logOutput + "[Deploy] Blue/Green shift verified!") }
        }
    }

    fun setTrafficShift(greenPercent: Int) {
        _devOpsState.update {
            it.copy(
                greenTrafficPercent = greenPercent,
                blueTrafficPercent = 100 - greenPercent
            )
        }
    }

    // CLI Terminal Actions
    fun executeCliCommand(cmd: String) {
        val trimmed = cmd.trim()
        if (trimmed.isEmpty()) return

        val userPromptLine = "awiskar@cloud-box:~$ $trimmed"
        val response = when {
            trimmed == "help" -> """
                Available Simulated AWS CLI Commands:
                  aws ec2 describe-instances
                  aws s3 ls
                  aws iam list-users
                  aws lambda list-functions
                  aws vpc describe-vpcs
                  aws sts get-caller-identity
                  clear
            """.trimIndent()
            trimmed == "clear" -> {
                _terminalHistory.value = emptyList()
                return
            }
            trimmed.startsWith("aws ec2 describe-instances") -> """
                {
                    "Reservations": [
                        {
                            "Instances": [
                                {
                                    "InstanceId": "i-09ab12cd34ef5678",
                                    "InstanceType": "${_ec2State.value.instanceType.substringBefore(" ")}",
                                    "State": { "Name": "${_ec2State.value.status.lowercase()}" },
                                    "PublicIpAddress": "${_ec2State.value.publicIp}",
                                    "PrivateIpAddress": "${_ec2State.value.privateIp}"
                                }
                            ]
                        }
                    ]
                }
            """.trimIndent()
            trimmed.startsWith("aws s3 ls") -> {
                "2026-10-01 08:30:12 ${_s3State.value.bucketName}\n2026-09-28 14:10:05 awiskar-prod-backups\n2026-09-20 09:12:44 awiskar-serverless-artifacts"
            }
            trimmed.startsWith("aws iam list-users") -> """
                {
                    "Users": [
                        { "UserName": "awiskar-admin", "CreateDate": "2026-01-10T10:00:00Z" },
                        { "UserName": "ci-cd-deployer", "CreateDate": "2026-02-14T11:22:00Z" },
                        { "UserName": "readonly-auditor", "CreateDate": "2026-05-01T08:15:00Z" }
                    ]
                }
            """.trimIndent()
            trimmed.startsWith("aws lambda list-functions") -> """
                {
                    "Functions": [
                        {
                            "FunctionName": "ProcessOrderFunction",
                            "Runtime": "nodejs20.x",
                            "MemorySize": 512,
                            "Timeout": 30
                        },
                        {
                            "FunctionName": "ResizeUploadedImage",
                            "Runtime": "python3.11",
                            "MemorySize": 1024,
                            "Timeout": 15
                        }
                    ]
                }
            """.trimIndent()
            trimmed.startsWith("aws sts get-caller-identity") -> """
                {
                    "UserId": "AROAEXAMPLE12345:awiskar",
                    "Account": "123456789012",
                    "Arn": "arn:aws:sts::123456789012:assumed-role/CloudEngineerRole/awiskar"
                }
            """.trimIndent()
            else -> "aws: error: argument '$trimmed' unrecognized. Type 'help' for examples."
        }

        _terminalHistory.update { it + userPromptLine + response }
    }

    // AI Tutor Chat
    fun setTutorMode(mode: String) {
        _tutorMode.value = mode
    }

    fun sendTutorQuestion(prompt: String) {
        val userMsg = TutorMessage("user", prompt, "Just now")
        _tutorMessages.update { it + userMsg }

        viewModelScope.launch {
            delay(600)
            val answer = when {
                prompt.contains("VPC", ignoreCase = true) -> "Amazon VPC (Virtual Private Cloud) lets you provision a logically isolated section of the AWS Cloud where you launch AWS resources in a virtual network you define. Think of it as your private datacenter inside AWS. You control IP ranges, subnets, route tables, and network gateways. For high availability, always spread subnets across at least two Availability Zones!"
                prompt.contains("EC2", ignoreCase = true) -> "Amazon EC2 provides resizable virtual compute capacity in the cloud. You choose CPU, RAM, storage, and networking specs via Instance Types (like t3 for general burstable workloads or c6i for compute-intensive tasks). Security groups act as virtual firewalls to control inbound and outbound traffic."
                prompt.contains("S3", ignoreCase = true) -> "Amazon S3 is object storage offering 11 9's of durability. Files ('objects') reside in 'buckets'. It features automatic replication across multiple datacenters and flexible storage tiers: S3 Standard for frequent access, S3 Intelligent-Tiering to auto-save money, and S3 Glacier for long-term compliance archiving."
                prompt.contains("IAM", ignoreCase = true) -> "AWS IAM manages authentication and authorization. Rule #1 of AWS security: NEVER use the root account for daily work! Instead, create IAM users with Multi-Factor Authentication (MFA), and use IAM Roles with temporary STS credentials whenever code or services (like EC2 or Lambda) need to access AWS resources."
                prompt.contains("Lambda", ignoreCase = true) -> "AWS Lambda is serverless event-driven compute. You write code, and Lambda runs it in response to events (like S3 uploads, HTTP API calls, or database changes). You pay strictly for execution milliseconds, with zero idle server cost and automatic horizontal scaling."
                prompt.contains("Interview", ignoreCase = true) -> "When answering cloud interview questions, structure your answers using the STAR method (Situation, Task, Action, Result) combined with the AWS Well-Architected Framework: Security, Reliability, Performance Efficiency, Cost Optimization, Operational Excellence, and Sustainability."
                else -> "In AWS cloud engineering, remember the core architectural tenet: 'Design for failure and nothing fails.' Always decouple components with queues (SQS), scale horizontally across multiple Availability Zones, and enforce the principle of least privilege in IAM."
            }
            val tutorMsg = TutorMessage("tutor", answer, "Just now")
            _tutorMessages.update { it + tutorMsg }
        }
    }
}
