package org.getscol.gscol.feature.legal.presentation

data class LegalDocumentContent(
    val title: String,
    val body: String,
)

object LegalContent {
    fun forType(isTermsAndConditions: Boolean): LegalDocumentContent = when (isTermsAndConditions) {
        true -> termsOfService()
        false -> privacyPolicy()
    }

    private fun termsOfService() = LegalDocumentContent(
        title = "Terms of Service",
        body = """
            Last updated: June 2026

            Welcome to SCOL. These Terms of Service ("Terms") govern your access to and use of the SCOL mobile application and related services (the "Service"). By creating an account or using the Service, you agree to these Terms.

            1. About SCOL
            SCOL helps students discover study-abroad courses, check eligibility, manage applications, and connect with support consultants. Course listings, eligibility results, and application status are provided for information and convenience; final admission decisions are made by universities and partner institutions.

            2. Eligibility
            You must be at least 18 years old, or the age of majority in your jurisdiction, to register. You agree that the information you provide during registration and in your academic profile is accurate, complete, and kept up to date.

            3. Your account
            You are responsible for maintaining the confidentiality of your login credentials and for all activity under your account. Notify us promptly if you suspect unauthorized access. We may suspend or terminate accounts that violate these Terms or applicable law.

            4. Acceptable use
            You agree not to:
            • Misrepresent your identity, academic history, or application documents
            • Upload false, misleading, or infringing content
            • Attempt to disrupt, reverse engineer, or scrape the Service
            • Use the Service for unlawful purposes

            5. Applications and documents
            When you submit applications or upload documents (such as transcripts or identification), you confirm you have the right to share that material and that it is accurate. SCOL may transmit your data to universities, partners, and service providers involved in processing your application.

            6. Intellectual property
            SCOL and its licensors own the Service, including software, branding, and content we provide. You receive a limited, non-exclusive license to use the Service for personal, non-commercial purposes.

            7. Disclaimers
            The Service is provided "as is." We do not guarantee admission to any institution, accuracy of third-party course data, or uninterrupted availability. To the fullest extent permitted by law, SCOL disclaims warranties of merchantability, fitness for a particular purpose, and non-infringement.

            8. Limitation of liability
            To the maximum extent permitted by law, SCOL and its affiliates are not liable for indirect, incidental, special, or consequential damages arising from your use of the Service. Our total liability for any claim related to the Service is limited to the amount you paid us in the twelve months before the claim, or zero if the Service is free.

            9. Changes
            We may update these Terms from time to time. Continued use of the Service after changes become effective constitutes acceptance of the revised Terms.

            10. Contact
            For questions about these Terms, contact SCOL support through the in-app Support Center or your usual SCOL support channel.
        """.trimIndent(),
    )

    private fun privacyPolicy() = LegalDocumentContent(
        title = "Privacy Policy",
        body = """
            Last updated: June 2026

            This Privacy Policy explains how SCOL ("we", "us") collects, uses, and protects personal information when you use the SCOL mobile application and related services (the "Service").

            1. Information we collect
            • Account information: name, phone number, and password (stored securely; we do not display your full password)
            • Profile and academic information: preferences, test scores, and details you submit in the academic form
            • Application data: course selections, application status, and documents you upload (e.g. PDFs)
            • Usage data: app interactions needed to operate and improve the Service
            • Device and technical data: basic diagnostics required for security and performance

            2. How we use information
            We use your information to:
            • Create and manage your account
            • Show eligible courses and process applications
            • Communicate with you about verification, applications, and support
            • Improve the Service and prevent fraud or abuse
            • Comply with legal obligations

            3. Sharing
            We may share information with:
            • Universities and partners involved in your applications
            • Service providers that host data, deliver messages, or support operations (under contractual safeguards)
            • Authorities when required by law

            We do not sell your personal information.

            4. Data retention
            We retain information while your account is active and as needed to fulfill applications, resolve disputes, and meet legal requirements. You may request deletion of your account subject to applicable law and ongoing application obligations.

            5. Security
            We use reasonable technical and organizational measures to protect your data. No method of transmission or storage is completely secure; please use a strong password and keep your device secure.

            6. Your choices
            You can update profile information in the app. You may contact support to access, correct, or request deletion of your data where applicable law allows.

            7. Children
            The Service is not directed to children under 13 (or the minimum age required in your region). We do not knowingly collect data from children.

            8. International transfers
            If you apply to institutions in other countries, your information may be processed in those jurisdictions.

            9. Changes
            We may update this Privacy Policy. We will post the revised policy in the app and update the "Last updated" date.

            10. Contact
            For privacy questions, contact SCOL support through the in-app Support Center or your usual SCOL support channel.
        """.trimIndent(),
    )
}
