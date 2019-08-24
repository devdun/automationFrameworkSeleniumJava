package utils.listners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;
import org.testng.xml.XmlSuite;
import utils.config.CreateDirectories;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Devdun.k on 6/4/2018.
 */
public class ExtentTestNGIReporterListener implements IReporter {
    private static final String OUTPUT_FOLDER = System.getProperty("user.dir")+"/test-output/MyReports/";
    private static final String FILE_NAME = "ExtentReport_"+System.currentTimeMillis()+".html";
    private ExtentReports extent;
    CreateDirectories createDir;
    Date date = Calendar.getInstance().getTime();

    @Override
    public void generateReport(List <XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        init();

        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                buildTestNodes(context.getFailedTests(), Status.FAIL);
                buildTestNodes(context.getSkippedTests(), Status.SKIP);
                buildTestNodes(context.getPassedTests(), Status.PASS);
            }
        }
        for (String s : Reporter.getOutput()) {
            extent.setTestRunnerOutput(s);
        }
        extent.flush();
    }

    private void init() {
        DateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
        String today = formatter.format(date);
        createDir = new CreateDirectories();
        createDir.createFolderWithSpecificName(OUTPUT_FOLDER + today);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + today +"/"+ FILE_NAME);
        htmlReporter.config().setDocumentTitle("Test Report");
        htmlReporter.config().setReportName("Regression Report");
        htmlReporter.config().setTheme(Theme.DARK);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
        extent.setSystemInfo("Environment", "Automation Testing");
        extent.setSystemInfo("User Name", "Devdun Kariyawasam");
    }

    private void buildTestNodes(IResultMap tests, Status status) {
        ExtentTest test;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extent.createTest(result.getMethod().getMethodName());
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);
                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());                }
                else {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }
                test.getModel().setStartTime(getTime(result.getStartMillis()));
                test.getModel().setEndTime(getTime(result.getEndMillis()));
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
