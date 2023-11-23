package com.example.demoudemyapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JasperService {

    private final ResourceLoader resourceLoader;
    private final DataSource dataSource;
    private Map<String, Object> parametros = new HashMap<>();

    private static final String JASPER_DIRETORIO = "classpath:reports/";
    public void addParametro(String chave, Object valor) {
        this.parametros.put("IMAGEM_DIRETORIO", JASPER_DIRETORIO);
        this.parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
        this.parametros.put(chave, valor);
    }

    public byte[] gerarPDF() {
        byte[] bytes = null;
        try {
            Resource resource = resourceLoader.getResource(JASPER_DIRETORIO.concat("estacionamentos2.jasper"));
            InputStream stream = resource.getInputStream();
            JasperPrint print = JasperFillManager.fillReport(stream, parametros, dataSource.getConnection());
            bytes = JasperExportManager.exportReportToPdf(print);
        } catch (IOException | JRException | SQLException e) {
            log.error("Erro ao gerar relatorio", e.getCause());
            throw new RuntimeException(e);
        }
        return bytes;
    }
}
