import java.nio.file.Files
import java.nio.file.Paths

def reportPath = "test-output/emailable-report.html"

// Read the file content
def reportContent = new String(Files.readAllBytes(Paths.get(reportPath)))

// Extract Summary Section (Fixing the Regex)
def summarySection = reportContent =~ /<table.*?>.*?<\/table>/s

// Return Extracted Summary or Default Message
return summarySection ? summarySection[0] : "Test Summary Not Found!"
