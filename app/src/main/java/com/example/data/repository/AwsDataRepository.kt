package com.example.data.repository

import com.example.data.model.*

object AwsDataRepository {

    val services: List<AWSService> = listOf(
        AWSService(
            id = "ec2",
            name = "Amazon Elastic Compute Cloud",
            shortName = "EC2",
            category = ServiceCategory.COMPUTE,
            summary = "Scalable virtual servers (instances) in the cloud with custom hardware configurations, OS options, and elastic capacity.",
            beginnerExplanation = "Imagine renting a computer in an Amazon data center. You pick how much RAM, CPU, and disk space you want, install whatever software you need, and pay only for the seconds it runs.",
            whyItExists = "Before EC2, companies had to buy physical servers, rack them in data centers, and wait weeks for hardware delivery. EC2 lets you boot virtual servers in seconds.",
            keyConcepts = listOf(
                "Instance Types: General Purpose (t3, m6i), Compute (c6i), Memory (r6i), Storage (i3en), GPU (g4dn)",
                "AMIs (Amazon Machine Images): Pre-configured templates with OS and initial software",
                "EBS (Elastic Block Store): Persistent block storage volumes attached over the internal network",
                "Security Groups: Virtual stateful firewalls controlling inbound and outbound IP traffic",
                "User Data: Bootstrapping shell script executed automatically when the instance first launches",
                "Auto Scaling Groups (ASG): Automatically launches or terminates instances based on load demand"
            ),
            commonUseCases = listOf(
                "Hosting web applications, API microservices, and backend daemons",
                "Running legacy enterprise applications and Windows Server workloads",
                "Data processing pipelines and distributed batch computing"
            ),
            relatedServiceIds = listOf("ebs", "lambda", "ecs", "vpc"),
            difficulty = "Beginner",
            sampleCli = "aws ec2 run-instances --image-id ami-0c55b159cbfafe1f0 --count 1 --instance-type t3.micro --key-name MyKeyPair --security-group-ids sg-903004f8 --subnet-id subnet-6e7f829e",
            sampleIaC = """
                Resources:
                  WebServerInstance:
                    Type: AWS::EC2::Instance
                    Properties:
                      InstanceType: t3.micro
                      ImageId: ami-0c55b159cbfafe1f0
                      SecurityGroupIds:
                        - !Ref WebServerSecurityGroup
                      Tags:
                        - Key: Name
                          Value: WebServer-Prod
            """.trimIndent(),
            commonMistakes = listOf(
                "Leaving port 22 (SSH) open to 0.0.0.0/0 instead of your specific IP or using SSM Session Manager",
                "Assuming instance store storage is persistent across stops (it is ephemeral, use EBS!)",
                "Placing backend servers in public subnets instead of private subnets behind a NAT gateway"
            )
        ),
        AWSService(
            id = "s3",
            name = "Amazon Simple Storage Service",
            shortName = "S3",
            category = ServiceCategory.STORAGE,
            summary = "Object storage built to retrieve any amount of data from anywhere with 99.999999999% (11 9's) durability.",
            beginnerExplanation = "Think of S3 as an infinite digital hard drive accessible over the web. You upload files ('objects') into folders ('buckets') and can access them via URL or API anytime.",
            whyItExists = "Traditional file systems have maximum drive capacities, risk physical drive failures, and are hard to distribute globally. S3 scales automatically and replicates across multiple data centers.",
            keyConcepts = listOf(
                "Buckets: Globally unique containers for objects (must follow DNS naming rules)",
                "Objects: Consist of file data, a key (filename/path), and metadata",
                "Storage Classes: Standard, Intelligent-Tiering, Standard-IA, One Zone-IA, Glacier Instant/Flexible, Deep Archive",
                "Bucket Policies: JSON permission documents attached directly to buckets to control access",
                "Versioning: Protects against accidental deletes or overwrites by keeping file revisions",
                "Lifecycle Rules: Automatically transitions objects to cheaper tiers or deletes after X days"
            ),
            commonUseCases = listOf(
                "Hosting static single-page websites (HTML, CSS, JS, images)",
                "Storing user uploads (profile avatars, invoices, video uploads)",
                "Data lakes for analytics, big data, and machine learning training data",
                "Backup and disaster recovery archives"
            ),
            relatedServiceIds = listOf("cloudfront", "iam", "glacier"),
            difficulty = "Beginner",
            sampleCli = "aws s3 mb s3://my-cloud-learning-bucket-2026\naws s3 cp index.html s3://my-cloud-learning-bucket-2026/ --acl public-read",
            sampleIaC = """
                Resources:
                  StaticAssetsBucket:
                    Type: AWS::S3::Bucket
                    Properties:
                      BucketName: my-unique-company-assets-prod
                      VersioningConfiguration:
                        Status: Enabled
                      BucketEncryption:
                        ServerSideEncryptionConfiguration:
                          - ServerSideEncryptionByDefault:
                              SSEAlgorithm: AES256
            """.trimIndent(),
            commonMistakes = listOf(
                "Unintentionally disabling 'Block Public Access' and exposing company assets or logs to the internet",
                "Not enabling versioning on critical buckets, risking unrecoverable deletions",
                "Keeping rarely accessed petabytes in S3 Standard rather than configuring S3 Intelligent-Tiering"
            )
        ),
        AWSService(
            id = "vpc",
            name = "Amazon Virtual Private Cloud",
            shortName = "VPC",
            category = ServiceCategory.NETWORKING,
            summary = "Logically isolated virtual network dedicated to your AWS account with full control over IP addressing, subnets, route tables, and gateways.",
            beginnerExplanation = "VPC is your own private digital fenced-in property inside AWS. You decide who can enter from the internet, which servers are hidden in the back, and how computers talk to each other.",
            whyItExists = "Without VPC, every cloud server would be exposed to the public internet. VPC provides enterprise-grade network security and isolation.",
            keyConcepts = listOf(
                "CIDR Block: The private IP address range assigned to your VPC (e.g., 10.0.0.0/16)",
                "Public Subnet: Subnet whose route table has a direct route (0.0.0.0/0) to an Internet Gateway (IGW)",
                "Private Subnet: Subnet without a direct IGW route; uses NAT Gateway to access internet outbound only",
                "Route Tables: Rules determining where network packets are directed",
                "NAT Gateway: Network Address Translation device allowing private instances to download updates without inbound exposure",
                "Security Groups vs NACLs: Security Groups are stateful (instance level), NACLs are stateless (subnet level)"
            ),
            commonUseCases = listOf(
                "Three-tier architecture (Web in public subnet, App & Database in isolated private subnets)",
                "Connecting on-premises corporate datacenters via Site-to-Site VPN or AWS Direct Connect",
                "VPC Peering to route traffic between isolated VPCs using private IP addresses"
            ),
            relatedServiceIds = listOf("ec2", "route53", "cloudfront"),
            difficulty = "Intermediate",
            sampleCli = "aws ec2 create-vpc --cidr-block 10.0.0.0/16 --tag-specifications 'ResourceType=vpc,Tags=[{Key=Name,Value=ProductionVPC}]'",
            sampleIaC = """
                Resources:
                  ProductionVPC:
                    Type: AWS::EC2::VPC
                    Properties:
                      CidrBlock: 10.0.0.0/16
                      EnableDnsHostnames: true
                      EnableDnsSupport: true
                      Tags:
                        - Key: Name
                          Value: Prod-VPC
            """.trimIndent(),
            commonMistakes = listOf(
                "Allocating a CIDR block that is too small (e.g., /24) and quickly running out of available IP addresses",
                "Forgetting that AWS reserves 5 IP addresses in every subnet (first 4 and last 1)",
                "Blocking ephemeral return ports on stateless Network ACLs (NACLs)"
            )
        ),
        AWSService(
            id = "iam",
            name = "AWS Identity and Access Management",
            shortName = "IAM",
            category = ServiceCategory.SECURITY,
            summary = "Centrally manage permissions and credentials for users, groups, and compute resources with fine-grained policy documents.",
            beginnerExplanation = "IAM is the security bouncer of your AWS cloud. It checks who you are (Authentication) and what you are allowed to touch (Authorization).",
            whyItExists = "Sharing one master password or root account is catastrophic. IAM gives every developer and server their own identity with the exact minimal permissions required.",
            keyConcepts = listOf(
                "IAM Users: Individuals who sign in to AWS (console or CLI)",
                "IAM Groups: Collections of users that share common policies (e.g. Admins, Developers)",
                "IAM Roles: Temporary identities assumed by AWS services (like EC2 or Lambda) or federated users",
                "Policies: JSON documents declaring Effect (Allow/Deny), Actions, Resources, and Conditions",
                "Principle of Least Privilege: Grant only the minimum permissions necessary for the job",
                "MFA (Multi-Factor Authentication): Mandatory security layer for all console accounts"
            ),
            commonUseCases = listOf(
                "Granting an EC2 instance an IAM Role to read from an S3 bucket without hardcoded credentials",
                "Enforcing MFA for all developers and rotating access keys automatically",
                "Federating Google Workspace or Okta identities via SAML 2.0 / OpenID Connect"
            ),
            relatedServiceIds = listOf("kms", "cloudtrail", "secretsmanager"),
            difficulty = "Beginner",
            sampleCli = "aws iam create-user --user-name awiskar-developer\naws iam attach-user-policy --user-name awiskar-developer --policy-arn arn:aws:iam::aws:policy/ReadOnlyAccess",
            sampleIaC = """
                Resources:
                  AppInstanceRole:
                    Type: AWS::IAM::Role
                    Properties:
                      AssumeRolePolicyDocument:
                        Version: "2012-10-17"
                        Statement:
                          - Effect: Allow
                            Principal:
                              Service: ec2.amazonaws.com
                            Action: sts:AssumeRole
            """.trimIndent(),
            commonMistakes = listOf(
                "Using the Root account for daily development and creating root access keys",
                "Attaching AdministratorAccess to service roles or general developer groups",
                "Hardcoding AWS secret access keys in git repositories instead of using IAM Roles"
            )
        ),
        AWSService(
            id = "lambda",
            name = "AWS Lambda",
            shortName = "Lambda",
            category = ServiceCategory.SERVERLESS,
            summary = "Run code without provisioning or managing servers. Scales automatically from zero to thousands of requests and pay only for compute time consumed.",
            beginnerExplanation = "Write a function (in Python, Node.js, Go, or Java), upload it to AWS, and tell it when to run (e.g., when a file is uploaded to S3 or an HTTP request arrives). When not running, you pay \$0.",
            whyItExists = "Traditional servers cost money 24/7 even when idle. Lambda eliminates server management, capacity planning, OS patching, and idle costs.",
            keyConcepts = listOf(
                "Event-Driven: Triggered by S3 events, API Gateway requests, SQS queues, EventBridge cron schedules",
                "Stateless: Each invocation runs in a fresh isolated execution environment",
                "Cold Starts: Initial container spin-up delay on first invocation or after scaling up",
                "Timeout Limit: Maximum execution duration is 15 minutes (900 seconds)",
                "Concurrency: Number of simultaneous function executions handling active requests",
                "Memory & CPU: Memory scales from 128 MB to 10 GB; CPU power scales proportionally to memory"
            ),
            commonUseCases = listOf(
                "Serverless REST & GraphQL APIs with Amazon API Gateway",
                "Real-time image thumbnail generation when photos are uploaded to S3",
                "Automated nightly cron jobs, log scrapers, and database cleanups",
                "Event-driven ETL pipelines with SQS and DynamoDB Streams"
            ),
            relatedServiceIds = listOf("apigateway", "dynamodb", "s3", "eventbridge"),
            difficulty = "Intermediate",
            sampleCli = "aws lambda create-function --function-name resizeImage --runtime python3.11 --role arn:aws:iam::123456789012:role/lambda-role --handler index.handler --zip-file fileb://function.zip",
            sampleIaC = """
                Resources:
                  ProcessOrderFunction:
                    Type: AWS::Lambda::Function
                    Properties:
                      FunctionName: ProcessOrder
                      Runtime: nodejs20.x
                      Handler: index.handler
                      Role: !GetAtt LambdaExecutionRole.Arn
                      Timeout: 30
                      MemorySize: 512
            """.trimIndent(),
            commonMistakes = listOf(
                "Treating Lambda as a stateful server and storing persistent files on local disk outside /tmp",
                "Setting an insufficient timeout causing functions to fail mid-processing",
                "Not monitoring concurrent executions, leading to hitting AWS account concurrency limits"
            )
        ),
        AWSService(
            id = "rds",
            name = "Amazon Relational Database Service",
            shortName = "RDS",
            category = ServiceCategory.DATABASE,
            summary = "Managed relational database service supporting PostgreSQL, MySQL, MariaDB, Oracle, and Microsoft SQL Server with automated backups and Multi-AZ failover.",
            beginnerExplanation = "Running your own database is stressful (OS patches, backups, replication, failover). RDS automates all the maintenance while letting you connect using standard SQL.",
            whyItExists = "Database corruption, unpatched vulnerabilities, and lost data can ruin a company. RDS makes enterprise-grade database management point-and-click.",
            keyConcepts = listOf(
                "Engines: PostgreSQL, MySQL, MariaDB, Oracle, SQL Server, and Amazon Aurora",
                "Multi-AZ Deployment: Synchronous replication to a standby instance in another AZ for zero-downtime failover",
                "Read Replicas: Asynchronous copies in the same or different regions to offload read-heavy SQL queries",
                "Automated Backups: Point-in-time recovery up to 35 days with continuous transaction log archiving",
                "DB Subnet Group: Collection of subnets across at least 2 AZs where the database can run privately"
            ),
            commonUseCases = listOf(
                "E-commerce order catalogs requiring strict ACID transactional integrity",
                "Enterprise ERP, CRM, and financial accounting databases",
                "SaaS platforms needing complex relational SQL queries and foreign key constraints"
            ),
            relatedServiceIds = listOf("aurora", "dynamodb", "elasticache", "ec2"),
            difficulty = "Intermediate",
            sampleCli = "aws rds create-db-instance --db-instance-identifier prod-db --db-instance-class db.t4g.medium --engine postgres --master-username dbadmin --master-user-password 'SuperSecret123!' --allocated-storage 50 --multi-az",
            sampleIaC = """
                Resources:
                  ProductionDatabase:
                    Type: AWS::RDS::DBInstance
                    Properties:
                      DBInstanceClass: db.t4g.medium
                      Engine: postgres
                      AllocatedStorage: 50
                      MultiAZ: true
                      PubliclyAccessible: false
                      VPCSecurityGroups:
                        - !Ref DBSecurityGroup
            """.trimIndent(),
            commonMistakes = listOf(
                "Setting PubliclyAccessible to true instead of placing RDS inside private database subnets",
                "Neglecting Multi-AZ on production databases, leaving them vulnerable to single-datacenter outages",
                "Not sizing IOPS properly, causing database queries to queue up and throttle under peak load"
            )
        ),
        AWSService(
            id = "dynamodb",
            name = "Amazon DynamoDB",
            shortName = "DynamoDB",
            category = ServiceCategory.DATABASE,
            summary = "Fully managed, serverless, key-value and document NoSQL database designed for single-digit millisecond performance at any scale.",
            beginnerExplanation = "A lightning-fast, serverless database that doesn't use SQL tables. It holds documents (JSON) and can handle 10 requests per second or 10 million per second without breaking a sweat.",
            whyItExists = "Traditional SQL databases struggle to scale horizontally past a single primary server. DynamoDB partitions data across SSDs automatically, scaling infinitely.",
            keyConcepts = listOf(
                "Partition Key (PK): Hash key determining which physical partition stores the item",
                "Sort Key (SK): Range key enabling 1-to-many relationships and range queries within a partition",
                "Global Secondary Indexes (GSI): Alternate partition and sort keys allowing query flexibility across the entire table",
                "On-Demand vs Provisioned: Pay per request or set reserved Read/Write Capacity Units (RCUs/WCUs)",
                "DynamoDB Streams: Time-ordered stream of item-level changes that trigger Lambda functions",
                "Point-in-Time Recovery (PITR): Continuous automated backups with 35-day restore capability"
            ),
            commonUseCases = listOf(
                "User session stores, shopping carts, and leaderboard tracking",
                "High-throughput IoT device telemetry and event tracking",
                "Serverless microservices with ultra-low latency requirements"
            ),
            relatedServiceIds = listOf("lambda", "rds", "elasticache"),
            difficulty = "Intermediate",
            sampleCli = "aws dynamodb create-table --table-name Users --attribute-definitions AttributeName=UserId,AttributeType=S --key-schema AttributeName=UserId,KeyType=HASH --billing-mode PAY_PER_REQUEST",
            sampleIaC = """
                Resources:
                  UsersTable:
                    Type: AWS::DynamoDB::Table
                    Properties:
                      TableName: ProdUsers
                      BillingMode: PAY_PER_REQUEST
                      AttributeDefinitions:
                        - AttributeName: UserId
                          AttributeType: S
                      KeySchema:
                        - AttributeName: UserId
                          KeyType: HASH
            """.trimIndent(),
            commonMistakes = listOf(
                "Attempting to treat DynamoDB like a relational SQL database and running table Scans instead of Queries",
                "Designing poorly distributed partition keys leading to 'hot partition' throttling",
                "Over-indexing with too many GSIs, which multiplies write costs"
            )
        ),
        AWSService(
            id = "cloudfront",
            name = "Amazon CloudFront",
            shortName = "CloudFront",
            category = ServiceCategory.NETWORKING,
            summary = "Fast, highly secure Content Delivery Network (CDN) that delivers data, videos, applications, and APIs globally with low latency.",
            beginnerExplanation = "Instead of users in Tokyo waiting 250 milliseconds for a server in Virginia to send an image, CloudFront caches a copy in Tokyo so it loads in 10 milliseconds.",
            whyItExists = "Speed of light across physical undersea fiber cables introduces latency. CloudFront brings content physically closer to users through 600+ Edge Locations.",
            keyConcepts = listOf(
                "Origins: The source of content (S3 bucket, ALB, EC2, or custom HTTP server)",
                "Edge Locations: AWS Points of Presence (PoPs) worldwide caching content",
                "Origin Access Control (OAC): Secures S3 buckets so users can only access content through CloudFront",
                "TTL (Time to Live): How long edge caches keep a cached object before refreshing from origin",
                "SSL/TLS & ACM: Free SSL certificate provisioning and HTTPS termination at the edge",
                "CloudFront Functions & Lambda@Edge: Execute code at the edge to modify headers, URLs, or cookies"
            ),
            commonUseCases = listOf(
                "Global delivery of static website assets, videos, and downloads",
                "API acceleration and DDoS protection alongside AWS WAF and AWS Shield",
                "Geographic content restrictions and A/B testing at the edge"
            ),
            relatedServiceIds = listOf("s3", "route53", "waf"),
            difficulty = "Intermediate",
            sampleCli = "aws cloudfront create-invalidation --distribution-id E1A2B3C4D5E6F7 --paths '/*'",
            sampleIaC = """
                Resources:
                  CDN:
                    Type: AWS::CloudFront::Distribution
                    Properties:
                      DistributionConfig:
                        Enabled: true
                        DefaultRootObject: index.html
                        Origins:
                          - Id: S3Origin
                            DomainName: !GetAtt S3Bucket.RegionalDomainName
            """.trimIndent(),
            commonMistakes = listOf(
                "Forgetting to invalidate the cache after deploying website updates",
                "Leaving S3 buckets directly public instead of restricting them exclusively to CloudFront via OAC"
            )
        ),
        AWSService(
            id = "route53",
            name = "Amazon Route 53",
            shortName = "Route 53",
            category = ServiceCategory.NETWORKING,
            summary = "Highly available and scalable cloud Domain Name System (DNS) web service with health checking and advanced routing policies.",
            beginnerExplanation = "The internet's phonebook. It translates human names like 'awiskar.com' into computer IP addresses like '54.239.28.85'.",
            whyItExists = "DNS is the single point of entry for all web traffic. Route 53 guarantees 100% SLA availability using a globally distributed Anycast network.",
            keyConcepts = listOf(
                "Hosted Zones: Container for DNS records corresponding to a domain name (Public or Private)",
                "Record Types: A (IPv4), AAAA (IPv6), CNAME (alias to another name), Alias (AWS specific pointer to ALB/S3/CloudFront)",
                "Routing Policies: Simple, Weighted (A/B testing), Latency-based, Failover (active-passive DR), Geolocation",
                "DNS Health Checks: Actively probes web servers and routes traffic away from unhealthy endpoints"
            ),
            commonUseCases = listOf(
                "Domain registration and DNS hosting",
                "Multi-region active-active or active-passive disaster recovery failover",
                "Internal private DNS for microservices inside a VPC"
            ),
            relatedServiceIds = listOf("cloudfront", "vpc", "ec2"),
            difficulty = "Intermediate",
            sampleCli = "aws route53 list-hosted-zones",
            sampleIaC = "Resources:\n  DnsRecord:\n    Type: AWS::Route53::RecordSet\n    Properties:\n      HostedZoneName: example.com.\n      Name: app.example.com.\n      Type: A\n      AliasTarget:\n        HostedZoneId: Z2FDTNDATAQYW2\n        DNSName: d12345.cloudfront.net",
            commonMistakes = listOf("Using CNAME at the zone apex (root domain) instead of Route 53 Alias records")
        ),
        AWSService(
            id = "ecs",
            name = "Amazon Elastic Container Service",
            shortName = "ECS",
            category = ServiceCategory.CONTAINERS,
            summary = "Fully managed container orchestration service that makes it easy to deploy, manage, and scale containerized applications using Docker.",
            beginnerExplanation = "Package your app and dependencies into a Docker container. ECS runs hundreds of those containers reliably across a cluster of servers.",
            whyItExists = "Managing Docker across dozens of physical servers manually is prone to crashes. ECS schedules containers, monitors health, and handles auto-scaling.",
            keyConcepts = listOf(
                "Task Definition: JSON blueprint defining Docker images, CPU/RAM, environment variables, ports",
                "Task: A running instantiation of a Task Definition",
                "Service: Maintains a specified number of running tasks and connects them to a Load Balancer",
                "Launch Types: EC2 (you manage instances) vs Fargate (serverless containers, no EC2 management!)"
            ),
            commonUseCases = listOf(
                "Production microservices architectures running Docker containers",
                "Migrating monolithic apps into modernized containerized deployments"
            ),
            relatedServiceIds = listOf("fargate", "ecr", "eks"),
            difficulty = "Intermediate",
            sampleCli = "aws ecs create-cluster --cluster-name production-cluster",
            sampleIaC = "Resources:\n  ECSCluster:\n    Type: AWS::ECS::Cluster\n    Properties:\n      ClusterName: ProductionCluster",
            commonMistakes = listOf("Hardcoding database passwords in container environment variables instead of referencing Secrets Manager")
        ),
        AWSService(
            id = "fargate",
            name = "AWS Fargate",
            shortName = "Fargate",
            category = ServiceCategory.CONTAINERS,
            summary = "Serverless compute engine for containers that works with both Amazon ECS and Amazon EKS without managing servers.",
            beginnerExplanation = "Run Docker containers without worrying about EC2 instances, AMI updates, or cluster patching. You only specify CPU and memory.",
            whyItExists = "Operating EC2 worker nodes for containers requires patching and OS maintenance. Fargate takes care of the infrastructure so you only build containers.",
            keyConcepts = listOf(
                "Serverless Container Execution: Pay strictly for vCPU and memory allocated per second",
                "Isolated Kernel: Each Fargate task runs in its own dedicated microVM for supreme isolation",
                "Integrated Networking: Every task gets its own Elastic Network Interface (ENI) with private IP"
            ),
            commonUseCases = listOf(
                "Batch processing jobs and queue workers",
                "Microservice APIs where zero server administration is desired"
            ),
            relatedServiceIds = listOf("ecs", "eks", "lambda"),
            difficulty = "Intermediate",
            sampleCli = "aws ecs run-task --cluster prod --task-definition app:1 --launch-type FARGATE",
            sampleIaC = "TaskDefinition:\n  Type: AWS::ECS::TaskDefinition\n  Properties:\n    RequiresCompatibilities:\n      - FARGATE\n    Cpu: '256'\n    Memory: '512'",
            commonMistakes = listOf("Choosing large vCPU configurations for low-traffic tasks resulting in unnecessary container spend")
        ),
        AWSService(
            id = "cloudwatch",
            name = "Amazon CloudWatch",
            shortName = "CloudWatch",
            category = ServiceCategory.MANAGEMENT,
            summary = "Monitoring and observability service that provides data and actionable insights for AWS resources and applications.",
            beginnerExplanation = "The dashboard and smoke detector for your cloud. It tracks server CPU usage, collects application logs, and sends alerts when something breaks.",
            whyItExists = "You cannot fix what you cannot measure. CloudWatch collects real-time telemetry across all AWS services in one central location.",
            keyConcepts = listOf(
                "Metrics: Variables representing performance data over time (e.g. CPUUtilization, 5xxErrorRate)",
                "Alarms: Watches a metric and triggers automated actions (SNS alert, Auto Scaling action) if threshold is breached",
                "Logs Insights: Fast interactive search and query engine for application and access logs",
                "Dashboards: Visual panels plotting health and operational metrics in real-time"
            ),
            commonUseCases = listOf(
                "Triggering EC2 Auto Scaling when average CPU exceeds 75% for 3 minutes",
                "Alerting the on-call engineer via Slack/SMS when HTTP 500 error rate spikes",
                "Debugging application exceptions using centralized CloudWatch Logs"
            ),
            relatedServiceIds = listOf("sns", "cloudtrail", "ec2"),
            difficulty = "Beginner",
            sampleCli = "aws cloudwatch put-metric-alarm --alarm-name HighCPU --metric-name CPUUtilization --namespace AWS/EC2 --statistic Average --period 300 --threshold 80 --comparison-operator GreaterThanThreshold",
            sampleIaC = "Resources:\n  CPUAlarm:\n    Type: AWS::CloudWatch::Alarm\n    Properties:\n      AlarmName: HighCPUAlarm\n      MetricName: CPUUtilization\n      Namespace: AWS/EC2\n      Threshold: 80\n      Period: 300",
            commonMistakes = listOf("Not setting log retention periods, causing log groups to store debug data forever and accumulate high storage bills")
        )
    )

