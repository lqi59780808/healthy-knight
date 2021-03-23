/**
 * @file:  Generator.java
 * @author: liang_xiaojian
 * @date:   2020/9/1 23:18
 * @copyright: 2020-2023 www.bosssoft.com.cn Inc. All rights reserved.
 */
package generator;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @class Generator
 * @classdesc Mybatis 代码生成器
 * @author liang_xiaojian
 * @date 2020/9/1  23:18
 * @version 1.0.0
 * @see
 * @since
 */
@Slf4j
public class Generator {

    public static InputStream getResourceAsStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        //read local config file in resource directory
        Configuration config = cp.parseConfiguration(getResourceAsStream("generator/generatorConfig.xml"));
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        // generate code in pre-defined file which on resource/mybatis/generator-config.xml
        myBatisGenerator.generate(null);
        for (String warning : warnings) {
            log.info(warning);
        }
    }
}
