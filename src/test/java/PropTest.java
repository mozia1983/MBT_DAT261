
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.quicktheories.WithQuickTheories;
import org.testng.annotations.Test;
import java.text.Format;
import java.text.SimpleDateFormat;
import org.quicktheories.core.Gen;

import javax.xml.bind.Element;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Locale.ENGLISH;


public class PropTest implements WithQuickTheories {

    private static ArrayList<String> addedNames = new ArrayList<>();
    private static ArrayList<String> addedIds = new ArrayList<>();

    //to check if the id format respect the specification
    public boolean regMatchId(String id) {
        boolean b = id.matches("([A-Z]|[a-z]|[0-9]|-|_|\\.|\\$)+");
        System.out.print("The generated id: " + id + " Matched: " + b + "\n");
        return b;
    }

    //to check if the name format respect the specification
    public boolean regMatchName(String name) {
        boolean b = name.matches("^([A-Z]|[a-z]|[0-9])([^\\|%]*)");
        System.out.print("The generated name: " + name + " Matched: " + b + "\n");
        return b;
    }

    //to check if the session name format respect the specification
    public boolean regMatchSessionName(String sessionName) {
        boolean b = sessionName.matches("^([A-Z]|[a-z]|[0-9])([^\\|%<>&'/\"]*)");
        if (b) System.out.print("The generated session name: " + sessionName + " Matched: " + b + "\n");
        return b;
    }

    public  Gen<String> genName() {
        return  strings().basicLatinAlphabet().ofLengthBetween(1, 400).assuming(s -> regMatchSessionName(s));
    }

    //to check if the closing date is not in the past
    public boolean verifyDate(Date closingDate){
        Format formatter = new SimpleDateFormat("EEE, dd MMM, yyyy",ENGLISH);
        Date today = new Date();
        String actual = formatter.format(today);
        String closing = formatter.format(closingDate);
        if (closingDate.after(today)) System.out.println("Closing : " + closing +" Today : "+ actual+"\n");
        return closingDate.after(today);
    }


    // A test for adding a course
    //@Test
    public void respectedCourseFormatIsAdded() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mozia\\Desktop\\Trial\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        SeleniumI seleniumI = new SeleniumI();
        seleniumI.login(driver);
        seleniumI.goToTab("a.nav.courses", driver);
        // created just to be able to call a method from Course clas
        Course course = new Course("hello", "hi");
        // the test is aborting almost of the times because the generators are almost of the time rejected because they don't match the regex
        // we must investigate how to generate Strings with less characters then LatinAlphabet.
        qt()
                .forAll(strings().basicLatinAlphabet().ofLength(4), strings().basicLatinAlphabet().ofLength(4))
                .assuming((id, name) -> regMatchId(id)
                        && regMatchName(name)
                        && course.addToCurrentAndAssess(id, name, addedNames, addedIds)) //we check that the course was not added before
                .check((id, name) -> seleniumI.addCourseAndCheckIfAdded(id, name, driver));

        seleniumI.deleteAllCourses(driver);
    }


    public Gen<String> genStudent() {
        return genName().zip(genName(),genName(),genName(), (a,b,c,d) -> {
            return a + "    " + b + "   " + c + "   " + d ;
        }) ;
    }

    public  Gen<List<String>> genStudentList() {
        return lists().of(genStudent()).ofSizeBetween(1, 100);
    }

    public  void logIn() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mozia\\Desktop\\Trial\\chromedriver.exe");
        SeleniumI seleniumI = new SeleniumI();
        WebDriver driver = new ChromeDriver();
        seleniumI.login(driver);
    }

    @Test
    public void testEnrollStudent() {

        final String header = "Section\tTeam\tName\tEmail\tComments";
        final String enrollButtonPath = "//*[@id=\"tableActiveCourses\"]/tbody/tr/td[8]/a[1]";
        final String studentsInputFieldPath = "//*[@id=\"enrollstudents\"]";


        System.setProperty("webdriver.chrome.driver", "/home/moh/tmp/chromedriver");
        SeleniumI seleniumI = new SeleniumI();
        WebDriver driver = new ChromeDriver();
        seleniumI.login(driver);




        qt().withGenerateAttempts(200000).forAll(genStudentList())
                .check(list -> {
            final String students = list.stream().reduce(header, (a, b) -> a + "\n" + b);

            seleniumI.goToTab("a.nav.courses", driver);

            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(enrollButtonPath)));

                    JavascriptExecutor jse = (JavascriptExecutor) driver;
                    jse.executeScript("scroll(0, 250)"); // if the element is on bottom.

            driver.findElement(By.xpath(enrollButtonPath)).click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(studentsInputFieldPath)));

                    jse.executeScript("scroll(0, 250)");

                    System.out.println(students.toString());
            driver.findElement(By.xpath(studentsInputFieldPath)).sendKeys(header);
            return  true;
            /*
            driver.findElement(By.xpath("//*[@id=\"button_enroll\"]"));

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"mainContent\"]/div[2]/table")));


            //*[@id="mainContent"]/div[2]/table/tbody/tr[2]/td[1]
            List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"mainContent\"]/div[2]/table/tbody/tr"));
            // We extract the ID first
            //WebElement dataElement = rows.get(0).findElement(By.xpath("td[position()=2]"));
            //String str = (dataElement.getText().toString());




            return  list.stream().map(strg -> {
                final String email = strg.split("   ")[3];
                return rows.stream().map( row -> {
                    return row.findElement(By.xpath("td[position()=4]")).getText().equals(email);
                }).reduce((a, b) -> a || b);
            }).reduce((a, b) -> java.util.Optional.of(a.get().booleanValue() || b.get().booleanValue())).get().get().booleanValue();
            */
        });

    }



    // A test for adding a session
    //@Test
    public void respectedSessionFormatIsAdded() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mozia\\Desktop\\Trial\\chromedriver.exe");
        SeleniumI seleniumI = new SeleniumI();
        WebDriver driver = new ChromeDriver();
        seleniumI.login(driver);

        // created just to be able to add sessions
        seleniumI.goToTab("a.nav.courses", driver);
        seleniumI.addCourse("trial", "trial", driver);

        seleniumI.goToTab("a.nav.evaluations", driver);
        Format formatter = new SimpleDateFormat("EEE, dd MMM, yyyy",ENGLISH);

        // actual time in milliseconds
        long millis = System.currentTimeMillis();

        qt()
                .forAll(strings().basicLatinAlphabet().ofLength(4),
                        dates().withMillisecondsBetween(millis,1904578261000L))
                .assuming((sessionName, date) -> regMatchSessionName(sessionName)
                                                    && verifyDate(date))
                .check((sessionName, date) -> seleniumI.addSessionAndCheckIfAdded(sessionName, formatter.format(date), driver));

    }

}