    val modules: List<LearningModule> = listOf(
        LearningModule(
            id = "mod-cloud-fundamentals",
            levelNumber = 1,
            title = "Level 1: Cloud Fundamentals",
            subtitle = "Master core distributed systems, cloud models, and economics",
            category = ServiceCategory.COMPUTE,
            labId = "lab-cloud-concepts",
            lessons = listOf(
                Lesson(
                    id = "cf-01",
                    moduleId = "mod-cloud-fundamentals",
                    title = "What is Cloud Computing?",
                    estimatedMinutes = 12,
                    difficulty = "Beginner",
                    summary = "Understand on-demand computing, elastic scaling, and pay-as-you-go economics.",
                    sections = listOf(
                        LessonSection(
                            heading = "The Shift from On-Premises to Cloud",
                            body = "Before cloud computing, deploying software meant buying physical server hardware, leasing datacenter rack space, configuring cooling and power backups, and waiting weeks. Cloud computing is the on-demand delivery of IT resources over the internet with pay-as-you-go pricing.\n\nKey advantages include:\n• Trade capital expense (CapEx) for variable expense (OpEx)\n• Massive economies of scale\n• Stop guessing capacity (scale elastically)\n• Increase speed and agility\n• Go global in minutes",
                            keyTakeaway = "Cloud turns infrastructure from an expensive upfront physical purchase into an elastic utility like electricity."
                        ),
                        LessonSection(
                            heading = "Cloud Deployment Models: Public, Private & Hybrid",
                            body = "• Public Cloud: Infrastructure owned and operated by a third-party cloud provider (e.g., AWS) delivered over the internet.\n• Private Cloud: Cloud infrastructure used exclusively by a single business or organization, hosted on-premises or by third parties.\n• Hybrid Cloud: Connects public cloud and on-premises infrastructure, allowing data and applications to be shared across both environments.",
                            diagramType = "INFRA_GLOBAL"
                        ),
                        LessonSection(
                            heading = "The Three Cloud Service Models: IaaS, PaaS, SaaS",
                            body = "1. IaaS (Infrastructure as a Service): You rent basic computing blocks (virtual machines, networks, storage). Example: Amazon EC2, Amazon VPC. You manage OS, runtime, and app.\n2. PaaS (Platform as a Service): The provider manages the hardware, OS, and runtimes. You just deploy your code. Example: AWS Elastic Beanstalk, AWS App Runner.\n3. SaaS (Software as a Service): Complete end-user application managed entirely by the vendor. Example: Slack, Google Workspace.",
                            codeSnippet = """
                                # IaaS vs PaaS vs SaaS responsibility:
                                On-Premises: You manage Everything
                                IaaS: AWS manages Hardware & Virtualization; You manage OS & Apps
                                PaaS: AWS manages Hardware, OS, Runtime; You manage App & Data
                                SaaS: Vendor manages Everything; You just consume the service
                            """.trimIndent(),
                            codeLanguage = "text"
                        )
                    ),
                    quizQuestion = QuizQuestion(
                        id = "q-cf-01",
                        question = "Which cloud service model provides maximum architectural control over the operating system, network configuration, and disk volumes?",
                        options = listOf("SaaS (Software as a Service)", "IaaS (Infrastructure as a Service)", "PaaS (Platform as a Service)", "FaaS (Function as a Service)"),
                        correctIndex = 1,
                        explanation = "IaaS (Infrastructure as a Service), such as Amazon EC2, gives developers full control over OS configuration, firewall rules, and virtual hardware.",
                        category = "Cloud Fundamentals"
                    )
                ),
                Lesson(
                    id = "cf-02",
                    moduleId = "mod-cloud-fundamentals",
                    title = "High Availability, Fault Tolerance & Scalability",
                    estimatedMinutes = 15,
                    difficulty = "Beginner",
                    summary = "Learn why cloud systems survive hardware failures and how elasticity prevents outages.",
                    sections = listOf(
                        LessonSection(
                            heading = "Defining High Availability (HA) vs Fault Tolerance (FT)",
                            body = "High Availability (HA) ensures a system remains accessible and operational with minimal downtime (e.g. 99.99% 'four nines' SLA). If one server crashes, a health check detects it and traffic is rerouted within seconds.\n\nFault Tolerance (FT) is even stricter: a system operates without ANY interruption or degraded performance even when a physical component fails completely (usually requiring duplicate hot-standby hardware operating in lockstep).",
                            keyTakeaway = "High Availability allows a brief recovery glitch; Fault Tolerance guarantees zero interruption."
                        ),
                        LessonSection(
                            heading = "Vertical vs Horizontal Scaling",
                            body = "• Vertical Scaling (Scale Up): Adding more CPU, RAM, or storage to a single server (e.g., upgrading an EC2 instance from t3.micro to r6i.4xlarge). Has hard physical limits and requires a restart.\n• Horizontal Scaling (Scale Out): Adding MORE servers behind a load balancer (e.g., 2 instances → 10 instances). True cloud elasticity relies on horizontal scaling.",
                            codeSnippet = """
                                Vertical Scaling:   [ t3.micro ] -> [ c6i.16xlarge ] (Single point of failure)
                                Horizontal Scaling: [ t3.micro ] + [ t3.micro ] + [ t3.micro ] (Distributed, Resilient)
                            """.trimIndent(),
                            codeLanguage = "text"
                        )
                    ),
                    quizQuestion = QuizQuestion(
                        id = "q-cf-02",
                        question = "What is the primary architectural benefit of Horizontal Scaling over Vertical Scaling?",
                        options = listOf(
                            "It simplifies database migrations",
                            "It eliminates single points of failure and allows near-infinite scaling without server downtime",
                            "It requires fewer IP addresses",
                            "It completely eliminates the need for load balancers"
                        ),
                        correctIndex = 1,
                        explanation = "Horizontal scaling distributes traffic across multiple instances, ensuring that if one instance crashes, the others continue serving traffic seamlessly.",
                        category = "Architecture"
                    )
                )
            )
        ),
        LearningModule(
            id = "mod-aws-fundamentals",
            levelNumber = 2,
            title = "Level 2: AWS Global Infrastructure & IAM",
            subtitle = "Regions, Availability Zones, and the AWS Security Model",
            category = ServiceCategory.SECURITY,
            labId = "lab-iam",
            lessons = listOf(
                Lesson(
                    id = "aws-01",
                    moduleId = "mod-aws-fundamentals",
                    title = "AWS Regions & Availability Zones",
                    estimatedMinutes = 15,
                    difficulty = "Beginner",
                    summary = "Understand physical global geography: Regions, AZs, and Edge Locations.",
                    sections = listOf(
                        LessonSection(
                            heading = "Physical Architecture of AWS",
                            body = "AWS infrastructure is organized hierarchically:\n\n1. Regions: Physical geographic locations around the world (e.g., us-east-1, eu-west-1, ap-southeast-1). Each Region consists of multiple isolated and physically separated AZs.\n\n2. Availability Zones (AZs): One or more discrete datacenters with independent redundant power, networking, and cooling. AZs within a region are connected via ultra-low latency private optical networks (< 1-2 ms).\n\n3. Edge Locations / Points of Presence (PoPs): Over 600 caching locations globally used by CloudFront and Route 53 to serve content close to users.",
                            keyTakeaway = "Always deploy across at least 2 Availability Zones for high availability."
                        ),
                        LessonSection(
                            heading = "How to Choose an AWS Region",
                            body = "When selecting a region for your application, evaluate:\n1. Compliance & Data Sovereignty: Some countries legally require citizen data to reside within national borders.\n2. Latency: Choose regions physically closest to your end users.\n3. Service Availability: Not every cutting-edge AWS service is available in every region on day one.\n4. Pricing: Taxes, energy costs, and logistics make some regions cheaper than others (e.g., us-east-1 is generally cheaper than sa-east-1)."
                        )
                    )
                ),
                Lesson(
                    id = "aws-02",
                    moduleId = "mod-aws-fundamentals",
                    title = "IAM: Users, Groups, Roles & Policies",
                    estimatedMinutes = 20,
                    difficulty = "Beginner",
                    summary = "Zero-trust identity management and least-privilege policies in AWS.",
                    sections = listOf(
                        LessonSection(
                            heading = "The Difference Between Users and Roles",
                            body = "• IAM Users: Represent a person or external system with long-term credentials (password, access key). Avoid using user keys for code running inside AWS!\n\n• IAM Roles: Intended to be ASSUMED temporarily by whoever needs them (e.g., an EC2 instance, Lambda function, or federated corporate user). AWS STS automatically generates short-lived temporary security credentials that rotate automatically.",
                            codeSnippet = """
                                {
                                  "Version": "2012-10-17",
                                  "Statement": [
                                    {
                                      "Effect": "Allow",
                                      "Action": [
                                        "s3:GetObject",
                                        "s3:ListBucket"
                                      ],
                                      "Resource": [
                                        "arn:aws:s3:::my-production-bucket",
                                        "arn:aws:s3:::my-production-bucket/*"
                                      ]
                                    }
                                  ]
                                }
                            """.trimIndent(),
                            codeLanguage = "json",
                            keyTakeaway = "Never bake AWS secret keys into EC2 code. Attach an IAM Role to the instance profile instead."
                        )
                    )
                )
            )
        ),
        LearningModule(
            id = "mod-compute",
            levelNumber = 3,
            title = "Level 3: Compute Academy (EC2 & Lambda)",
            subtitle = "Deep dive into virtual machines, security groups, and serverless compute",
            category = ServiceCategory.COMPUTE,
            labId = "lab-ec2",
            lessons = listOf(
                Lesson(
                    id = "ec2-01",
                    moduleId = "mod-compute",
                    title = "EC2 Architecture & Security Groups",
                    estimatedMinutes = 20,
                    difficulty = "Intermediate",
                    summary = "Instance types, AMIs, root EBS volumes, and stateful firewall configurations.",
                    sections = listOf(
                        LessonSection(
                            heading = "How Security Groups Protect EC2",
                            body = "A Security Group acts as a virtual firewall for your EC2 instances to control incoming and outgoing traffic. Unlike traditional OS firewalls:\n• They are STATEFUL: If you allow inbound traffic on port 80, the return outbound response is automatically permitted regardless of outbound rules.\n• All inbound traffic is BLOCKED by default until you add an allow rule.\n• All outbound traffic is ALLOWED by default.",
                            keyTakeaway = "Security groups cannot have explicit 'Deny' rules—they only contain 'Allow' rules."
                        )
                    )
                )
            )
        ),
        LearningModule(
            id = "mod-networking",
            levelNumber = 4,
            title = "Level 4: Cloud Networking Laboratory (VPC)",
            subtitle = "CIDR blocks, Subnets, Internet Gateways, and Route Tables",
            category = ServiceCategory.NETWORKING,
            labId = "lab-vpc",
            lessons = listOf(
                Lesson(
                    id = "vpc-01",
                    moduleId = "mod-networking",
                    title = "Building a Multi-Tier Production VPC",
                    estimatedMinutes = 25,
                    difficulty = "Intermediate",
                    summary = "Design a bulletproof VPC layout with public and private subnets across multiple AZs.",
                    sections = listOf(
                        LessonSection(
                            heading = "The 3-Tier Enterprise Network Topology",
                            body = "A battle-tested VPC design divides IP subnets into 3 tiers across at least 2 Availability Zones:\n\n1. Public Subnet (Web Tier): Internet-facing Application Load Balancers and Bastion hosts. Route table directs 0.0.0.0/0 to an Internet Gateway (IGW).\n\n2. Private App Subnet (Application Tier): EC2 or ECS backend services. Route table directs 0.0.0.0/0 to a NAT Gateway in the public subnet for outbound-only internet (patches, APIs).\n\n3. Private Database Subnet (Data Tier): RDS or Aurora clusters. NO route to the internet whatsoever! Accessible only from the Application Tier security group on port 5432 / 3306.",
                            diagramType = "VPC_FLOW",
                            keyTakeaway = "Databases should NEVER have public IP addresses or routes to an Internet Gateway."
                        )
                    )
                )
            )
        )
    )

