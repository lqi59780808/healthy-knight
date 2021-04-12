package com.chh.healthy.backend.utils;

import com.boss.xtrain.core.annotation.stuffer.IdGenerator;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public final class MyUtil {

    /**
     * @param: [multipartFile, idGenerator]
     * @return: java.lang.String
     * @desc: 保存图片
     * @see
     * @since
     */
    public static String saveImage (MultipartFile multipartFile, IdGenerator idGenerator) throws IOException {
        //获取原文件名
        String name=multipartFile.getOriginalFilename();
        //获取文件后缀
        String subffix=name.substring(name.lastIndexOf("."),name.length());
        String fileName = String.valueOf(idGenerator.snowflakeId());
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/picture/";
        File file = new File(path);
        //文件夹不存在就创建
        if(!file.exists())
        {
            file.mkdirs();
        }
        File save = new File(file+"\\"+fileName + subffix);
        multipartFile.transferTo(save);
        String url = "/" + "picture" + "/" + save.getName();
        return url;
    }
}
