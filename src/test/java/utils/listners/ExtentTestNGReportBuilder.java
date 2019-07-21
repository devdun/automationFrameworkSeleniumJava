package utils.listners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.config.CreateDirectories;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExtentTestNGReportBuilder {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
    private static final String OUTPUT_FOLDER = System.getProperty("user.dir")+"/test-output/MyReports/";
    private static final String FILE_NAME = "ExtentReport_"+System.currentTimeMillis()+".html";
    Date date = Calendar.getInstance().getTime();
    CreateDirectories createDir;

    @BeforeSuite
    @Parameters({"browser","URL","Env"})
    public void beforeSuite(String browser,String URL,String Env) {
        DateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
        String today = formatter.format(date);
        createDir = new CreateDirectories();
        createDir.createFolderWithSpecificName(OUTPUT_FOLDER + today);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + today +"/"+ FILE_NAME);
        htmlReporter.config().setDocumentTitle("My Test Report");
        htmlReporter.config().setReportName("Regression Report");
        htmlReporter.config().setTheme(Theme.DARK);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("BROWSER" , browser);
        extent.setSystemInfo("URL", URL);
        extent.setSystemInfo("ENVIRONMENT" , "ENV");
    }

    @BeforeClass
    public synchronized void beforeClass() {
        ExtentTest parent = extent.createTest(getClass().getName());
        parentTest.set(parent);
    }

    @BeforeMethod
    public synchronized void beforeMethod(Method method) {
        ExtentTest child = parentTest.get().createNode(method.getName());
        test.set(child);
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result) {

        StringBuilder inputArgs = new StringBuilder();
        Object objects[] = result.getParameters();

        for(Object obj : objects){
            if(obj==null){
                inputArgs.append("  ");
            }else{
                inputArgs.append(obj.toString());
            }
            inputArgs.append(" , ");
        }

        if (result.getStatus() == ITestResult.FAILURE)
            test.get().fail(result.getThrowable()+ "Input Parameters : "+inputArgs.toString());
        else if (result.getStatus() == ITestResult.SKIP)
            test.get().skip(result.getThrowable() + "Input Parameters : "+inputArgs.toString());
        else
            test.get().pass( " Test Passed. Input parameters : " +inputArgs.toString());
        extent.flush();
    }
}
