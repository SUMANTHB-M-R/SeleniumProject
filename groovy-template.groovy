import java.nio.file.Files
import java.nio.file.Paths

def reportPath = "test-output/emailable-report.html"

// Read the file content
def reportContent = new String(Files.readAllBytes(Paths.get(reportPath)))

// Extract Summary Section
def summarySection = reportContent.find(/<table.*?>.*?<\/table>/)

// Return Extracted Summary or Default Message
return summarySection ?: "Test Summary Not Found!"
