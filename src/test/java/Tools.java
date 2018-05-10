import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class Tools {


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


    public void doneEditing(WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button_done_editing")));
        JavascriptExecutor jse = (JavascriptExecutor) chromeDriver;
        jse.executeScript("scroll(0, 4800)");
        chromeDriver.findElement(By.id("button_done_editing")).click();
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

}
