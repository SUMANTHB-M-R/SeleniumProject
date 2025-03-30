import java.io.File

// Get the Jenkins workspace path dynamically
def workspace = "${env.WORKSPACE}"
def reportPath = "${workspace}/test-output/emailable-report.html"

// Normalize path for Windows
reportPath = reportPath.replace("/", "\\")

// Check if the file exists
def reportFile = new File(reportPath)

if (!reportFile.exists()) {
    return "❌ Error: Report file not found at ${reportPath}"
}

// Read file content
def reportContent = reportFile.text

// Extract summary section from the report
def summarySection = (reportContent =~ /<table.*?>.*?<\/table>/s)

// Return extracted summary or default message
return summarySection ? summarySection[0] : "⚠️ Test Summary Not Found!"
