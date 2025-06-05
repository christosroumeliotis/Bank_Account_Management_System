package bank.accounts.springboot.Services;

import bank.accounts.springboot.Config.EmailDetails;
import bank.accounts.springboot.Entities.BankAccount;
import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Interfaces.EmailService;
import bank.accounts.springboot.Repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ExtraFunctionsService implements EmailService {

    private final JdbcTemplate jdbcTemplate;
    @Autowired UserService userService;
    @Autowired private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;


    public ExtraFunctionsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    @Override
    public String sendEmailWithAttachments(EmailDetails emailDetails, String[] recipients) {

        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(recipients);
            mimeMessageHelper.setText(emailDetails.getMsgBody());
            mimeMessageHelper.setSubject(
                    emailDetails.getSubject());

            if(emailDetails.getAttachment() != null){

                FileSystemResource file
                        = new FileSystemResource(
                        new File(emailDetails.getAttachment()));

                mimeMessageHelper.addAttachment(
                        file.getFilename(), file);
            }

            javaMailSender.send(mimeMessage);
            return "E-mails sent successfully";
        }
        catch (Exception e) {
            return "Error while sending mail.";
        }
    }

    public List<String> getRecipientsByAction(String action){
        String sql = "Select email from recipients where action = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{action}, String.class);
    }
}