    val troubleshootingIncidents: List<TroubleshootingIncident> = listOf(
        TroubleshootingIncident(
            id = "inc-01",
            title = "Production Web Tier Returning HTTP 504 Gateway Timeout",
            severity = "P1 - Critical",
            symptom = "Users visiting https://app.example.com receive '504 Gateway Time-out'. The Application Load Balancer is healthy, but clients cannot reach backend API servers.",
            architectureContext = "Internet -> Route 53 -> ALB (Public Subnet) -> EC2 Backend Instances (Private Subnet) -> RDS Postgres",
            simulatedLogs = listOf(
                "10:14:02 [ALB-AccessLog] client=198.51.100.22 elb=app-alb target=10.0.2.45:8080 request_processing_time=0.001 target_processing_time=-1 response_processing_time=-1 elb_status_code=504 target_status_code=-",
                "10:14:05 [EC2-Node-1] [INFO] Express server listening on 0.0.0.0:8080",
                "10:14:10 [ALB-HealthCheck] Target 10.0.2.45:8080 failed health check: Connection timed out"
            ),
            simulatedMetrics = mapOf(
                "ALB TargetResponseTime" to "Timeout (>15s)",
                "EC2 CPU Utilization" to "4.2% (Idle)",
                "UnhealthyHostCount" to "4 of 4 instances Unhealthy"
            ),
            options = listOf(
                IncidentOption(
                    id = "opt-1",
                    description = "Upgrade EC2 instance types from t3.micro to c6i.4xlarge due to CPU saturation",
                    isCorrect = false,
                    diagnosticReason = "EC2 CPU is only at 4.2%. The servers are completely idle because traffic isn't reaching them."
                ),
                IncidentOption(
                    id = "opt-2",
                    description = "Fix EC2 Security Group inbound rule: Allow TCP port 8080 from the ALB Security Group ID (sg-alb)",
                    isCorrect = true,
                    diagnosticReason = "Correct! Target processing time was -1 and connection timed out. The EC2 security group was missing an inbound rule allowing traffic from the load balancer security group on port 8080."
                ),
                IncidentOption(
                    id = "opt-3",
                    description = "Attach an Internet Gateway directly to the private subnet route table",
                    isCorrect = false,
                    diagnosticReason = "Attaching an IGW to private subnets breaks network isolation and doesn't resolve the ALB to EC2 firewall block."
                )
            ),
            explanation = "A 504 Gateway Timeout from an ALB means the load balancer attempted to connect to the target EC2 instances, but the connection timed out before the instance responded. Since the app is running on port 8080 and CPU is low, the culprit is the EC2 Security Group blocking port 8080 from the ALB's security group."
        ),
        TroubleshootingIncident(
            id = "inc-02",
            title = "Lambda Function Fails with S3 'Access Denied' Error",
            severity = "P2 - High",
            symptom = "A background Lambda function that compresses user profile uploads crashes with 'botocore.exceptions.ClientError: An error occurred (AccessDenied) when calling the PutObject operation'.",
            architectureContext = "S3 Uploads Bucket -> S3 Event Notification -> Lambda Function -> S3 Compressed Bucket",
            simulatedLogs = listOf(
                "2026-10-01T12:00:01.120Z START RequestId: 4f3a2c1e-9b8a",
                "2026-10-01T12:00:01.350Z [INFO] Processing image: user_avatar_99.png from bucket 'source-uploads'",
                "2026-10-01T12:00:01.890Z [ERROR] ClientError: An error occurred (AccessDenied) when calling the PutObject operation: Access Denied",
                "2026-10-01T12:00:01.910Z END RequestId: 4f3a2c1e-9b8a Duration: 790 ms"
            ),
            simulatedMetrics = mapOf(
                "Lambda Invocations" to "142 / min",
                "Lambda Errors" to "142 / min (100% Failure Rate)",
                "Duration" to "790 ms"
            ),
            options = listOf(
                IncidentOption(
                    id = "opt-1",
                    description = "Increase Lambda function timeout from 3 seconds to 15 minutes",
                    isCorrect = false,
                    diagnosticReason = "The function failed in 790ms with an explicit AccessDenied error, well before any timeout."
                ),
                IncidentOption(
                    id = "opt-2",
                    description = "Update the Lambda Execution Role IAM policy to grant 's3:PutObject' on 'arn:aws:s3:::compressed-bucket/*'",
                    isCorrect = true,
                    diagnosticReason = "Correct! The Lambda function's IAM execution role was only granted 's3:GetObject' permissions on the source bucket, not 's3:PutObject' on the target bucket."
                ),
                IncidentOption(
                    id = "opt-3",
                    description = "Make the S3 compressed bucket completely public to 0.0.0.0/0",
                    isCorrect = false,
                    diagnosticReason = "Dangerous anti-pattern! Making buckets public violates security compliance and doesn't fix missing IAM permissions on Lambda."
                )
            ),
            explanation = "AWS services assume IAM Roles to interact with other services. The Lambda execution role had read permissions for the source bucket, but was missing 's3:PutObject' permission on the destination bucket resource ARN."
        )
    )

