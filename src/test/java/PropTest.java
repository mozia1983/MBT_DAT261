
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.quicktheories.WithQuickTheories;
import org.testng.annotations.Test;
import java.text.Format;
import java.text.SimpleDateFormat;
import org.quicktheories.core.Gen;

import java.util.ArrayList;
import java.util.Date;

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
    @Test
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

    // A test for adding a session
    @Test
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

    // A test for surfing through tabs
    @Test
    public void surfingThroughTabsDoesNotChangeCoursesView() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mozia\\Desktop\\Trial\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        SeleniumI seleniumI = new SeleniumI();
        seleniumI.login(driver);

        // created just to be able to add three courses
        seleniumI.goToTab("a.nav.courses", driver);
        seleniumI.addCourse("MOMO", "ZIA", driver);
        seleniumI.addCourse("KE", "JIA", driver);
        seleniumI.addCourse("MOH", "HASS", driver);


        qt()
                .forAll(integers().between(0,300))
                .check( i -> seleniumI.checkForCourseAfterSurfing(i, 3, driver));
    }

    // A test for surfing through sessions
    @Test
    public void surfingThroughTabsDoesNotChangeSessionsView() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mozia\\Desktop\\Trial\\chromedriver.exe");
        SeleniumI seleniumI = new SeleniumI();
        WebDriver driver = new ChromeDriver();
        seleniumI.login(driver);

        // created just to be able to add three sessions
        seleniumI.goToTab("a.nav.courses", driver);
        seleniumI.addCourse("MOMO", "ZIA", driver);

        seleniumI.goToTab("a.nav.evaluations", driver);
        seleniumI.addOneSession("Hello","Wed, 18 Jul, 2018",driver);
        seleniumI.addOneSession("Hi","Wed, 18 Jul, 2018",driver);
        seleniumI.addOneSession("Hola","Wed, 18 Jul, 2018",driver);

        qt()
                .forAll(integers().between(0,50))
                .check( i -> seleniumI.checkForSessionsAfterSurfing(i, 3, driver));

    }

}

