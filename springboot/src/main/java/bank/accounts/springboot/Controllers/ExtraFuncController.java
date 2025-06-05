package bank.accounts.springboot.Controllers;

import bank.accounts.springboot.Config.EmailDetails;
import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Repositories.UserRepository;
import bank.accounts.springboot.Services.AccountService;
import bank.accounts.springboot.Services.ExtraFunctionsService;
import bank.accounts.springboot.Services.UserService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/do")
public class ExtraFuncController {


    @Autowired
    UserService userService;

    @Autowired
    ExtraFunctionsService extraFunctionsService;


    //TODO it has problem!!! Blank csv
    @GetMapping("/exportCSV")
    public void exportCSV(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        String filename = "all_users.csv";
        String path = "C:/csv_export/";

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
    public void exportExcel(HttpServletResponse response, @RequestParam String filepath) throws IOException, IntrospectionException {

        response.setContentType("users/excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filepath + "\"");

        List<Map<String, Object>> usersInfo = userService.getUsersInformation();

        extraFunctionsService.createExcel("users_excel",filepath,usersInfo);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Excel written successfully, path: "+filepath+"users_excel.xlsx");
        response.getWriter().flush();
    }

    @GetMapping("/email")   //Use of spring mail
    public String exportExcel(@RequestBody EmailDetails emailDetails){

           List<String> recipients = extraFunctionsService.getRecipientsByAction("/do/email");
           return extraFunctionsService.sendEmailWithAttachments(emailDetails, recipients.toArray(new String[0]));
    }


    //TODO add logs, make the code better and reusable
    //TODO Use Sheduled to send every week
}
