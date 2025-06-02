package bank.accounts.springboot.Controllers;

import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Services.ExtraFunctionsService;
import bank.accounts.springboot.Services.UserService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/do")
public class ExtraFuncController {


    @Autowired
    UserService userService;

    @Autowired
    ExtraFunctionsService extraFunctionsService;

    @GetMapping("/exportCSV")
    public void exportCSV(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        String filename = "All-Users.csv";
        String path = "C:/CSV_Export/";

        response.setContentType("users/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        Files.createDirectories(Paths.get(path));
        Writer writer = new FileWriter(path+filename);
        StatefulBeanToCsv<User> writerToCSV = new StatefulBeanToCsvBuilder<User>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();
        writerToCSV.write(userService.getAllUsers());
    }

    @GetMapping("/excel")   //we will use apache poi
    public void exportExcel(HttpServletResponse response, @RequestParam String filepath) throws IOException{

        response.setContentType("users/excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filepath + "\"");

        List<User> users = userService.getAllUsers();
        List<String> columns = new ArrayList<>();
        columns.add("ID");
        columns.add("USERNAME");
        columns.add("ROLE");

        extraFunctionsService.createExcel("users_excel",users,filepath,columns);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Excel written successfully, path: "+filepath+"users_excel.xlsx");
        response.getWriter().flush();
    }


        //TODO Send the CSV with email
    //TODO Use Sheduled to send every week
}
