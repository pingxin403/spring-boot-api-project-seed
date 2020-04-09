import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.company.project.framework.object.AbstractDO;
import com.company.project.framework.object.IService;
import com.company.project.framework.object.ServiceImpl;
import com.company.project.plugin.BaseMapper;

import java.util.Scanner;

import static com.company.project.business.consts.ProjectConstant.*;

/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 *
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in PACKAGE_NAME
 * hyp create at 20-4-2
 **/
//@SpringBootTest
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("pingxin");
        gc.setOpen(false);
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        gc.setEnableCache(false); // XML 二级缓存
        gc.setIdType(IdType.ASSIGN_ID);
        gc.setFileOverride(true);
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sMapper");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceImpl");
        // gc.setControllerName("%sController");

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(scanner("模块名"));
        pc.setParent(BASE_PACKAGE);
        pc.setXml(MAPPER_XML_PACKAGE);
        pc.setMapper(MAPPER_PACKAGE);
        pc.setEntity(MODEL_PACKAGE);
        pc.setService(SERVICE_PACKAGE);
        pc.setServiceImpl(SERVICE_IMPL_PACKAGE);
        pc.setController(CONTROLLER_PACKAGE);

        mpg.setPackageInfo(pc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        //RESTful
        strategy.setRestControllerStyle(true);
        strategy.setEntitySerialVersionUID(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setLogicDeleteFieldName("deleted");

        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        strategy.setSuperEntityClass(AbstractDO.class);
        strategy.setSuperMapperClass(BaseMapper.class.getName());
        strategy.setSuperServiceClass(IService.class.getName());
        strategy.setSuperServiceImplClass(ServiceImpl.class.getName());


        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id", "create_time", "modified_time", "delete");
        strategy.setInclude("tb_user");

        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);


        strategy.setTablePrefix("tb_");
        mpg.setStrategy(strategy);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setController("/templates/templatesMybatis/controller.java.vm");
        tc.setService("/templates/templatesMybatis/service.java.vm");
        tc.setServiceImpl("/templates/templatesMybatis/serviceImpl.java.vm");
        tc.setEntity("/templates/templatesMybatis/entity.java.vm");
        tc.setMapper("/templates/templatesMybatis/mapper.java.vm");
        tc.setXml("/templates/templatesMybatis/mapper.xml.vm");


        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        mpg.setTemplate(tc);

        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }

}
