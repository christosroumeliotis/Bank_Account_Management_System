package bank.accounts.springboot.Services;

import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Repositories.UserRepository;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ExtraFunctionsService {

    @Autowired
    UserService userService;

    public void createExcel(String excelName, String fileLocation, List<Map<String, Object>> entityInfo) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        //Style
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        //Build the excel
        Sheet sheet = workbook.createSheet(excelName);

        int rows = 0;
        Row header = sheet.createRow(rows++);

        if(entityInfo.get(0) != null){

            Set keys = entityInfo.get(0).keySet();
            int counter = 0;
            for(Object key: keys){
                Cell headerCell = header.createCell(counter++);
                headerCell.setCellValue((String) key);
                headerCell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(counter);
            }
        }else{
            return;
        }

        for(int i=0; i<entityInfo.size(); i++){

            Row row = sheet.createRow(rows++);

            int counter_values = 0;
            for(String j : entityInfo.get(i).keySet()){

                Cell headerCell = row.createCell(counter_values++);
                String value = String.valueOf(entityInfo.get(i).get(j));
                if(value == null){
                    headerCell.setCellValue(" ");
                }else{
                    headerCell.setCellValue(value);
                    sheet.autoSizeColumn(counter_values);
                }
                String cellValue = row.getCell(counter_values)!=null ?
                        row.getCell(counter_values).getStringCellValue() : "";
                sheet.setColumnWidth(0, cellValue.length() * 256);
            }
        }
        try {

            Files.createDirectories(Paths.get(fileLocation));
            FileOutputStream out = new FileOutputStream(fileLocation+excelName+".xlsx");
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully on disk.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
