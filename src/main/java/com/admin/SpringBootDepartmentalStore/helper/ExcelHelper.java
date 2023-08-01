package com.admin.SpringBootDepartmentalStore.helper;

import com.admin.SpringBootDepartmentalStore.bean.ProductInventory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    //check if the file is an Excel file or not
    public static final boolean checkFormat(final MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        }
        return false;

    }


    //to convert Excel file to a list of products

    public static List<ProductInventory> convertExcelToList(final InputStream is) {
        List<ProductInventory> list = new ArrayList<>();

        try {


            XSSFWorkbook workbook = new XSSFWorkbook(is);

            XSSFSheet sheet = workbook.getSheet("data");

            int rowNumber = 0;
            Iterator<Row> i = sheet.iterator();

            while (i.hasNext()) {
                Row row = i.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();

                int cellId = 0;

                ProductInventory p = new ProductInventory();

                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cellId) {
                        case 0:
                            p.setProductId((long) cell.getNumericCellValue());
                            break;
                        case 1:
                            p.setProductDesc(cell.getStringCellValue());
                            break;
                        case 2:
                            p.setProductName(cell.getStringCellValue());
                            break;
                        case 3:
                            p.setPrice(cell.getNumericCellValue());
                            break;
                        case 4:
                            p.setQuantity((int) cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellId++;

                }

                list.add(p);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
}
