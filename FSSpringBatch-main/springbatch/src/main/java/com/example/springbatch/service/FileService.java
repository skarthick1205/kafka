package com.example.springbatch.service;

import com.example.springbatch.model.User;
import com.example.springbatch.repo.UserRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private UserRepository userRepository;

    public String handleFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();

        if (fileName != null && fileName.endsWith(".csv")) {
            File tempFile = new File("/home/karthik/Downloads/FSSpringBatch-main/springbatch/src/main/resources" + fileName);
            tempFile.getParentFile().mkdirs();
            file.transferTo(tempFile);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", tempFile.getAbsolutePath())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
            return "Success";
        } else {
            File folder = new File("/home/karthik/Downloads/FSSpringBatch-main/springbatch/src/main/resources\"+\"removed_files");
            if (!folder.exists()) folder.mkdirs();
            Path destination = folder.toPath().resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return "Not a CSV file";
        }
    }

    public File exportToCsv() throws IOException {
        List<User> users = userRepository.findAll();
        File file = new File("/home/karthik/Downloads/exported_users.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,userName,userEmailId");
            writer.newLine();
            for (User user : users) {
                writer.write(user.getId() + "," + user.getUserName() + "," + user.getUserEmailId());
                writer.newLine();
            }
        }
        return file;
    }
}
