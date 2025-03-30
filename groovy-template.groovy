import java.io.File

def workspace = "${env.WORKSPACE}"
def reportPath = "${workspace}/test-output/emailable-report.html"
reportPath = reportPath.replace("/", "\\") // Normalize path for Windows

println "🔍 Checking for report at: ${reportPath}"

// Check if the file exists
def reportFile = new File(reportPath)
if (!reportFile.exists()) {
    println "❌ Error: Report file not found at ${reportPath}"
    return "❌ Error: emailable-report.html not found! Make sure TestNG is generating the report."
}

// Read file content
def reportContent = reportFile.text

// Extract summary section
def summarySection = (reportContent =~ /<table.*?>.*?<\/table>/s)

return summarySection ? summarySection[0] : "⚠️ Test Summary Not Found!"
