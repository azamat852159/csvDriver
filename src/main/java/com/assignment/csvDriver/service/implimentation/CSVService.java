package com.assignment.csvDriver.service.implimentation;

import com.assignment.csvDriver.exceptions.RecordNotFoundException;
import com.assignment.csvDriver.model.CSVDAO;
import com.assignment.csvDriver.repository.CSVRepository;
import com.assignment.csvDriver.utils.CSVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.util.Collections;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    private CSVRepository csvRepository;

    public List<CSVDAO> upload(InputStream inputStream) {
        List<CSVDAO> csvs = CSVUtil.parseCSV(inputStream);

        return csvRepository.saveAll(csvs);
    }

    public byte[] getByCode(String code) throws RecordNotFoundException, IOException {
        List<CSVDAO> list = Collections.singletonList(csvRepository.findByCode(code).orElseThrow(RecordNotFoundException::new));
        return CSVUtil.createCSV(list);
    }

    public byte[] getAll() throws IOException {
        List<CSVDAO> allCsv = csvRepository.findAll();
        if(allCsv.isEmpty()) {
            return null;
        }

        return CSVUtil.createCSV(csvRepository.findAll());
    }

    public void deleteAll() {
        csvRepository.deleteAll();
    }
}