    val projects: List<CloudProject> = listOf(
        CloudProject(
            id = "proj-01",
            title = "Resilient 3-Tier Web Application on AWS",
            level = "Beginner",
            estimatedHours = "3-4 hours",
            objective = "Deploy a production-grade, highly available web application with an ALB, Auto Scaling group of EC2 instances, and Multi-AZ RDS PostgreSQL database across two AZs.",
            architectureFlow = "Users -> Route 53 -> ALB -> Auto Scaling EC2 (Private Subnet) -> RDS Multi-AZ (DB Subnet)",
            tasks = listOf(
                ProjectTask("task-1", "Design VPC with 2 Public, 2 Private App, and 2 Private DB Subnets", "Ensure distinct CIDR blocks (10.0.1.0/24 through 10.0.6.0/24)", true),
                ProjectTask("task-2", "Configure Internet Gateway and NAT Gateway with Route Tables", "Verify public subnet routes to IGW and private subnets route to NAT Gateway", true),
                ProjectTask("task-3", "Create Security Groups with strict principle of least privilege", "ALB accepts 80/443; EC2 accepts 8080 only from ALB SG; RDS accepts 5432 only from EC2 SG", false),
                ProjectTask("task-4", "Launch Multi-AZ RDS PostgreSQL in Private DB Subnet Group", "Enable automated backups and storage auto-scaling", false),
                ProjectTask("task-5", "Deploy EC2 Launch Template with Auto Scaling Group", "Bootstrap web app with User Data and attach to ALB Target Group", false)
            ),
            codePreview = """
                # Terraform / IaC Snippet for 3-Tier ALB & ASG
                resource "aws_lb" "app_alb" {
                  name               = "prod-app-alb"
                  internal           = false
                  load_balancer_type = "application"
                  security_groups    = [aws_security_group.alb_sg.id]
                  subnets            = [aws_subnet.public_1.id, aws_subnet.public_2.id]
                }
            """.trimIndent(),
            architectureNotes = "This canonical architecture provides complete resilience against datacenter failures while keeping backend instances and databases fully isolated from direct internet threats.",
            badgeName = "3-Tier Cloud Architect"
        ),
        CloudProject(
            id = "proj-02",
            title = "Serverless Event-Driven Image Processing Pipeline",
            level = "Intermediate",
            estimatedHours = "4-5 hours",
            objective = "Build a fully serverless, event-driven image thumbnail generation and AI object detection pipeline using S3, Lambda, EventBridge, and DynamoDB.",
            architectureFlow = "Client -> S3 Upload -> S3 Event -> EventBridge -> Lambda Resizer -> S3 Clean Bucket & DynamoDB Metadata",
            tasks = listOf(
                ProjectTask("task-1", "Create S3 Source Bucket with EventBridge Notification Enabled", "Configure CORS and presigned URL generation", false),
                ProjectTask("task-2", "Implement Python Lambda Function for Image Resizing using Pillow", "Package dependencies as a Lambda Layer", false),
                ProjectTask("task-3", "Create DynamoDB Table with Pay-Per-Request Billing", "Set PK = image_id, SK = uploaded_at timestamp", false),
                ProjectTask("task-4", "Configure Dead Letter Queue (SQS) for Failed Invocations", "Ensure alert fires to SNS if DLQ has > 0 messages", false)
            ),
            codePreview = """
                import json, boto3
                s3 = boto3.client('s3')
                dynamodb = boto3.resource('dynamodb')
                
                def lambda_handler(event, context):
                    record = event['Records'][0]
                    bucket = record['s3']['bucket']['name']
                    key = record['s3']['object']['key']
                    # Resize logic & record metadata in DynamoDB
                    return {'status': 'success', 'file': key}
            """.trimIndent(),
            architectureNotes = "Serverless event-driven architecture scales to zero when idle, costing literally \$0 during low traffic periods.",
            badgeName = "Serverless Master"
        )
    )

