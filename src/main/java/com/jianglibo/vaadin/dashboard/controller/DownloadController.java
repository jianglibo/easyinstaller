package com.jianglibo.vaadin.dashboard.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.io.Files;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;

@Controller
@RequestMapping("/download")
public class DownloadController {
    
    @Autowired
	private PkSourceRepository pkSourceRepository;
    
    @Autowired
    private ApplicationConfig applicationConfig;
    
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
    
	@RequestMapping(path = "/{pkname:.+}", method = RequestMethod.GET)
	@ResponseBody
	public void feed(@PathVariable("pkname") String pkname, HttpServletResponse response) {
		Path dst = applicationConfig.getUploadDstPath().resolve(pkname);
		if (dst.startsWith(applicationConfig.getUploadDstPath()) && java.nio.file.Files.exists(dst)) {
			String md5 = pkname.substring(0, pkname.lastIndexOf('.'));
			PkSource pk = pkSourceRepository.findByFileMd5(md5);
			try {
				pk.setDownloadCount(pk.getDownloadCount() + 1);
				pkSourceRepository.save(pk);
				response.setContentLengthLong(dst.toFile().length());
				response.setContentType(pk.getMimeType());      
				response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(pk.getPkname(), "utf-8"));
				Files.asByteSource(dst.toFile()).copyTo(response.getOutputStream());
				response.flushBuffer();
	    	} catch (IOException ex) {
	    		LOGGER.info("Error writing file to output stream. Filename was '{}'", pkname, ex);
	    		throw new RuntimeException("IOError writing file to output stream");
	    	}
		}
	}
}
