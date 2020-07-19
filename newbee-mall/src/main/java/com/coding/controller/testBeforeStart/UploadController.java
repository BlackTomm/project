package com.coding.controller.testBeforeStart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-10 14:09
 **/
//当使用testBeforeStart上传测试时再解除注解
//@Controller
public class UploadController {
	/*//文件保存路径
	private final static String FILE_UPLOAD_PATH = "E:\\log\\";

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return "upload failed";
		}
		String fileName = file.getOriginalFilename();
		//通过后缀获取文件格式
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		//生成文件名
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Random random = new Random();
		StringBuilder tempFileName = new StringBuilder();
		tempFileName.append(format.format(new Date())).append(random.nextInt(100)).append(suffixName);
		String newFileName = tempFileName.toString();
		try {
			//保存文件
			byte[] bytes = file.getBytes();
			Path path = Paths.get(FILE_UPLOAD_PATH + newFileName);
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "upload succeed";
	}*/
}
