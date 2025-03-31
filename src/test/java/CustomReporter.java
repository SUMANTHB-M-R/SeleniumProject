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
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class CustomReporter implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try {
            // Create the HTML report file
            File reportFile = new File(outputDirectory + "/custom-report.html");
            FileWriter writer = new FileWriter(reportFile);

            // Get test results
            ISuite suite = suites.get(0);
            ITestContext testContext = suite.getResults().values().iterator().next().getTestContext();
            int passedCount = testContext.getPassedTests().size();
            int failedCount = testContext.getFailedTests().size();
            int skippedCount = testContext.getSkippedTests().size();

            // Write HTML header
            writer.write("<!DOCTYPE html>");
            writer.write("<html xmlns=\"https://www.w3.org/1999/xhtml\">");
            writer.write("<head>");
            writer.write("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>");
            writer.write("<title>TestNG Report</title>");
            writer.write("<style type=\"text/css\">");
            writer.write("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show; width: 100%; max-width: 800px; margin: 20px auto; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);}");
            writer.write("th,td {border: 0.9px solid #ddd; padding: 12px; text-align: left;}");
            writer.write("th {background-color: #f2f2f2; font-weight: bold;}");
            writer.write("tr:nth-child(even) {background-color: #f9f9f9;}");
            writer.write(".num {text-align: right;}");
            writer.write(".passed {background-color: #e6f7e6; color: #389e0d; font-weight: bold;}");
            writer.write(".failed {background-color: #ffe6e6; color: #cf1322; font-weight: bold;}");
            writer.write(".skipped {background-color: #ffe0b3; color: #d97706; font-weight: bold;}");
            writer.write("body{text-align: center;}");
            writer.write("table{margin: 20px auto;}");
            writer.write("</style>");
            writer.write("</head>");
            writer.write("<body>");

            // Suite Summary Table
            writer.write("<table style=\"border-collapse: collapse;\">"); // Added inline style to table
            writer.write("<tr>");
            writer.write("<th style=\"border: 0.9px solid #ddd; padding: 12px;\">Test</th>");
            writer.write("<th style=\"border: 0.9px solid #ddd; padding: 12px;\"># Passed</th>");
            writer.write("<th style=\"border: 0.9px solid #ddd; padding: 12px;\"># Skipped</th>");
            writer.write("<th style=\"border: 0.9px solid #ddd; padding: 12px;\"># Failed</th>");
            writer.write("<th style=\"border: 0.9px solid #ddd; padding: 12px;\">Start Time</th>");
            writer.write("<th style=\"border: 0.9px solid #ddd; padding: 12px;\">End Time</th>");
            writer.write("</tr>");

            // Loop through suites and write summary for each test suite
            Date startDate = testContext.getStartDate();
            Date endDate = testContext.getEndDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = (startDate != null) ? dateFormat.format(startDate) : "N/A";
            String endTime = (endDate != null) ? dateFormat.format(endDate) : "N/A";

            writer.write("<tr>");
            writer.write("<td style=\"border: 0.9px solid #ddd; padding: 12px;\">" + suite.getName() + "</td>");
            writer.write("<td style=\"border: 0.9px solid #ddd; padding: 12px;\" class=\"num passed\">" + passedCount + "</td>");
            writer.write("<td style=\"border: 0.9px solid #ddd; padding: 12px;\" class=\"num skipped\">" + skippedCount + "</td>");
            writer.write("<td style=\"border: 0.9px solid #ddd; padding: 12px;\" class=\"num failed\">" + failedCount + "</td>");
            writer.write("<td style=\"border: 0.9px solid #ddd; padding: 12px;\">" + startTime + "</td>");
            writer.write("<td style=\"border: 0.9px solid #ddd; padding: 12px;\">" + endTime + "</td>");
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

            // Encode the image as Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Embed the image in the HTML
            writer.write("<img src=\"data:image/png;base64," + base64Image + "\" alt=\"Test Results Summary Chart\" />");

            writer.write("</body>");
            writer.write("</html>");

            // Close the file writer
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}