import java.io.File

// Define absolute path to the report file
def workspace = "${JENKINS_HOME}/workspace/SeleniumTestJob"
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

// Extract summary section (ensuring it captures the table)
def summarySection = (reportContent =~ /<table.*?>.*?<\/table>/s)

// Return Extracted Summary or Default Message
return summarySection ? summarySection[0] : "⚠️ Test Summary Not Found in the report!"
