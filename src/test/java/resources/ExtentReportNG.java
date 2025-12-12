package resources;



import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class  ExtentReportNG {
	
	public static ExtentReports getExtendReportObject() {
		String path = System.getProperty("user.dir")+"//reports//index.html";
		  ExtentSparkReporter sparkReporter = new ExtentSparkReporter(path);
		  sparkReporter.config().setDocumentTitle("Ecommerce Test");
		  sparkReporter.config().setReportName("Ecommerce Report Name");
		  ExtentReports extent = new ExtentReports();
		  extent.attachReporter(sparkReporter);
		  extent.setSystemInfo("Tester", "Basharmal Safi");
		  return extent;
	}

}