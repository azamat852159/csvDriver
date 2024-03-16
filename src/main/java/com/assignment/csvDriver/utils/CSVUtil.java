package com.assignment.csvDriver.utils;

import com.assignment.csvDriver.model.CSVDAO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    private static final String CODE = "code";
    private static final String SOURCE = "source";
    private static final String DISPLAY_VALUE = "displayValue";
    private static final String FROM_DATE = "fromDate";
    private static final String TO_DATE = "toDate";
    private static final String CODE_LIST_CODE = "codeListCode";
    private static final String LONG_DESCRIPTION = "longDescription";
    private static final String SORTING_PRIORITY = "sortingPriority";

    public static byte[] createCSV(List<CSVDAO> csvdaos) throws IOException {
        StringWriter sw = new StringWriter();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(SOURCE,
                           CODE_LIST_CODE,
                           CODE,
                           DISPLAY_VALUE,
                           LONG_DESCRIPTION,
                           FROM_DATE,
                           TO_DATE,
                           SORTING_PRIORITY)
                .build();

        try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
            csvdaos.forEach(record -> {
                try {
                    printer.printRecord(record.getSource(),
                            record.getCodeListCode(),
                            record.getCode(),
                            record.getDisplayValue(),
                            record.getLongDescription(),
                            record.getFromDate(),
                            record.getToDate(),
                            record.getSortingPriority());
                } catch (IOException e) {
                    throw new RuntimeException("creating CSV file failed");                }
            });
        }

        return sw.toString().getBytes("UTF-8");
    }

    public static List<CSVDAO> parseCSV(InputStream csv) {
        try (BufferedReader csvReader = new BufferedReader(new InputStreamReader(csv, "UTF-8"));
             CSVParser csvParser = new CSVParser(csvReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withIgnoreHeaderCase().withTrim());
        ) {
            List<CSVDAO> csvData = new ArrayList<>();
            csvParser.stream().iterator().forEachRemaining(record -> csvData.add(new CSVDAO()
                    .setCode(record.get(CODE))
                    .setSource(record.get(SOURCE))
                    .setDisplayValue(record.get(DISPLAY_VALUE))
                    .setFromDate(parseDate(record.get(FROM_DATE)))
                    .setToDate(parseDate(record.get(TO_DATE)))
                    .setCodeListCode(record.get(CODE_LIST_CODE))
                    .setLongDescription(record.get(LONG_DESCRIPTION))
                    .setSortingPriority(parseInt(record.get(SORTING_PRIORITY)))));

            return csvData;
        } catch (IOException e) {
            throw new RuntimeException("parsing CSV file failed");
        }
    }

    private static Integer parseInt(String str) {
        if (str.isBlank()) {
            return null;
        }

        return Integer.valueOf(str);
    }

    private static LocalDate parseDate(String str) {
        if (str.isBlank()) {
            return null;
        }

        return LocalDate.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
