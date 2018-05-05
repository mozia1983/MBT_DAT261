import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

}
