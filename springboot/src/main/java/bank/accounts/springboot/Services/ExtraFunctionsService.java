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

@Service
public class ExtraFunctionsService {

    @Autowired
    UserService userService;

    public void createExcel(String excelName, List<User> entity, String fileLocation, List<String> columnNames) throws IOException {
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

        Row header = sheet.createRow(0);

        for(int i=0 ; i<columnNames.size() ; i++){
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(columnNames.get(i));
            headerCell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        for(int i=1 ; i<=entity.size() ; i++){

            Row row = sheet.createRow(i);

            Cell headerCell = row.createCell(0);
            headerCell.setCellValue(entity.get(i-1).getId());

            headerCell = row.createCell(1);
            headerCell.setCellValue(entity.get(i-1).getUsername());

            headerCell = row.createCell(2);
            headerCell.setCellValue(entity.get(i-1).getRole());
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
