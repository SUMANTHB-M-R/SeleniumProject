import java.io.File

// Use System.getenv() to fetch environment variables
def workspace = System.getenv("WORKSPACE")
if (!workspace) {
    println "❌ Error: WORKSPACE environment variable is not set."
    return "❌ Error: WORKSPACE not found! Ensure Jenkins is setting the WORKSPACE variable."
}

def reportPath = workspace + "/test-output/emailable-report.html"
reportPath = reportPath.replace("/", "\\") // Normalize path for Windows

println "🚀 Debug: Script execution started!"
println "🔍 Debug: Checking workspace environment..."
println "📝 Debug: Preparing to locate the report file..."
println "📂 Debug: Jenkins Workspace -> ${workspace}"
println "📄 Debug: Expected Report Path -> ${reportPath}"

// Check if the file exists
def reportFile = new File(reportPath)
if (!reportFile.exists()) {
    println "❌ Error: Report file not found at ${reportPath}"
    return "❌ Error: emailable-report.html not found! Make sure TestNG is generating the report."
}

// ✅ Only read the file if it exists
println "📑 Debug: Report file found! Reading content..."
def reportContent = reportFile.text

// Extract summary section
def summarySection = (reportContent =~ /(?s)<table.*?>.*?<\\/table>/)

println "✅ Debug: Script execution completed!"
return summarySection ? summarySection[0] : "⚠️ Test Summary Not Found!"
