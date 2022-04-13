package com.example.logsever.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class LogController {
    private final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log")
    public StreamingResponseBody stream(HttpServletRequest req) throws Exception {

//        ClassPathResource resource = new ClassPathResource("data/1.txt");
//        File file = resource.getFile();

        File file = new File("/Users/chanho/workspace/company/logsever/src/main/resources/data/1.txt");

        final InputStream is = new FileInputStream(file);
        return os -> {
            readAndWrite(is, os);
        };
    }

    private void readAndWrite(final InputStream is, OutputStream os) throws IOException {
        String temp;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        while (true) {
            temp = buffer.readLine();
            if(temp == null) {
                os.flush();
                try {
                    logger.info("waiting");
                    Thread.sleep(100);
                } catch (Exception ignored) {
                    logger.error("Thread.sleep error");
                }
            } else {
                os.write((temp + "\r\n").getBytes());
                if(temp.equals("END")) {
                    logger.info("END");
                    os.flush();
                    break;
                }
            }
        }
    }

//    @GetMapping (value = "/download", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<StreamingResponseBody> download(final HttpServletResponse response) {
//
//        response.setContentType("application/zip");
//        response.setHeader(
//                "Content-Disposition",
//                "attachment;filename=sample.zip");
//
//        StreamingResponseBody stream = out -> {
//
//            final String home = System.getProperty("user.home");
//            final File directory = new File(home + File.separator + "Documents" + File.separator + "sample");
//            final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
//
//            if(directory.exists() && directory.isDirectory()) {
//                try {
//                    for (final File file : directory.listFiles()) {
//                        final InputStream inputStream=new FileInputStream(file);
//                        final ZipEntry zipEntry=new ZipEntry(file.getName());
//                        zipOut.putNextEntry(zipEntry);
//                        byte[] bytes=new byte[1024];
//                        int length;
//                        while ((length=inputStream.read(bytes)) >= 0) {
//                            zipOut.write(bytes, 0, length);
//                        }
//                        inputStream.close();
//                    }
//                    zipOut.close();
//                } catch (final IOException e) {
//                    logger.error("Exception while reading and streaming data {} ", e);
//                }
//            }
//        };
//        logger.info("steaming response {} ", stream);
//        return new ResponseEntity(stream, HttpStatus.OK);
//    }

}
