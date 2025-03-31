import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomReporter implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try {
            // Create the HTML report file
            File reportFile = new File(outputDirectory + "/custom-report.html");
            FileWriter writer = new FileWriter(reportFile);

            // Write the HTML header
            writer.write("<!DOCTYPE html>");
            writer.write("<html xmlns=\"https://www.w3.org/1999/xhtml\">");
            writer.write("<head>");
            writer.write("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>");
            writer.write("<title>TestNG Report</title>");
            writer.write("<style type=\"text/css\">");
            writer.write("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show; width: 100%; max-width: 800px; margin: 20px auto; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);}th,td {border:1px solid #ddd;padding:12px;text-align:left;}th {background-color:#f2f2f2;font-weight:bold;}tr:nth-child(even) {background-color:#f9f9f9;}.num {text-align:right;}.passed {background-color: #e6f7e6; color: #389e0d; font-weight: bold;}.failed {background-color: #ffe6e6; color: #cf1322; font-weight: bold;}.skipped {background-color: #ffe0b3; color: #d97706; font-weight: bold;}" +
                    "#piechart {margin: 20px auto; width: 900px; height: 500px;}" +
                    "body{text-align: center;}" +
                    "table{margin: 20px auto;}" +
                    "</style>");
            writer.write("<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"); // Load Google Charts
            writer.write("<script type=\"text/javascript\">");
            writer.write("google.charts.load('current', {'packages':['corechart']});");
            writer.write("google.charts.setOnLoadCallback(drawChart);");
            writer.write("function drawChart() {");

            // Get test results
            ISuite suite = suites.get(0);
            ITestContext testContext = suite.getResults().values().iterator().next().getTestContext();
            int passedCount = testContext.getPassedTests().size();
            int skippedCount = testContext.getSkippedTests().size();
            int failedCount = testContext.getFailedTests().size();

            writer.write("var data = google.visualization.arrayToDataTable([");
            writer.write("['Task', 'Hours per Day'],");
            writer.write("['Passed', " + passedCount + "],");
            writer.write("['Skipped', " + skippedCount + "],");
            writer.write("['Failed', " + failedCount + "]");
            writer.write("]);");

            writer.write("var options = {'title':'Test Results Summary'};");
            writer.write("var chart = new google.visualization.PieChart(document.getElementById('piechart'));");
            writer.write("chart.draw(data, options);");
            writer.write("}");
            writer.write("</script>");
            writer.write("</head>");
            writer.write("<body>");
            //writer.write("<h3>Test Results Summary</h3>");
            // Suite Summary Table
            writer.write("<table>");
            writer.write("<tr><th>Test</th><th># Passed</th><th># Skipped</th><th># Failed</th><th>Start Time</th><th>End Time</th></tr>");

            // Loop through suites and write summary for each test suite
            Date startDate = testContext.getStartDate();
            Date endDate = testContext.getEndDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = (startDate != null) ? dateFormat.format(startDate) : "N/A";
            String endTime = (endDate != null) ? dateFormat.format(endDate) : "N/A";

            writer.write("<tr><td>" + suite.getName() + "</td>");
            writer.write("<td class=\"num passed\">" + passedCount + "</td>");
            writer.write("<td class=\"num skipped\">" + skippedCount + "</td>");
            writer.write("<td class=\"num failed\">" + failedCount + "</td>");
            writer.write("<td>" + startTime + "</td>");
            writer.write("<td>" + endTime + "</td>");
            writer.write("</tr>");

            writer.write("</table>");

            // Pie Chart Div
            writer.write("<div id=\"piechart\"></div>");

            writer.write("</body>");
            writer.write("</html>");

            // Close the file writer
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}