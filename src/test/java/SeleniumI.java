import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class SeleniumI {

    public void login(WebDriver chromeDriver){
        chromeDriver.get("http://teammatesv4.appspot.com/");
        chromeDriver.findElement(By.id("btnInstructorLogin")).click();
        chromeDriver.findElement(By.id("identifierId")).sendKeys("dat261mbt@gmail.com");
        chromeDriver.findElement(By.id("identifierNext")).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
        chromeDriver.findElement(By.name("password")).sendKeys("helloMBT2018");
        chromeDriver.findElement(By.id("passwordNext")).click();
    }

<<<<<<< HEAD
=======
    // Go to any Tab in the webapp
    public void goToTab(String tabCss, WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(tabCss)));
        chromeDriver.findElement(By.cssSelector(tabCss)).click();
    }
    // Add a course by Name and Id
    // A course ID can contain letters, numbers, fullstops, hyphens, underscores, and dollar signs.
    // It cannot be longer than 40 characters, cannot be empty and cannot contain spaces

    // A course name must start with an alphanumeric character,
    // and cannot contain any vertical bar (|) or percent sign (%)
    public void addCourse(String courseID, String courseName, WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("courseid")));
        chromeDriver.findElement(By.name("courseid")).sendKeys(courseID);
        chromeDriver.findElement(By.name("coursename")).sendKeys(courseName);
        chromeDriver.findElement(By.id("btnAddCourse")).click();

    }
    //Todo
    // deleteCourse first course element in the list for the moment
    public  void deleteCourse(WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("courseid")));

        JavascriptExecutor jse = (JavascriptExecutor) chromeDriver;
        jse.executeScript("scroll(0, 350)"); // if the element is on bottom.

        // Delete button
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tableActiveCourses")));
        List<WebElement> rows =  chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
        WebElement element = rows.get(0).findElement(By.xpath("td[8]/a[5]"));
        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(element);
        actions.click(element);
        actions.build().perform();
        // Ok button
        try {
            Thread.sleep(500);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div/div/div[3]/button[2]")));
        } catch (InterruptedException e) {
            actions.build().perform();
            e.printStackTrace();
        }
        chromeDriver.findElement(By.xpath("/html/body/div[4]/div/div/div[3]/button[2]")).click();
    }
    // get the number of courses
    public int getCoursesCount(WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("th.align-center.no-print")));
        ArrayList<String> data = new ArrayList<>();
        List<WebElement> rows =  chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
        for (WebElement row : rows) {
            WebElement dataElement = row.findElement(By.xpath("td[position()=1]"));
            data.add(dataElement.getText().toString());
        }
        if (data.get(0).isEmpty()){return 0;}
        else return data.size();
    }
//*

    // get the number of sessions
    public int getSessionsCount(WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("table-sessions")));
        return chromeDriver.findElements(By.xpath("//table[@class='table-responsive table table-striped table-bordered']/tbody/tr")).size();
    }

    // improvement to be done : entering which session i want to copy from which course, and if already the name exist.
    // if we can get the warnings when we do similar acts.
    public  void addSessionByCopy(String sessionName, WebDriver chromeDriver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button_copy")));
        WebElement element = chromeDriver.findElement(By.id("button_copy"));
        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(element);
        actions.click(element);
        actions.build().perform();
        //chromeDriver.findElement(By.id("button_copy")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modalCopiedSessionName")));
        chromeDriver.findElement(By.id("modalCopiedSessionName")).sendKeys(sessionName);
        Thread.sleep(2000);

        /*
        element = chromeDriver.findElement(By.id("modalCopiedSessionName"));
        //chromeDriver.findElement(By.id("modalCopiedSessionName")).sendKeys(sessionName);
        actions.moveToElement(element);
        actions.sendKeys(sessionName);
        actions.s;
        */
        List<WebElement> rows =  chromeDriver.findElements(By.xpath("//table[@id='copyTableModal']/tbody/tr"));
        WebElement dataElement = rows.get(0).findElement(By.xpath("td[1]/input"));
        Actions actions1 = new Actions(chromeDriver);
        actions1.moveToElement(dataElement);
        actions1.click(dataElement);
        actions1.build().perform();

        chromeDriver.findElement(By.id("button_copy_submit")).click();
        Thread.sleep(1000);
        //"(//input[@type='radio'])[1]"
    }

    public  void addNewSession(String sessionName,String date, WebDriver chromeDriver) {
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fsname")));
        chromeDriver.findElement(By.id("fsname")).sendKeys(sessionName);
        chromeDriver.findElement(By.id("enddate")).sendKeys(date);
        chromeDriver.findElement(By.id("fsname")).click();
        JavascriptExecutor jse = (JavascriptExecutor) chromeDriver;
        jse.executeScript("scroll(0, 250)"); // if the element is on bottom.
        chromeDriver.findElement(By.id("button_submit")).click();
    }

    public void doneEditing(WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button_done_editing")));
        JavascriptExecutor jse = (JavascriptExecutor) chromeDriver;
        jse.executeScript("scroll(0, 4800)");
        chromeDriver.findElement(By.id("button_done_editing")).click();
    }

    public void sortColumn(String columnId, WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(columnId)));
        WebElement element = chromeDriver.findElement(By.id(columnId));
        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(element);
        actions.click(element);
        actions.build().perform();
        //chromeDriver.findElement(By.id(columnId)).sendKeys(Keys.RETURN);
    }

    public ArrayList<String> extractColumMembers(String tableId, int position, WebDriver chromeDriver){
        ArrayList<String> data = new ArrayList<>();
        List<WebElement> rows =  chromeDriver.findElements(By.xpath("//table[@id='"+tableId+"']/tbody/tr"));
        for (WebElement row : rows) {
            WebElement dataElement = row.findElement(By.xpath("td[position()=" + Integer.toString(position)+"]"));
            data.add(dataElement.getText().toString());
        }
        return data;
    }
>>>>>>> f3552c4919ee61bd92f9e5144829554cc9e187ec

}
