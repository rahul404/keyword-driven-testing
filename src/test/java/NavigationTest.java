import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class NavigationTest {
    @Test
    public void navigate() throws Exception{
        WebDriver webDriver = new ChromeDriver();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File("keyword.xlsx")));
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        Iterator<Row> rowIterator = xssfSheet.rowIterator();
        while (rowIterator.hasNext()){
            Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
            String command = cellIterator.next().getStringCellValue().toLowerCase();
            if (command.equals("goto")){
                String url = cellIterator.next().getStringCellValue();
                webDriver.get(url);
                Assert.assertEquals(webDriver.getCurrentUrl(), url);
                Assert.assertEquals(webDriver.getTitle().toLowerCase(), cellIterator.next().getStringCellValue().toLowerCase());
            }
            else if (command.equals("fill")){
                while (cellIterator.hasNext()){
                    String[] formValue = cellIterator.next().getStringCellValue().split(" ");
                    webDriver.findElement(By.name(formValue[0])).sendKeys(formValue[1]);
                }
            }
            else if (command.equals("click")){
                String findBy = cellIterator.next().getStringCellValue().toLowerCase();
                if (findBy.equals("name")){
                    webDriver.findElement(By.name(cellIterator.next().getStringCellValue())).click();
                    Assert.assertEquals(webDriver.getCurrentUrl(), cellIterator.next().getStringCellValue());
                }
            }
        }
    }
}