    val interviewQuestions: List<InterviewQuestion> = listOf(
        InterviewQuestion(
            id = "iq-01",
            topic = "EC2 & Load Balancing",
            difficulty = "Intermediate",
            question = "Explain the difference between an Application Load Balancer (ALB) and a Network Load Balancer (NLB). When would you pick one over the other?",
            keyTalkingPoints = listOf(
                "ALB operates at Layer 7 (Application - HTTP/HTTPS/gRPC); understands URLs, paths, and host headers",
                "NLB operates at Layer 4 (Transport - TCP/UDP/TLS); handles millions of requests/sec with ultra-low latency",
                "NLB supports static/elastic IP addresses; ALB uses dynamic IPs with DNS CNAME",
                "Pick ALB for microservices and path-based routing; pick NLB for gaming, financial tickers, or static IP requirements"
            ),
            sampleAnswer = "An Application Load Balancer operates at Layer 7 of the OSI model. It is content-aware, which allows smart routing rules based on URL path (e.g., /api vs /images), query parameters, or HTTP headers. It terminates HTTP/HTTPS traffic and supports WebSockets.\n\nOn the other hand, a Network Load Balancer operates at Layer 4 (Transport level). It does not inspect the HTTP payload, but can process millions of requests per second with microsecond latency. It can also provide a fixed, static Elastic IP address per Availability Zone, which is essential for whitelisting firewalls. I would choose ALB for modern web apps and REST APIs, and NLB for extreme low-latency TCP sockets, non-HTTP protocols, or gaming servers.",
            architecturalInsight = "In real-world setups, companies often place an NLB in front of an ALB when they need both static IP whitelisting and advanced Layer 7 path routing."
        ),
        InterviewQuestion(
            id = "iq-02",
            topic = "Security & IAM",
            difficulty = "Advanced",
            question = "How does an EC2 instance obtain credentials to read from an S3 bucket securely without storing API keys in code or on disk?",
            keyTalkingPoints = listOf(
                "Attach an IAM Role via an Instance Profile",
                "AWS STS automatically issues temporary rotating credentials",
                "Instance retrieves credentials via Instance Metadata Service (IMDSv2 at 169.254.169.254)",
                "AWS SDK automatically detects and refreshes these credentials transparently"
            ),
            sampleAnswer = "You never store hardcoded AWS credentials in configuration files or environment variables on an EC2 instance. Instead, you create an IAM Role with an attached policy granting 's3:GetObject' on the specific bucket ARN. You then assign this IAM Role to the EC2 instance via an Instance Profile.\n\nUnder the hood, the instance queries the AWS Instance Metadata Service (IMDSv2 at 169.254.169.254). The AWS Security Token Service (STS) automatically provisions temporary security credentials (access key, secret key, and session token) that rotate periodically. When you use the AWS SDK (e.g. Boto3, AWS SDK for Java), the SDK automatically retrieves and refreshes these temporary credentials with zero code changes.",
            architecturalInsight = "Always enforce IMDSv2 (requiring a session token via HTTP PUT) to prevent SSRF vulnerabilities from leaking IAM credentials."
        )
    )

