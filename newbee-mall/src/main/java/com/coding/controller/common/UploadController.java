package com.coding.controller.common;

import com.coding.common.Constants;
import com.coding.entity.testBeforeStart.WangEditor;
import com.coding.util.NewBeeMallUtils;
import com.coding.util.Result;
import com.coding.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-10 14:09
 **/
@Slf4j
@Controller
@RequestMapping("/admin")
public class UploadController {

	@PostMapping(value = "/upload/file")
	@ResponseBody
	public Result upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws URISyntaxException {
		String fileName = file.getOriginalFilename();
		//通过后缀获取文件格式
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		//生成文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Random random = new Random();
		StringBuilder tempFileName = new StringBuilder();
		tempFileName.append("carousel_").append(sdf.format(new Date())).append(random.nextInt(100)).append(suffixName);
		String newFileName = tempFileName.toString();
		//上传目标文件夹检验
		File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
		//定义上传目标文件名
		File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
		try {
			//检验目标文件夹是否存在，不存在则创建
			if (!fileDirectory.exists()) {
				if (!fileDirectory.mkdir()) {
					throw new IOException("文件夹创建失败，路径为：" + fileDirectory);
				}
			}
			file.transferTo(destFile);
			Result resultSuccess = ResultGenerator.genSuccessResult();
			resultSuccess.setData(NewBeeMallUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName);
				return resultSuccess;
		} catch (IOException e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("文件上传失败");
		}
	}

}
