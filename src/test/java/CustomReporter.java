import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class CustomReporter implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try {
            File reportFile = new File(outputDirectory + "/custom-report.html");
            FileWriter writer = new FileWriter(reportFile);

            ISuite suite = suites.get(0);
            ITestContext testContext = suite.getResults().values().iterator().next().getTestContext();
            int passedCount = testContext.getPassedTests().size();
            int skippedCount = testContext.getSkippedTests().size();
            int failedCount = testContext.getFailedTests().size();

            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Passed", passedCount);
            dataset.setValue("Skipped", skippedCount);
            dataset.setValue("Failed", failedCount);

            JFreeChart chart = ChartFactory.createPieChart("Test Results Summary", dataset, true, true, false);
            PiePlot plot = (PiePlot) chart.getPlot();

            plot.setSectionPaint("Passed", Color.GREEN);
            plot.setSectionPaint("Skipped", Color.ORANGE);
            plot.setSectionPaint("Failed", Color.RED);

            PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                    "{0} ({2})", new DecimalFormat("0"), new DecimalFormat("0%")
            );
            plot.setLabelGenerator(labelGenerator);
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelBackgroundPaint(new Color(255, 255, 204));
            plot.setLabelOutlinePaint(Color.GRAY);
            plot.setLabelShadowPaint(Color.WHITE);

            plot.setBackgroundPaint(new Color(200, 200, 200));
            plot.setOutlinePaint(Color.GRAY);

            BufferedImage bufferedImage = chart.createBufferedImage(500, 300);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            String encodedImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            // HTML Report (Using Base64)
            writer.write("<!DOCTYPE html>");
            writer.write("<html xmlns=\"https://www.w3.org/1999/xhtml\">");
            writer.write("<head>");
            writer.write("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>");
            writer.write("<title>TestNG Report</title>");
            writer.write("</head>");
            writer.write("<body style=\"font-family: Arial, sans-serif;\">");

            writer.write("<table style=\"width: 100%; max-width: 800px; margin: 20px auto; border-collapse: collapse; border: 2px solid black;\">");
            writer.write("<tr><th style=\"background-color: #f2f2f2; border: 2px solid black; padding: 10px; text-align: left;\">Test</th><th style=\"background-color: #f2f2f2; border: 2px solid black; padding: 10px; text-align: left;\"># Passed</th><th style=\"background-color: #f2f2f2; border: 2px solid black; padding: 10px; text-align: left;\"># Skipped</th><th style=\"background-color: #f2f2f2; border: 2px solid black; padding: 10px; text-align: left;\"># Failed</th><th style=\"background-color: #f2f2f2; border: 2px solid black; padding: 10px; text-align: left;\">Start Time</th><th style=\"background-color: #f2f2f2; border: 2px solid black; padding: 10px; text-align: left;\">End Time</th></tr>");
            Date startDate = testContext.getStartDate();
            Date endDate = testContext.getEndDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = (startDate != null) ? dateFormat.format(startDate) : "N/A";
            String endTime = (endDate != null) ? dateFormat.format(endDate) : "N/A";
            writer.write("<tr><td style=\"border: 2px solid black; padding: 10px; text-align: left;\">" + suite.getName() + "</td><td style=\"border: 2px solid black; padding: 10px; text-align: right; background-color: #e6f7e6; color: #389e0d; font-weight: bold;\">" + passedCount + "</td><td style=\"border: 2px solid black; padding: 10px; text-align: right; background-color: #ffe0b3; color: #d97706; font-weight: bold;\">" + skippedCount + "</td><td style=\"border: 2px solid black; padding: 10px; text-align: right; background-color: #ffe6e6; color: #cf1322; font-weight: bold;\">" + failedCount + "</td><td style=\"border: 2px solid black; padding: 10px; text-align: left;\">" + startTime + "</td><td style=\"border: 2px solid black; padding: 10px; text-align: left;\">" + endTime + "</td></tr>");
            writer.write("</table>");

            writer.write("<div style=\"text-align: center;\"><img src=\"data:image/png;base64," + encodedImage + "\" alt=\"Test Results Pie Chart\" style=\"display: block; margin: 20px auto;\"></div>");

            writer.write("</body>");
            writer.write("</html>");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}