    val flashcards: List<Flashcard> = listOf(
        Flashcard("fc-01", "What is an Availability Zone (AZ)?", "One or more discrete datacenters with redundant power, networking, and cooling within an AWS Region, connected via low-latency fiber.", "Fundamentals"),
        Flashcard("fc-02", "What is the key difference between Security Groups and NACLs?", "Security Groups are stateful (instance level); Network ACLs (NACLs) are stateless (subnet level).", "Networking"),
        Flashcard("fc-03", "What is S3's durability rating?", "99.999999999% (11 9's) across multiple Availability Zones.", "Storage"),
        Flashcard("fc-04", "What is the maximum execution time of an AWS Lambda function?", "15 minutes (900 seconds).", "Serverless"),
        Flashcard("fc-05", "What is an AWS IAM Role?", "An IAM identity with specific permissions that can be assumed temporarily by services (EC2, Lambda) or federated users, using short-lived rotating credentials.", "Security")
    )

    val cheatSheets: List<CheatSheet> = listOf(
        CheatSheet(
            id = "cs-cli",
            title = "AWS CLI Essential Cheat Sheet",
            category = "Developer Tools",
            quickReference = listOf(
                "Configure Credentials" to "aws configure",
                "Get Caller Identity" to "aws sts get-caller-identity",
                "List Active Regions" to "aws account list-regions"
            ),
            commonCommands = listOf(
                "List S3 Buckets" to "aws s3 ls",
                "Copy to S3" to "aws s3 cp file.txt s3://bucket-name/",
                "Sync Folder to S3" to "aws s3 sync ./dist s3://my-bucket --delete",
                "Describe Running EC2" to "aws ec2 describe-instances --filters 'Name=instance-state-name,Values=running'",
                "List IAM Users" to "aws iam list-users"
            ),
            gotchas = listOf(
                "Always pass --region if you want to query a region other than your default configured region",
                "Use --query and --output text for powerful JMESPath parsing in bash scripts"
            )
        ),
        CheatSheet(
            id = "cs-vpc",
            title = "VPC & Subnet Math Cheat Sheet",
            category = "Networking",
            quickReference = listOf(
                "/16 Subnet Size" to "65,536 Total IPs (Ideal for whole VPC: 10.0.0.0/16)",
                "/24 Subnet Size" to "256 Total IPs (251 usable by you in AWS)",
                "5 Reserved AWS IPs" to ".0 (Net), .1 (Router), .2 (DNS), .3 (Future), .255 (Bcast)"
            ),
            commonCommands = listOf(
                "Inspect Route Tables" to "aws ec2 describe-route-tables",
                "List NAT Gateways" to "aws ec2 describe-nat-gateways"
            ),
            gotchas = listOf(
                "You cannot change the primary CIDR block of a VPC after creation",
                "A NAT Gateway must live in a PUBLIC subnet to access the internet via an IGW"
            )
        )
    )

    val creatorName = "Awiskar Acharya"
    val creatorLinkedIn = "https://www.linkedin.com/in/awiskaracharya/"
    val appTagline = "Learn AWS. Build Cloud Systems. Deploy the Future."
}
