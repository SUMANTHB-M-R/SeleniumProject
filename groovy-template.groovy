import java.io.File

def workspace = "${env.WORKSPACE}"
def reportPath = "${workspace}/test-output/emailable-report.html"

// Normalize path for Windows
reportPath = reportPath.replace("/", "\\")

println "🔍 Checking for report at: ${reportPath}"

// Check if file exists before reading
def reportFile = new File(reportPath)
if (!reportFile.exists()) {
    println "❌ Error: Report file not found at ${reportPath}"
    return "❌ Report file not found! Ensure TestNG is generating reports."
}

// Read file content
def reportContent = reportFile.text
def summarySection = (reportContent =~ /<table.*?>.*?<\/table>/s)

return summarySection ? summarySection[0] : "⚠️ Test Summary Not Found!"
