import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;

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
                    "body{text-align: center;}" +
                    "table{margin: 20px auto;}" +
                    "</style>");
            writer.write("</head>");
            writer.write("<body>");

            // Suite Summary Table
            writer.write("<table>");
            writer.write("<tr><th>Test</th><th># Passed</th><th># Skipped</th><th># Failed</th><th>Start Time</th><th>End Time</th></tr>");

            // Get test results
            ISuite suite = suites.get(0);
            ITestContext testContext = suite.getResults().values().iterator().next().getTestContext();
            int passedCount = testContext.getPassedTests().size();
            int skippedCount = testContext.getSkippedTests().size();
            int failedCount = testContext.getFailedTests().size();

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

            // Generate pie chart image using JFreeChart
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Passed", passedCount);
            dataset.setValue("Failed", failedCount);

            JFreeChart chart = ChartFactory.createPieChart(
                    "Test Results Summary",
                    dataset,
                    true, // include legend
                    true, // tooltips
                    false // URLs
            );

            // Customize chart appearance
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setSectionPaint("Passed", new Color(0, 102, 204)); // Darker blue for passed
            plot.setSectionPaint("Failed", new Color(255, 165, 0)); // Orange for failed

            chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 14)); // Smaller title font
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10)); // Smaller label font
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})")); // Show percentage in labels

            BufferedImage image = chart.createBufferedImage(300, 200); // Smaller chart dimensions
            File chartImageFile = new File(outputDirectory + "/test-results-chart.png");
            ImageIO.write(image, "png", chartImageFile);

            // No image embedding in HTML

            writer.write("</body>");
            writer.write("</html>");

            // Close the file writer
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}